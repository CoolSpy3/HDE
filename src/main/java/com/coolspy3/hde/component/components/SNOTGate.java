package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a NOT gate logic component
 */
public class SNOTGate extends SimulatedComponent {

    private static final long serialVersionUID = 6745696924455287109L;

    static {
        try {
            ResourceManager.loadImages("DNOTGate", "Assets/NOTGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new DNOTGate at the given position
     * @param pos The top left corner of the component
     */
    public SNOTGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DNOTGate", new Dimension(100, 50), pos, rotation, component);
    }

}
