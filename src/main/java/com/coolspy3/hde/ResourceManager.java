package com.coolspy3.hde;

import com.coolspy3.hde.component.BaseComponent;
import com.coolspy3.hde.component.EditorComponent;
import java.awt.Component;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Cleaner;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Manages the allocation of component and port ids as well as component images
 */
public final class ResourceManager {

    private static final Cleaner cleaner = Cleaner.create();
    private static final HashMap<String, BufferedImage> images = new HashMap<>();
    private static final HashMap<Long, WeakReference<BaseComponent<?>>> compids = new HashMap<>();
    private static final HashMap<Long, ArrayList<String>> ports = new HashMap<>();
    private static final ReentrantReadWriteLock compidLock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock portLock = new ReentrantReadWriteLock();
    private static Object stateKey = new Object();

    /**
     * Loads the image from the given file and stores it with the given alias
     * @param name The alias of the image
     * @param path The file from which to load the image
     * @throws IOException if an error occurs loading the image
     * @see #getImage(java.lang.String) 
     * @see #loadImages(java.lang.String, java.lang.String) 
     * @see #putImage(java.lang.String, java.awt.image.BufferedImage) 
     */
    public static void loadImage(String name, String path) throws IOException {
        images.put(name, ImageIO.read(new File(path)));
    }

    /**
     * Loads the image from the given file and stores it with the given alias as well as creating 4 other images which correspond to the original image rotated by a multiple of 90 degrees.
     * This creates 5 new image entries with the following names:
     * <code>name</code> - The original image
     * <code>name0</code> - The original image
     * <code>name90</code> - The original image rotated 90 degrees clockwise
     * <code>name180</code> - The original image rotated 180 degrees clockwise
     * <code>name270</code> - The original image rotated 270 degrees clockwise
     * @param name The alias of the image
     * @param path The file from which to load the image
     * @throws IOException if an error occurs loading the image
     * @see #getImage(java.lang.String) 
     * @see #loadImage(java.lang.String, java.lang.String)  
     * @see #putImage(java.lang.String, java.awt.image.BufferedImage) 
     */
    public static void loadImages(String name, String path) throws IOException {
        BufferedImage img = ImageIO.read(new File(path));

        images.put(name, img);
        images.put(name + "0", img);
        img = Utils.rotateClockwise90(img);
        images.put(name + "90", img);
        img = Utils.rotateClockwise90(img);
        images.put(name + "180", img);
        img = Utils.rotateClockwise90(img);
        images.put(name + "270", img);
    }

    /**
     * Maps the given alias to reference the given image
     * @param name The alias of the image
     * @param img The image to associate with the given alias
     * @see #getImage(java.lang.String) 
     * @see #loadImage(java.lang.String, java.lang.String) 
     * @see #loadImages(java.lang.String, java.lang.String) 
     */
    public static void putImage(String name, BufferedImage img) {
        images.put(name, img);
    }

    /**
     * Retrieves the image which corresponds to the given alias
     * @param name The alias of the image to search for
     * @return The image which corresponds to the given alias or <code>null</code> if none is found
     * @see #loadImage(java.lang.String, java.lang.String) 
     * @see #loadImages(java.lang.String, java.lang.String) 
     * @see #putImage(java.lang.String, java.awt.image.BufferedImage) 
     */
    public static BufferedImage getImage(String name) {
        return images.get(name);
    }

