package com.coolspy3.hde.component.components;

import com.coolspy3.hde.component.EditorComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Represents a NOT gate which can be displayed in the editor
 */
public class ENOTGate extends EditorComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    /**
     * Creates a new ENOTGate at the given position
     * @param pos The position of the top left corner of the component
     */
    public ENOTGate(Point2D.Double pos) {
        super("DNOTGate", new Dimension(100, 50), pos);
        putLeft("I", 24);
        putRight("O", 24);
    }

}
