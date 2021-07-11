package com.coolspy3.hde.component;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.TaggedDoublePoint;
import com.coolspy3.hde.Utils;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class EditorComponent extends BaseComponent<Pair<EditorComponent.Side, Integer>> implements DisplayableComponent {

    private static final long serialVersionUID = -548534437516062448L;

    private final String imageName;
    private final Dimension size;
    private final Point2D.Double pos;
    //in terms of pi/2 c
    private int rotation;

    /**
     * Creates a new DComponent with the given information
     * @param imageName The alias of the image which should be used when rendering this component
     * @param size The size of this component in component coordinate space
     * @param pos The position of this component in component coordinate space
     */
    public EditorComponent(String imageName, Dimension size, Point2D.Double pos) {
        this.imageName = imageName;
        this.size = size;
        this.pos = new Point2D.Double(pos.x, pos.y);
        this.rotation = 0;
    }

    protected void putLeft(String portId, int pos) {
        ports.put(portId, new Pair<>(Side.LEFT, pos));
    }

    protected void putRight(String portId, int pos) {
        ports.put(portId, new Pair<>(Side.RIGHT, pos));
    }

    protected void putTop(String portId, int pos) {
        ports.put(portId, new Pair<>(Side.TOP, pos));
    }

    protected void putBottom(String portId, int pos) {
        ports.put(portId, new Pair<>(Side.BOTTOM, pos));
    }

    /**
     * Attempts to create a copy of this component by searching for a constructor which accepts a position, invoking it, and rotating the resulting component to match this component's orientation
     * @return The new component
     * @throws ReflectiveOperationException if an error occurred while attempting to create the new component
     * @see #safeCopy() 
     */
    public EditorComponent copy() throws ReflectiveOperationException {
        try {
            // Search for a constructor which accepts a Point2D.Double and invoke it with pos
            EditorComponent comp = this.getClass().getConstructor(Point2D.Double.class).newInstance(pos);

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
    public EditorComponent safeCopy() {
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

        ports.values().forEach(port -> {
            Side newSide = null;
            switch(port.getT()) {
                case LEFT:
                    newSide = Side.TOP;
                    break;
                case TOP:
                    newSide = Side.RIGHT;
                    break;
                case RIGHT:
                    newSide = Side.BOTTOM;
                    break;
                case BOTTOM:
                    newSide = Side.LEFT;
                    break;
            }
            port.setT(newSide);
        });
    }

    public Map<String, Integer> getPortsOnSide(Side side) {
        return ports.entrySet().stream().filter(port -> port.getValue().getT() == side).collect(Collectors.toMap(Map.Entry::getKey, port -> port.getValue().getU()));
    }

    public Map<String, Integer> getLeftPorts() {
        return getPortsOnSide(Side.LEFT);
    }

    public Map<String, Integer> getRightPorts() {
        return getPortsOnSide(Side.RIGHT);
    }

    public Map<String, Integer> getTopPorts() {
        return getPortsOnSide(Side.TOP);
    }

    public Map<String, Integer> getBottomPorts() {
        return getPortsOnSide(Side.BOTTOM);
    }

    /**
     * @return A list of TaggedDoublePoint objects representing the locations of all of the ports on this component in component coordinate space with their tags set to the id of the corresponding port
     */
    public List<TaggedDoublePoint> getPoints() {
        List<TaggedDoublePoint> out = new ArrayList<>();

        // Get all ports and their positions
        out.addAll(getLeftPorts().entrySet().stream().map(p -> new TaggedDoublePoint(0, p.getValue(), p.getKey())).collect(Collectors.toList()));
        out.addAll(getRightPorts().entrySet().stream().map(p -> new TaggedDoublePoint(size.width-1, p.getValue(), p.getKey())).collect(Collectors.toList()));
        out.addAll(getTopPorts().entrySet().stream().map(p -> new TaggedDoublePoint(p.getValue(), 0, p.getKey())).collect(Collectors.toList()));
        out.addAll(getBottomPorts().entrySet().stream().map(p -> new TaggedDoublePoint(p.getValue(), size.height-1, p.getKey())).collect(Collectors.toList()));

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

    @Override
    public int getRotation() {
        return rotation;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public Point2D.Double getPosition() {
        return pos;
    }

    @Override
    public Image getImage() {
        return ResourceManager.getImage(getImageName() + (getRotation() * 90));
    }

    public void setPosition(double x, double y) {
        this.pos.setLocation(x, y);
    }

    public static enum Side {
        LEFT, RIGHT, TOP, BOTTOM;

        private Side() {}
    }

}
