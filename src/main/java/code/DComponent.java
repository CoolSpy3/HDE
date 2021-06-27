package code;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Represents a component in editing mode
 */
public class DComponent implements Serializable {

    private static final long serialVersionUID = 8551699257836397079L;
    
    private final int id;
    /**
     * The alias of the image which should be used when rendering this component
     * @see ResourceManager#getImage(java.lang.String) 
     */
    public final String imageName;
    /**
     * The size of this component in component coordinate space
     */
    public final Dimension size;
    /**
     * The position of this component in component coordinate space
     */
    public final Point2D.Double pos;
    //in terms of pi/2 c
    private int rotation;
    /**
     * A Map mapping port ids on the left side of this component to their distances from the top left corner of this component
     */
    public final HashMap<String, Integer> linputs;
    /**
     * A Map mapping port ids on the right side of this component to their distances from the top right corner of this component
     */
    public final HashMap<String, Integer> rinputs;
    /**
     * A Map mapping port ids on the top side of this component to their distances from the top left corner of this component
     */
    public final HashMap<String, Integer> tinputs;
    /**
     * A Map mapping port ids on the bottom side of this component to their distances from the bottom left corner of this component
     */
    public final HashMap<String, Integer> binputs;

    /**
     * Creates a new DComponent with the given information
     * @param imageName The alias of the image which should be used when rendering this component
     * @param size The size of this component in component coordinate space
     * @param pos The position of this component in component coordinate space
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public DComponent(String imageName, Dimension size, Point2D.Double pos) {
        // Allocate an id for this component
        this.id = ResourceManager.allocId(this);
        
        // Initialize variables
        this.imageName = imageName;
        this.size = size;
        this.pos = new Point2D.Double(pos.x, pos.y);
        this.rotation = 0;
        this.linputs = new HashMap<>();
        this.rinputs = new HashMap<>();
        this.tinputs = new HashMap<>();
        this.binputs = new HashMap<>();
    }
    
    /**
     * Attempts to create a copy of this component by searching for a constructor which accepts a position, invoking it, and rotating the resulting component to match this component's orientation
     * @return The new component
     * @throws ReflectiveOperationException if an error occurred while attempting to create the new component
     * @see #safeCopy() 
     */
    public DComponent copy() throws ReflectiveOperationException {
        try {
            // Search for a constructor which accepts a Point2D.Double and invoke it with pos
            DComponent comp = this.getClass().getConstructor(Point2D.Double.class).newInstance(pos);
            
            for(int i = 0; i < rotation; i++) {
                // Rotate this component to match our rotation
                comp.rotate();
            }
            
            // Return the result
            return comp;
        } catch(IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            // If an exception occurs, wrap it in a ReflectiveOperationException and throw it
            throw new ReflectiveOperationException(e instanceof InvocationTargetException ? e.getCause() : e);
        }
    }
    
    /**
     * Attempts to call <code>this.{@link #copy()}</code> and passes the resulting exception to {@link Utils#_throw(java.lang.Throwable)} if it occurs
     * @return The result of {@link #copy()} or <code>null</code> if an exception was thrown
     * @see #copy() 
     */
    public DComponent safeCopy() {
        try {
            // Try to copy the component
            return copy();
        } catch(ReflectiveOperationException e) {
            // If an error occurs, log it and return null
            Utils._throw(e);
            return null;
        }
    }
    
    /**
     * Rotates this component clockwise around its center and updates all of the port maps to reflect the new orientation
     * @see #getRotation() 
     */
    public void rotate() {
        // Increment rotation
        rotation++;
        
        // Cap it to [0,3]
        rotation %= 4;
        
        // Rotate the component's top-left corner so that the component stays centered
        double a = pos.getX()+(size.width/2D);
        double b = pos.getY()+(size.height/2D);
        pos.setLocation(pos.getX()+(size.width/2D), pos.getY()+(size.height/2D));
        size.setSize(size.height, size.width);
        pos.setLocation(pos.getX()-(size.width/2D), pos.getY()-(size.height/2D));
        
        // Rotate the ports to the correct side
        Utils.rotate(linputs, tinputs, rinputs, binputs);
    }
    
    /**
     * @return the current rotation of this component from its default position as a number of increments of 90 degrees clockwise
     * @see #rotate() 
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * @return The unique id of this component
     * @see ResourceManager#allocId(code.DComponent) 
     */
    public int getId() {
        return id;
    }
    
    /**
     * Retrieves the image which should be used to render this component.
     * This uses the {@link ResourceManager#getImage(java.lang.String)} function to search for an image with the id <code>imageName + (rotation * 90)</code>
     * @return The image which should be used to render this component with any required rotation applied
     * @see #imageName
     * @see #getRotation() 
     * @see #rotate() 
     */
    public Image getImage() {
        return ResourceManager.getImage(imageName + (rotation * 90));
    }
    
    /**
     * @return The position of this component rounded to integer precision
     * @see #pos
     */
    public Point intPoint() {
        Point p = new Point();
        p.setLocation(pos);
        return p;
    }
    
    /**
     * @return A rectangle representing the space occupied by this component
     */
    public Rectangle getBounds() {
        return new Rectangle(intPoint(), size);
    }
    
    /**
     * @return A list of TaggedDoublePoint objects representing the locations of all of the ports on this component in component coordinate space with their tags set to the id of the corresponding port
     */
    public List<TaggedDoublePoint> getPoints() {
        List<TaggedDoublePoint> out = new ArrayList<>();
        
        // Get all ports and their positions
        out.addAll(linputs.entrySet().stream().map(p -> new TaggedDoublePoint(0, p.getValue(), p.getKey())).collect(Collectors.toList()));
        out.addAll(rinputs.entrySet().stream().map(p -> new TaggedDoublePoint(size.width-1, p.getValue(), p.getKey())).collect(Collectors.toList()));
        out.addAll(tinputs.entrySet().stream().map(p -> new TaggedDoublePoint(p.getValue(), 0, p.getKey())).collect(Collectors.toList()));
        out.addAll(binputs.entrySet().stream().map(p -> new TaggedDoublePoint(p.getValue(), size.height-1, p.getKey())).collect(Collectors.toList()));
        
        // Convert coordinates from being relative to the top-left corner to being relative to the component space
        out = out.stream().map(point -> new TaggedDoublePoint(point.x+pos.x+1, point.y+pos.y+1, point.tag)).collect(Collectors.toList());
        
        return out;
    }
    
    /**
     * Retrieves the position of the given port
     * @param id The id of the port to search for
     * @return A TaggedDoublePoint with a position which represents the location of the specified port and its tag set to the port id
     * @throws NoSuchElementException if this component does not contain a port wit the given id
     */
    public TaggedDoublePoint getPoint(String id) throws NoSuchElementException {
        // For all ports, find any with a matching id. Return one of them or throw an exception if none exist
        return getPoints().stream().filter(p -> p.tag.equals(id)).findAny().orElseThrow();
    }
    
}