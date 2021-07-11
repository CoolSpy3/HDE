package com.coolspy3.hde;

import com.coolspy3.hde.component.EditorComponent;
import com.coolspy3.hde.component.components.EJunction;
import com.coolspy3.hde.component.components.SANDGate;
import com.coolspy3.hde.component.components.SBuffer;
import com.coolspy3.hde.component.components.SJunction;
import com.coolspy3.hde.component.components.SNANDGate;
import com.coolspy3.hde.component.components.SNORGate;
import com.coolspy3.hde.component.components.SNOTGate;
import com.coolspy3.hde.component.components.SORGate;
import com.coolspy3.hde.component.components.SXNORGate;
import com.coolspy3.hde.component.components.SXORGate;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JPanel;

/**
 * Represents the editing workspace which the user will interact with to create logic layouts
 */
public class ContentPanel extends JPanel implements ContainerListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private static final long serialVersionUID = -6307690966007887396L;

    /**
     * The value which the {@link #scale} value will be altered by for every unit scrolled
     */
    public static final double SCROLL_SCALE = 0.1;
    /**
     * The minimum value for {@link #scale}
     */
    public static final double SCALE_MIN = 0.35;
    /**
     * The maximum value for {@link #scale}
     */
    public static final double SCALE_MAX = 5;
    /**
     * The percentage of the line which the first angle will be moved when scrolling while connecting two components
     */
    public static final double SCROLL_MOV_LINE_PER = 0.05;

    /**
     * The position of the center of the frame in component coordinate space 
     */
    public final Point2D.Double pos;
    /**
     * The amount by which component coordinate space is scaled when it is rendered to the frame
     */
    public double scale;
    private final GUI gui;
    private final ComponentSelector selector;

    private final ArrayList<EditorComponent> comps;
    private EditorComponent selectedComponent;
    private final ArrayList<EditorComponent> draggedComponents;
    private final Point tldc;
    private final Point brdc;
    private final Point mouseDragStart;
    private final Point mousePos;

    private long selectedPortCompId;
    private final Point2D.Double selectedPortPos;
    private String selectedPortId;

    private boolean lbd;
    private boolean ctrl;
    private boolean shift;

    private boolean showLine;
    private final Point2D.Double lineStart;
    private double linePXMov;
    private boolean lineStartX;
    private long lineStartCompId;
    private String lineStartPortId;
    private final ArrayList<Line> lines;

    private final ArrayList<EditorComponent> clipboardComps;
    private final ArrayList<Line> clipboardLines;

    /**
     * Creates a new ContentPanel and initializes it with a blank workspace
     * @param gui The frame which will contain this ContentPanel
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public ContentPanel(GUI gui) {
        // Save parent window reference
        this.gui = gui;

        // Ensure all component's have registered images
        initComps();

        // Create a ComponentSelector
        this.selector = new ComponentSelector(gui, gui);

        // Register Listeners
        gui.getContentPane().addContainerListener(this);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setFocusable(true);

        // Set variables to default values
        draggedComponents = new ArrayList<>();
        pos = new Point2D.Double();
        scale = 2;
        lbd = false;
        ctrl = false;
        shift = false;
        selectedPortCompId = -1;
        selectedPortPos = new Point2D.Double();
        selectedPortId = "";
        tldc = new Point();
        brdc = new Point();
        mouseDragStart = new Point();
        mousePos = new Point();
        showLine = false;
        lineStart = new Point2D.Double();
        linePXMov = 0;
        lineStartX = true;
        lineStartCompId = -1;
        lineStartPortId = "";
        lines = new ArrayList<>();
        comps = new ArrayList<>();
        clipboardComps = new ArrayList<>();
        clipboardLines = new ArrayList<>();
    }

    /**
     * Creates a new ContentPanel and initializes it with the components loaded from the provided file
     * @param gui The frame which will contain this ContentPanel
     * @param file The file from which to load the components
     * @throws IOException if an error occurs reading or parsing the input file
     */
    @SuppressWarnings("unchecked")
    public ContentPanel(GUI gui, File file) throws IOException {
        // Init the window normally
        this(gui);

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            // Load the data from the file
            comps.addAll((Collection<? extends EditorComponent>)ois.readObject());
            lines.addAll((Collection<? extends Line>)ois.readObject());

            // Register all of the components and lines
            ResourceManager.forceLoad(comps, lines);
        } catch(ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    /**
     * Converts the given point from the component coordinate system into a position on the frame
     * @param ip The point to convert
     * @return The position of the given point on the frame (in pixels)
     * @see #toFrameCoords(java.awt.Rectangle) 
     * @see #fromFrameCoords(java.awt.Point) 
     * @see #fromFrameCoords(java.awt.Rectangle) 
     */
    public Point toFrameCoords(Point2D.Double ip) {
        // output_point = ((input_point + pos) * scale) + (window_size / 2)
        Point2D.Double p = new Point2D.Double(ip.x+pos.x, ip.y+pos.y);
        Rectangle bounds = getBounds();
        return new Point(Utils.asInt((bounds.width/2)+(p.getX()*scale)), Utils.asInt((bounds.height/2)+(p.getY()*scale)));
    }

    /**
     * Converts the given rectangle from the component coordinate system into a position on the frame
     * @param rect The rectangle to convert
     * @return The position of the given rectangle on the frame (in pixels)
     * @see #toFrameCoords(java.awt.geom.Point2D.Double)
     * @see #fromFrameCoords(java.awt.Rectangle) 
     * @see #fromFrameCoords(java.awt.Point) 
     */
    public Rectangle toFrameCoords(Rectangle rect) {
        // Convert the top-left corner into frame coords and multiply the width and height by scale
        return new Rectangle(toFrameCoords(Utils.asDouble(rect.getLocation())), new Dimension(Utils.asInt(rect.width*scale), Utils.asInt(rect.height*scale)));
    }

    /**
     * Converts the given point on the frame into the component coordinate system
     * @param ip The point to convert
     * @return The position of the given point in the component coordinate system
     * @see #fromFrameCoords(java.awt.Rectangle)
     * @see #toFrameCoords(java.awt.geom.Point2D.Double)
     * @see #toFrameCoords(java.awt.Rectangle) 
     */
    public Point2D.Double fromFrameCoords(Point ip) {
        // output_point = ((input_point - (window_size / 2)) / scale) - pos 
        Rectangle bounds = getBounds();
        Point2D.Double p = new Point2D.Double((ip.getX()-(bounds.width/2))/scale, (ip.getY()-(bounds.height/2))/scale);
        return new Point2D.Double(p.x-pos.x, p.y-pos.y);
    }

    /**
     * Converts the given rectangle on the frame into the component coordinate system
     * @param rect The rectangle to convert
     * @return The position of the given rectangle in the component coordinate system
     * @see #fromFrameCoords(java.awt.Point) 
     * @see #toFrameCoords(java.awt.Rectangle)
     * @see #toFrameCoords(java.awt.geom.Point2D.Double) 
     */
    public Rectangle fromFrameCoords(Rectangle rect) {
        // Convert the top-left corner into component coords and divide the width and height by scale
        return new Rectangle(Utils.asInt(fromFrameCoords(rect.getLocation())), new Dimension(Utils.asInt(rect.width/scale), Utils.asInt(rect.height/scale)));
    }

    /**
     * @return The component which the user is currently hovering over
     */
    public synchronized EditorComponent getSelectedComponent() {
        return selectedComponent;
    }

    private synchronized void setSelectedComponent(EditorComponent comp) {
        this.selectedComponent = comp;
    }

    /**
     * Checks whether the given component collides with the given position in the frame coordinate system
     * @param comp The component to check
     * @param pos The position to check in frame coordinates
     * @return Whether the component collides with the given point
     */
    public boolean collidesWithPoint(EditorComponent comp, Point pos) {
        return toFrameCoords(comp.getBounds()).contains(pos);
    }

    /**
     * Adds the given component to the list of components currently selected by the user
     * @param comp The component to add
     */
    public void addComponentToSelection(EditorComponent comp) {
        Point compPos = toFrameCoords(comp.getPosition());
        if(draggedComponents.isEmpty()) {
            // If no components are dragged, set the selection rectangle to the bounds of the component
            tldc.setLocation(compPos);
            compPos.translate(Utils.asInt(comp.getSize().width*scale), Utils.asInt(comp.getSize().height*scale));
            brdc.setLocation(compPos);
        } else {
            // Otherwise, set it to expand (if neccessary) to encompass the new component
            if(compPos.x < tldc.x) {
                tldc.setLocation(compPos.x, tldc.y);
            }
            if(compPos.y < tldc.y) {
                tldc.setLocation(tldc.x, compPos.y);
            }
            compPos.translate(Utils.asInt(comp.getSize().width*scale), Utils.asInt(comp.getSize().height*scale));
            if(compPos.x > brdc.x) {
                brdc.setLocation(compPos.x, brdc.y);
            }
            if(compPos.y > brdc.y) {
                brdc.setLocation(brdc.x, compPos.y);
            }
        }
        // Add the component to the list of dragged components if it isn't already
        if(!draggedComponents.contains(comp)) {
            draggedComponents.add(comp);
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        // Setup buffer
        Image img = createImage(getBounds().width, getBounds().height);
        Graphics2D g = (Graphics2D)img.getGraphics();
        g.setColor(new Color(0, 0, 0, 255));

        // Draw grid
        if(gui.shouldShowGrid() && scale >= 0.4) {
            for(int x = 0; x < getBounds().width; x++) {
                for(int y = 0; y < getBounds().height; y++) {
                    if((x-Utils.asInt(pos.x*scale)) % Utils.asInt(20*scale) == 0 && (y-Utils.asInt(pos.y*scale)) % Utils.asInt(20*scale) == 0) {
                        g.drawOval(x, y, 2, 2);
                    }
                }
            }
        }

        // Draw components (if they are on screen)
        comps.forEach(comp -> {
            if(toFrameCoords(comp.getBounds()).intersects(getBounds())) {
                Point tl = toFrameCoords(comp.getPosition());
                g.drawImage(comp.getImage(), tl.x, tl.y, Utils.asInt(comp.getSize().width*scale), Utils.asInt(comp.getSize().height*scale), null);
            }
        });

        g.setColor(new Color(0, 255, 255, 255));

        // Draw selection rectangle
        if(shift && lbd) {
            g.draw(Utils.rectangleFromAnyCorners(mouseDragStart, mousePos));
        } else if(!draggedComponents.isEmpty()) {
            g.draw(Utils.rectangleFromCorners(tldc, brdc));
        }

        g.setColor(new Color(0, 255, 0, 255));

        // Highlight selected port
        if(selectedPortCompId != -1) {
            Point portPos = toFrameCoords(selectedPortPos);
            g.fillRect(portPos.x, portPos.y, 2, 2);
        }

        g.setColor(new Color(0, 0, 0, 255));

        g.setStroke(new BasicStroke(2*(int)scale));

        // Draw line (if one is being created)
        if(showLine) {
            Utils.drawLine(g, toFrameCoords(lineStart), mousePos, lineStartX, linePXMov);
        }

        // Draw lines (if on screen)
        lines.forEach((line) -> {
            if(getBounds().contains(toFrameCoords(((EditorComponent)ResourceManager.getComponent(line.compId1)).getPoint(line.portId1))) || getBounds().contains(toFrameCoords(((EditorComponent)ResourceManager.getComponent(line.compId2)).getPoint(line.portId2)))) {
                Utils.drawLine(g, toFrameCoords(((EditorComponent)ResourceManager.getComponent(line.compId1)).getPoint(line.portId1)),
                        toFrameCoords(((EditorComponent)ResourceManager.getComponent(line.compId2)).getPoint(line.portId2)), line.isHoris, line.movPer);
            }
        });

        // Dispose graphis and render buffer
        g.dispose();
        graphics.drawImage(img, 0, 0, null);
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        // When we are added to the frame, request focus
        if(e.getChild()== this) {
            requestFocusInWindow();
        }
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        // When we are removed from the frame
        if(e.getChild() == this) {
            // Remove Listeners
            gui.getContentPane().removeContainerListener(this);
            removeKeyListener(this);
            removeMouseListener(this);
            removeMouseMotionListener(this);
            removeMouseWheelListener(this);

            // Remove ComponentSelector
            selector.setVisible(false);
            selector.dispose();

            // Clear components and lines
            comps.clear();
            lines.clear();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == '0') {
            // Reset position and scale to defaults
            pos.setLocation(0, 0);
            scale = 2;

        // If a single component is selected
        } else if(e.getKeyChar() == 'r' && draggedComponents.size() == 1) {
            // Rotate selected component
            EditorComponent comp = draggedComponents.get(0);
            comp.rotate();

            // Update selection
            tldc.setLocation(toFrameCoords(comp.getPosition()));
            brdc.setLocation(toFrameCoords(comp.getPosition()));
            brdc.translate(comp.getSize().width*(int)scale, comp.getSize().height*(int)scale);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Update control and shift flags
        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrl = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DELETE) {
            // Delete all selected components
            draggedComponents.forEach(comp -> {
                ArrayList<Line> removedLines = new ArrayList<>();
                // Disconnect all lines
                lines.stream().filter(l -> l.compId1 == comp.getId() || l.compId2 == comp.getId()).forEach(l -> {
                    ResourceManager.freePorts(l);
                    removedLines.add(l);
                });

                // Delete components
                lines.removeAll(removedLines);
                comps.remove(comp);
            });

            // Clear the selection
            draggedComponents.clear();
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            showLine = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_C && ctrl) {
            // Clear the clipboard
            clipboardComps.clear();
            clipboardLines.clear();

            if(draggedComponents.isEmpty()) {
                // Don't try to copy nothing
                return;
            }

            // Add selection to clipboard
            clipboardComps.addAll(draggedComponents);
            clipboardLines.addAll(lines);
            List<Long> clipboardIds = clipboardComps.stream().map(EditorComponent::getId).collect(Collectors.toList());

            // Remove all lines unless both their endpoints are part of the clipboard
            clipboardLines.removeIf(line -> !clipboardIds.contains(line.compId1) || !clipboardIds.contains(line.compId2));
        }
        if(e.getKeyCode() == KeyEvent.VK_V && ctrl) {
            // Copy components
            ArrayList<EditorComponent> newComps = new ArrayList<>();
            ResourceManager.addAsNew(clipboardComps, clipboardLines, fromFrameCoords(mousePos), newComps, lines, this);

            // Clear selected components
            draggedComponents.clear();

            // Select new components
            newComps.forEach(comp -> addComponentToSelection(comp));
            comps.addAll(newComps);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Update control and shift flags
        if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrl = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // If the user is not edition a selection and a port is selected
        if(!(ctrl && getSelectedComponent() != null) && !shift && selectedPortCompId != -1) {
            // Start drawing a line at the selected port
            if(ResourceManager.portStatus(selectedPortCompId, selectedPortId)) {
                return;
            }

            // Clear selected components
            draggedComponents.clear();

            // Start drawing a line at the selected position
            lineStart.setLocation(selectedPortPos);
            linePXMov = 0.5;

            // Determine whether line will be horizontal or vertical
            EditorComponent selectedPortComp = (EditorComponent)ResourceManager.getComponent(selectedPortCompId);
            Point2D.Double selectedPortPosRTComp = new Point2D.Double(selectedPortPos.x, selectedPortPos.y);
            selectedPortPosRTComp.setLocation(selectedPortPosRTComp.x-selectedPortComp.getPosition().x-1, selectedPortPosRTComp.y-selectedPortComp.getPosition().y-1);
            lineStartX = selectedPortComp.getLeftPorts().containsValue((int)selectedPortPosRTComp.getY()) || selectedPortComp.getRightPorts().containsValue((int)selectedPortPosRTComp.getY());

            // Start drawing the line
            lineStartCompId = selectedPortCompId;
            lineStartPortId = selectedPortId;
            showLine = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() != 1) {
            if(e.getButton() == 2 && getSelectedComponent() == null) {
                // A component has been right-clicked
            }
            return;
        }

        // Update left mouse button flag
        lbd = true;

        // Store the mouse position in case the user starts dragging the mouse
        mouseDragStart.setLocation(e.getPoint());

        // Has the user selected a point within the current selection
        boolean mouseInRect = !draggedComponents.isEmpty() && Utils.rectangleFromCorners(tldc, brdc).contains(e.getPoint());

        if(!(ctrl && getSelectedComponent() != null) && !shift && !mouseInRect) {
            // If the user is not editing a selection, clear the selection
            draggedComponents.clear();
        }

        // If a line is being drawn
        if(showLine) {
            if(selectedPortCompId == -1 || ResourceManager.portStatus(selectedPortCompId, selectedPortId) || (selectedPortCompId == lineStartCompId && selectedPortId.equals(lineStartPortId))) {
                // If no port is selected (or that port is in use)

                // Create a EJunction at the specified position

                // Get the position of the click in the component coordinate system
                Point2D.Double ePointNFC = fromFrameCoords(e.getPoint());

                // The line will end in the same horizontal / vertical direction it started in unless the bend occures at l00% of the line's length
                boolean lineEndX = linePXMov == 1 ? !lineStartX : lineStartX;

                // Create a EJunction so that the new port aligns with the mouse position
                EJunction junction = new EJunction(new Point2D.Double(
                        lineEndX ? ePointNFC.x - (lineStart.x > ePointNFC.x ? 20 : 0) : ePointNFC.x-9,
                        lineEndX ? ePointNFC.y-9 : ePointNFC.y - (lineStart.y > ePointNFC.y ? 20 : 0)));
                comps.add(junction);

                // Reserve a newly created port
                String port = lineEndX ? ePointNFC.x > lineStart.x ? "P2" : "P3" : ePointNFC.y > lineStart.y ? "P4" : "P1";
                ResourceManager.reservePort(junction, port);
                ResourceManager.reservePort(lineStartCompId, lineStartPortId);

                // Create the line
                lines.add(new Line(lineStartCompId, lineStartPortId, junction.getId(), port, lineStartX, linePXMov));
                showLine = false;
            } else {
                // Reserve the selected port
                ResourceManager.reservePort(selectedPortCompId, selectedPortId);
                ResourceManager.reservePort(lineStartCompId, lineStartPortId);

                // Create the line
                lines.add(new Line(lineStartCompId, lineStartPortId, selectedPortCompId, selectedPortId, lineStartX, linePXMov));
                showLine = false;
            }
        } else if(getSelectedComponent() != null) {
            if(!mouseInRect) {
                // If a component is selected outside of the current selection, add it to the selection
                addComponentToSelection(getSelectedComponent());
            }
        } else {
            // Add a new component to the selection if one is selected
            EditorComponent comp = selector.getAndReset(fromFrameCoords(e.getPoint()));
            if(comp != null) {
                comps.add(comp);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == 1) {
            // Update left mouse button flag
            lbd = false;

            if(shift) {
                // If the user was holding shift, create a selection and select all components contained within it
                Rectangle selection = fromFrameCoords(Utils.rectangleFromAnyCorners(mouseDragStart, e.getPoint()));
                comps.stream().filter(comp -> comp.getBounds().intersects(selection)).forEachOrdered(comp -> addComponentToSelection(comp));
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // If the mouse is dragged, it has also been moved
        mouseMoved(e);

        // If the left mouse button is not pressed or the user is holding shift, do nothing
        if(!lbd || shift) {
            return;
        }

        // Calculate the distance the mouse moved
        int rdx = e.getX()-mouseDragStart.x;
        int rdy = e.getY()-mouseDragStart.y;
        double dx = rdx/scale;
        double dy = rdy/scale;

        if(draggedComponents.isEmpty()) {
            // If no components are selected, drag the whole scene
            pos.setLocation(pos.x+dx, pos.y+dy);
        } else {
            // Otherwise, drag the selected components
            tldc.translate(rdx, rdy);
            brdc.translate(rdx, rdy);
            draggedComponents.forEach(comp -> {
                comp.setPosition(comp.getPosition().x + dx, comp.getPosition().y + dy);
            });
        }

        // If the mouse is dragged again, assume it started from it's current position (don't drag components by a distance the mouse has already moved)
        mouseDragStart.setLocation(e.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update the mouse position
        mousePos.setLocation(e.getPoint());

        // Update the selected component
        setSelectedComponent(null);
        comps.stream().filter(comp -> (collidesWithPoint(comp, e.getPoint()))).forEachOrdered(comp -> {
            setSelectedComponent(comp);
        });

        // Update the selected port
        selectedPortCompId = -1;
        double dist = Integer.MAX_VALUE;

        // For every component
        for(EditorComponent comp : comps) {
            // If it's on screen
            if(toFrameCoords(comp.getBounds()).intersects(getBounds())) {
                // For every port it has
                for(TaggedDoublePoint point : comp.getPoints()) {
                    // If that port is closer to the mouse than any other we've checked and is at most 12 pixels away
                    double tDist = toFrameCoords(point).distance(e.getPoint());
                    if(tDist <= 12 && tDist < dist) {
                        // Set it as the selected port
                        selectedPortCompId = comp.getId();
                        selectedPortPos.setLocation(point);
                        selectedPortId = point.tag;

                        // And update the distance of the closest known port
                        dist = tDist;
                    }
                }
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(showLine && ctrl) {
            // If we're drawing a line, update the location where it bends
            linePXMov += e.getUnitsToScroll()*SCROLL_MOV_LINE_PER;

            // Cap the result to between 0 and 1
            linePXMov = linePXMov < 0 ? 0 : linePXMov;
            linePXMov = linePXMov > 1 ? 1 : linePXMov;
        } else {
            // Otherwise, update the scale
            double oldScale = scale;
            scale += e.getUnitsToScroll()*SCROLL_SCALE*-1;

            // Cap the result to bettween SCALE_MIN and SCALE_MAX
            scale = scale < SCALE_MIN ? SCALE_MIN : scale;
            scale = scale > SCALE_MAX ? SCALE_MAX : scale;

            // If the scale changed and components are being dragged
            if(oldScale != scale && !draggedComponents.isEmpty()) {
                // Update the selection box to reflect the new positions of the components
                EditorComponent comp0 = draggedComponents.get(0);
                Point compPos0 = toFrameCoords(comp0.getPosition());
                tldc.setLocation(compPos0);
                compPos0.translate(Utils.asInt(comp0.getSize().width*scale), Utils.asInt(comp0.getSize().height*scale));
                brdc.setLocation(compPos0);
                draggedComponents.forEach(comp -> {
                    Point compPos = toFrameCoords(comp.getPosition());
                    if(compPos.x < tldc.x) {
                        tldc.setLocation(compPos.x, tldc.y);
                    }
                    if(compPos.y < tldc.y) {
                        tldc.setLocation(tldc.x, compPos.y);
                    }
                    compPos.translate(Utils.asInt(comp.getSize().width*scale), Utils.asInt(comp.getSize().height*scale));
                    if(compPos.x > brdc.x) {
                        brdc.setLocation(compPos.x, brdc.y);
                    }
                    if(compPos.y > brdc.y) {
                        brdc.setLocation(brdc.x, compPos.y);
                    }
                });
            }
        }
    }

    /**
     * Saves the current workspace to a file
     * @param file The file to which to save
     * @throws IOException if an error occurs while saving
     */
    public void save(File file) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            // Save components and lines to the file
            oos.writeObject(comps);
            oos.writeObject(lines);
        }
    }

    private void initComps() {
        // Attempt to load base component classes
        // This causes their static blocks to be run
        tryInit(SANDGate.class.getName());
        tryInit(SBuffer.class.getName());
        tryInit(SNANDGate.class.getName());
        tryInit(SNORGate.class.getName());
        tryInit(SNOTGate.class.getName());
        tryInit(SORGate.class.getName());
        tryInit(SXNORGate.class.getName());
        tryInit(SXORGate.class.getName());
        tryInit(SJunction.class.getName());
    }

    private void tryInit(String clazz) {
        try {
            // Find the class
            // This loads it and causes its static block to be run if it hasn't already
            Class.forName(clazz);
        } catch(ClassNotFoundException e) { /* Ignore exceptions */ }
    }

}
