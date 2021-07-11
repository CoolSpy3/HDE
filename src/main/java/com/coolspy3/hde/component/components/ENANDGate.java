package com.coolspy3.hde.component.components;

import com.coolspy3.hde.component.EditorComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Represents a NAND gate which can be displayed in the editor
 */
public class ENANDGate extends EditorComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    /**
     * Creates a new ENANDGate at the given position
     * @param pos The position of the top left corner of the component
     */
    public ENANDGate(Point2D.Double pos) {
        super("DNANDGate", new Dimension(100, 50), pos);
        putLeft("I1", 10);
        putLeft("I2", 40);
        putRight("O", 24);
    }

}