    /**
     * Creates a copy of the given components and Lines and centers them at the specified position. This method only copies Lines which exist between copied components
     * @param iComps The components to copy
     * @param iLines The Lines to copy
     * @param newCenter The position in component coordinate space around which to center the new components
     * @param outComps A Collection which should receive the copied components
     * @param outLines A Collection which should receive the copied Lines
     * @param parent The component to center the error dialog on (should an error occur). This can be <code>null</code>.
     */
    public static void addAsNew(ArrayList<EditorComponent> iComps, ArrayList<Line> iLines, Point2D.Double newCenter, Collection<EditorComponent> outComps, Collection<Line> outLines, Component parent) {
        if(iComps.isEmpty()) {
            // If there are no input components, nothing needs to be done
            return;
        }

        // Copy all componets and save their original id
        Map<EditorComponent, Long> newComps = iComps.stream().collect(Collectors.toMap(EditorComponent::safeCopy, EditorComponent::getId));

        if(Utils.checkErrors() != null) {
            // If an error occured, warn the user and do nothing
            // The copied components will be garbage-collected
            Utils.clearErrors();
            JOptionPane.showMessageDialog(parent, "An error occured importing the selected components", "Import Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Move the new components to the center point
        {
            // Calculate the bounding box containing the components
            EditorComponent comp0 = iComps.get(0); 
            Point2D.Double tlc = new Point2D.Double();
            tlc.setLocation(comp0.getPosition());
            Point2D.Double brc = new Point2D.Double();
            tlc.setLocation(comp0.getPosition());
            brc.setLocation(brc.x+comp0.getSize().width, brc.y+comp0.getSize().height);
            for(EditorComponent comp: iComps) {
                Point2D.Double compPos = new Point2D.Double();
                compPos.setLocation(comp.getPosition());
                if(compPos.x < tlc.x) {
                    tlc.setLocation(compPos.x, tlc.y);
                }
                if(compPos.y < tlc.y) {
                    tlc.setLocation(tlc.x, compPos.y);
                }
                compPos.setLocation(compPos.x+comp.getSize().width, compPos.y+comp.getSize().height);
                if(compPos.x > brc.x) {
                    brc.setLocation(compPos.x, brc.y);
                }
                if(compPos.y > brc.y) {
                    brc.setLocation(brc.x, compPos.y);
                }
            }

            // new_top_left_corner = newCenter - bounds / 2
            // new_pos = old_pos - original_top_left_corner + new_top_left_corner = old_pos - original_top_left_corner + newCenter - bounds / 2
            double width = brc.x - tlc.x;
            double height = brc.y - tlc.y;
            newCenter.setLocation(newCenter.x-width/2, newCenter.y-height/2);
            newCenter.setLocation(newCenter.x-tlc.x, newCenter.y-tlc.y);
            newComps.keySet().forEach(comp -> comp.setPosition(comp.getPosition().x+newCenter.x, comp.getPosition().y+newCenter.y));
        }

        // Map old ids to new ids
        Map<Long, Long> idMap = newComps.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, e -> e.getKey().getId()));

        // Ignore any lines which did not have both of their components copied
        iLines.removeIf(line -> !idMap.keySet().contains(line.compId1) || !idMap.keySet().contains(line.compId2));

        // Copy the lines and remap their component ids
        List<Line> newLines = iLines.stream().map(line -> new Line(idMap.get(line.compId1),
                line.portId1, idMap.get(line.compId2), line.portId2, line.isHoris, line.movPer)).collect(Collectors.toList());

        // Reserve ports
        newLines.forEach(ResourceManager::reservePorts);

        // Save the copied components and lines
        outComps.addAll(newComps.keySet());
        outLines.addAll(newLines);
    }

    /**
     * Deletes any existing components and Lines and registers the given components and Lines
     * @param comps The new components to register
     * @param lines The new Lines to register
     */
    public static void forceLoad(ArrayList<EditorComponent> comps, ArrayList<Line> lines) {
        // Lock the component and port locks
        compidLock.writeLock().lock();
        try {
            portLock.writeLock().lock();
            try {
                // Update the state key so that old components being cleared won't affect the new component lists
                stateKey = new Object();

                // Clear the currently loaded components and ports
                compids.clear();
                ports.clear();

                // Add the new components
                comps.forEach(comp -> compids.put(comp.getId(), new WeakReference<>(comp)));

                // Reserve ports
                lines.forEach(ResourceManager::reservePorts);

                // Unlock locks
            } finally {
                portLock.writeLock().unlock();
            }
        } finally {
            compidLock.writeLock().unlock();
        }
    }

