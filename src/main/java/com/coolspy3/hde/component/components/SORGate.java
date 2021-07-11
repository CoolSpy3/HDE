package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents an OR gate which can be displayed in the simulator
 */
public class SORGate extends SimulatedComponent {

    private static final long serialVersionUID = -7298136320618914714L;

    static {
        try {
            ResourceManager.loadImages("DORGate", "Assets/ORGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new SORGate at the given position
     * @param pos The position of the top left corner of the component
     * @param rotation The rotation of this component as a number of increments of 90 degrees clockwise between 0 and 3
     * @param component The LogicComponent that this SimulatedComponent is rendering
     */
    public SORGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DORGate", new Dimension(100, 50), pos, rotation, component);
    }

}
