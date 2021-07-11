package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a XOR gate logic component
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
     * Creates a new DXORGate at the given position
     * @param pos The top left corner of the component
     */
    public SXORGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DXORGate", new Dimension(100, 50), pos, rotation, component);
    }

}