    /**
     * Allocates a unique id for the given component
     * @param comp The component for which an id should be allocated
     * @return The id allocated to the component
     * @see #getComponent(long) 
     */
    public static long allocId(BaseComponent<?> comp) {
        // Lock the component lock
        compidLock.writeLock().lock();
        try {
            // Search for an id
            for(long i = 0; i < Long.MAX_VALUE; i++) {
                // If the id <i> is not in use
                if(!compids.containsKey(i)) {
                    // Map the component to id <i>
                    compids.put(i, new WeakReference<>(comp));

                    // Register a DeallocIdRunner to check for when the component is garbage-collected
                    cleaner.register(comp, new DeallocIdRunner(i, stateKey));

                    // Return the id
                    return i;
                }
            }

            // All valid 2^31 ids are in use, throw an error
            throw new IndexOutOfBoundsException("No valid id found");

            // Unlock locks
        } finally {
            compidLock.writeLock().unlock();
        }
    }

    /**
     * Retrieves the component with the specified id
     * @param id The id for which to search
     * @return The component with the specified id or <code>null</code> if none exists
     * @see #allocId(com.coolspy3.hde.component.BaseComponent)  
     */
    public static BaseComponent<?> getComponent(long id) {
        // Lock the component lock
        compidLock.readLock().lock();
        try {
            // Find the component
            return compids.containsKey(id) ? compids.get(id).get() : null;

            // Unlock locks
        } finally {
            compidLock.readLock().unlock();
        }
    }

    /**
     * Attempts to reserve all of the ports used by the specified Line
     * @param line The Line which should have its ports reserved
     * @throws IllegalArgumentException if one or more of the ports used by this line is already reserved
     * @see #reservePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePort(long, java.lang.String) 
     * @see #freePorts(code.Line) 
     * @see #freePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #freePort(long, java.lang.String) 
     * @see #portStatus(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #portStatus(long, java.lang.String) 
     */
    public static void reservePorts(Line line) throws IllegalArgumentException {
        reservePort(line.compId1, line.portId1);
        reservePort(line.compId2, line.portId2);
    }

    /**
     * Attempts to reserve the given port
     * @param comp The component on which the port is located
     * @param port The id of the port to be reserved
     * @throws IllegalArgumentException if the port is already reserved
     * @see #reservePorts(code.Line)  
     * @see #reservePort(long, java.lang.String) 
     * @see #freePorts(code.Line) 
     * @see #freePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #freePort(long, java.lang.String) 
     * @see #portStatus(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #portStatus(long, java.lang.String) 
     */
    public static void reservePort(BaseComponent<?> comp, String port) throws IllegalArgumentException {
        reservePort(comp.getId(), port);
    }

    /**
     * Attempts to reserve the given port
     * @param comp The id of the component on which the port is located
     * @param port The id of the port to be reserved
     * @throws IllegalArgumentException if the port is already reserved
     * @see #reservePorts(code.Line) 
     * @see #reservePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #freePorts(code.Line) 
     * @see #freePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #freePort(long, java.lang.String) 
     * @see #portStatus(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #portStatus(long, java.lang.String) 
     */
    public static void reservePort(long comp, String port) throws IllegalArgumentException {
        // Lock the port lock
        portLock.writeLock().lock();
        try {
            if(ports.containsKey(comp)) {
                if(ports.get(comp).contains(port)) {
                    // The port is already in use. Throw an exception
                    throw new IllegalArgumentException("Port in use: " + port + " on device: " + comp);
                }
            } else {
                // There is not an ArrayList for tracking the ports on this component. Create one
                ports.put(comp, new ArrayList<>());
            }

            // Reserve the port
            ports.get(comp).add(port);

            // Unlock locks
        } finally {
            portLock.writeLock().unlock();
        }
    }

