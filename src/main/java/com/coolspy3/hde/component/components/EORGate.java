package com.coolspy3.hde.component.components;

import com.coolspy3.hde.component.EditorComponent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Represents an OR gate logic component
 */
public class EORGate extends EditorComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    /**
     * Represents an DORGate gate logic component
     * @param pos The top left corner of the component
     */
    public EORGate(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Represents an DORGate gate logic component
     * @param pos The top left corner of the component
     */
    public EORGate(Point2D.Double pos) {
        super("DORGate", new Dimension(100, 50), pos);
        putLeft("I1", 10);
        putLeft("I2", 40);
        putRight("O", 24);
    }

}
