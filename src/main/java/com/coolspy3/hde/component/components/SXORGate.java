package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a XOR gate which can be displayed in the simulator
 */
public class SXORGate extends SimulatedComponent {

    private static final long serialVersionUID = -7794926008043804648L;

    static {
        try {
            ResourceManager.loadImages("DXORGate", "Assets/XORGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new SXORGate at the given position
     * @param pos The position of the top left corner of the component
     * @param rotation The rotation of this component as a number of increments of 90 degrees clockwise between 0 and 3
     * @param component The LogicComponent that this SimulatedComponent is rendering
     */
    public SXORGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DXORGate", new Dimension(100, 50), pos, rotation, component);
    }

}
