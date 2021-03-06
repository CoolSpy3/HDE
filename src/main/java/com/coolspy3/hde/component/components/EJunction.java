package com.coolspy3.hde.component.components;

import com.coolspy3.hde.component.EditorComponent;
import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Represents a junction which can be displayed in the editor
 */
public class EJunction extends EditorComponent {

    private static final long serialVersionUID = -3396877843973306762L;

    /**
     * Creates a new EJunction at the given position
     * @param pos The position of the top left corner of the component
     */
    public EJunction(Point2D.Double pos) {
        super("Junction", new Dimension(20, 20), pos);
        putBottom("P1", 9);
        putLeft("P2", 9);
        putRight("P3", 9);
        putTop("P4", 9);
    }

}
