package com.coolspy3.hde.component.components;

import com.coolspy3.hde.component.EditorComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Represents an AND gate which can be displayed in the editor
 */
public class EANDGate extends EditorComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    /**
     * Creates a new EANDGate at the given position
     * @param pos The position of the top left corner of the component
     */
    public EANDGate(Point2D.Double pos) {
        super("DANDGate", new Dimension(100, 50), pos);
        putLeft("I1", 10);
        putLeft("I2", 40);
        putRight("O", 24);
    }

}
