package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a XNOR gate logic component
 */
public class SXNORGate extends SimulatedComponent {

    private static final long serialVersionUID = -2830584833025663074L;

    static {
        try {
            ResourceManager.loadImages("DXNORGate", "Assets/XNORGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new DXNORGate at the given position
     * @param pos The top left corner of the component
     */
    public SXNORGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DXNORGate", new Dimension(100, 50), pos, rotation, component);
    }

}
