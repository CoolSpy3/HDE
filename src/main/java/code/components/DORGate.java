package code.components;

import code.DComponent;
import code.ResourceManager;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents an OR gate logic component
 */
public class DORGate extends DComponent {

    private static final long serialVersionUID = -2448682944040958907L;

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
    public DORGate(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }
    
    /**
     * Represents an DORGate gate logic component
     * @param pos The top left corner of the component
     */
    public DORGate(Point2D.Double pos) {
        super("DORGate", new Dimension(100, 50), pos);
        linputs.put("I1", 10);
        linputs.put("I2", 40);
        rinputs.put("O", 24);
    }
    
}