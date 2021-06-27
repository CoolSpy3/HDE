package code.components;

import code.DComponent;
import code.ResourceManager;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a XOR gate logic component
 */
public class DXORGate extends DComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    static {
        try {
            ResourceManager.loadImages("DXORGate", "Assets/XORGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new DXORGate at the given position
     * @param pos The top left corner of the component
     */
    public DXORGate(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Creates a new DXORGate at the given position
     * @param pos The top left corner of the component
     */
    public DXORGate(Point2D.Double pos) {
        super("DXORGate", new Dimension(100, 50), pos);
        linputs.put("I1", 10);
        linputs.put("I2", 40);
        rinputs.put("O", 24);
    }

}