    /**
     * Attempts to free the ports used by the given Line
     * @param line The Line which should have its ports freed
     * @see #freePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #freePort(long, java.lang.String) 
     * @see #reservePorts(code.Line) 
     * @see #reservePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePort(long, java.lang.String) 
     * @see #portStatus(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #portStatus(long, java.lang.String) 
     */
    public static void freePorts(Line line) {
        freePort(line.compId1, line.portId1);
        freePort(line.compId2, line.portId2);
    }

    /**
     * Attempts to free the specified port
     * @param comp The component on which the port is located
     * @param port The id of the port to be freed
     * @see #freePorts(code.Line) 
     * @see #freePort(long, java.lang.String) 
     * @see #reservePorts(code.Line) 
     * @see #reservePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePort(long, java.lang.String) 
     * @see #portStatus(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #portStatus(long, java.lang.String) 
     */
    public static void freePort(BaseComponent<?> comp, String port) {
        freePort(comp.getId(), port);
    }

    /**
     * Attempts to free the specified port
     * @param comp The id of the component on which the port is located
     * @param port The id of the port to be freed
     * @see #freePorts(code.Line) 
     * @see #freePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePorts(code.Line) 
     * @see #reservePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePort(long, java.lang.String) 
     * @see #portStatus(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #portStatus(long, java.lang.String) 
     */
    public static void freePort(long comp, String port) {
        // Lock the port lock
        portLock.writeLock().lock();
        try {
            if(ports.containsKey(comp)) {
                // If there is an ArrayList tracking the ports on this component, attempt to free the requested port
                ports.get(comp).remove(port);
            }

            // Unlock locks
        } finally {
            portLock.writeLock().unlock();
        }
    }

    /**
     * Checks whether the given port is reserved
     * @param comp The component on which the port is located
     * @param port The id of the port to check
     * @return Whether the given port is reserved
     * @see #portStatus(long, java.lang.String) 
     * @see #reservePorts(code.Line)
     * @see #reservePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePort(long, java.lang.String) 
     * @see #freePorts(code.Line) 
     * @see #freePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #freePort(long, java.lang.String) 
     */
    public static boolean portStatus(BaseComponent<?> comp, String port) {
        return portStatus(comp.getId(), port);
    }

    /**
     * Checks whether the given port is reserved
     * @param comp The id of the component on which the port is located
     * @param port The id of the port to check
     * @return Whether the given port is reserved
     * @see #portStatus(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePorts(code.Line) 
     * @see #reservePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #reservePort(long, java.lang.String) 
     * @see #freePorts(code.Line) 
     * @see #freePort(com.coolspy3.hde.component.BaseComponent, java.lang.String) 
     * @see #freePort(long, java.lang.String) 
     */
    public static boolean portStatus(long comp, String port) {
        // Lock the port lock
        portLock.readLock().lock();
        try {
            if(ports.containsKey(comp)) {
                // If there is an ArrayList tracking the ports on this component, check the requested port
                return ports.get(comp).contains(port);
            }

            // No ports on this component have ever reserved. Return false
            return false;

            // Unlock locks
        } finally {
            portLock.readLock().unlock();
        }
    }

    private static void doDeallocId(long id, Object stateKey) {
        if(stateKey != ResourceManager.stateKey) {
            // The stateKey has changed. The requested component has already been freed
            return;
        }

        // Lock the component lock
        compidLock.writeLock().lock();
        try {
            // Remove the component
            compids.remove(id);

            // Lock the port lock
            portLock.writeLock().lock();
            try {
                // Remove the ArrayList which is tracking ports for this component if it exists
                ports.remove(id);

                // Unlock locks
            } finally {
                portLock.writeLock().unlock();
            }
        } finally {
            compidLock.writeLock().unlock();
        }
    }

    private static class DeallocIdRunner implements Runnable {
        private final long id;
        private final Object stateKey;
        DeallocIdRunner(long id, Object stateKey) {
            this.id = id;
            this.stateKey = stateKey;
        }
        @Override
        public void run() {
            // The object we are tracking can be garbage-collected. Deallocate its id
            ResourceManager.doDeallocId(id, stateKey);
        }
    }

    private ResourceManager() {
    }

}
