package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a buffer which can be displayed in the simulator
 */
public class SBuffer extends SimulatedComponent {

    private static final long serialVersionUID = 4118267798345265357L;

    static {
        try {
            ResourceManager.loadImages("Buffer", "Assets/Buffer.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new SBuffer at the given position
     * @param pos The top left corner of the buffer
     * @param rotation The rotation of this component as a number of increments of 90 degrees clockwise between 0 and 3
     * @param component The LogicComponent that this SimulatedComponent is rendering
     */
    public SBuffer(Point2D.Double pos, int rotation, LogicComponent component) {
        super("Buffer", new Dimension(100, 50), pos, rotation, component);
    }

}
