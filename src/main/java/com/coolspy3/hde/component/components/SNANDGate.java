package com.coolspy3.hde.component.components;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.component.LogicComponent;
import com.coolspy3.hde.component.SimulatedComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a NAND gate which can be displayed in the simulator
 */
public class SNANDGate extends SimulatedComponent {

    private static final long serialVersionUID = 6205571129343177432L;

    static {
        try {
            ResourceManager.loadImages("DNANDGate", "Assets/NANDGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new SNANDGate at the given position
     * @param pos The position of the top left corner of the component
     * @param rotation The rotation of this component as a number of increments of 90 degrees clockwise between 0 and 3
     * @param component The LogicComponent that this SimulatedComponent is rendering
     */
    public SNANDGate(Point2D.Double pos, int rotation, LogicComponent component) {
        super("DNANDGate", new Dimension(100, 50), pos, rotation, component);
    }

}
