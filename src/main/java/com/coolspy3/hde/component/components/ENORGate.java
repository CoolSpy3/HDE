package com.coolspy3.hde.component.components;

import com.coolspy3.hde.component.EditorComponent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Represents a NOR gate logic component
 */
public class ENORGate extends EditorComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    /**
     * Creates a new DNORGate at the given position
     * @param pos The top left corner of the component
     */
    public ENORGate(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Creates a new DNORGate at the given position
     * @param pos The top left corner of the component
     */
    public ENORGate(Point2D.Double pos) {
        super("DNORGate", new Dimension(100, 50), pos);
        putLeft("I1", 10);
        putLeft("I2", 40);
        putRight("O", 24);
    }

}
