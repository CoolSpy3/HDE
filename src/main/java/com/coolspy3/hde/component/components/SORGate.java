package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents an OR gate logic component
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
     * Represents an DORGate gate logic component
     * @param pos The top left corner of the component
     */
    public SORGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DORGate", new Dimension(100, 50), pos, rotation, component);
    }

}
