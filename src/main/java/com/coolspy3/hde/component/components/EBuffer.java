package com.coolspy3.hde.component.components;

import com.coolspy3.hde.component.EditorComponent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Represents a buffer logic component
 */
public class EBuffer extends EditorComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    /**
     * Creates a new DBuffer at the given position
     * @param pos The top left corner of the buffer
     */
    public EBuffer(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Creates a new DBuffer at the given position
     * @param pos The top left corner of the buffer
     */
    public EBuffer(Point2D.Double pos) {
        super("Buffer", new Dimension(100, 50), pos);
        putLeft("I", 24);
        putRight("O", 24);
    }

}
