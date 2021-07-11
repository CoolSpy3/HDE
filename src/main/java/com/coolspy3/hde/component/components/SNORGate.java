package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a NOR gate logic component
 */
public class SNORGate extends SimulatedComponent {

    private static final long serialVersionUID = 4157833764764319875L;

    static {
        try {
            ResourceManager.loadImages("DNORGate", "Assets/NORGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new DNORGate at the given position
     * @param pos The top left corner of the component
     */
    public SNORGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DNORGate", new Dimension(100, 50), pos, rotation, component);
    }

}
