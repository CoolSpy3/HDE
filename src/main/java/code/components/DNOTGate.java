package code.components;

import code.DComponent;
import code.ResourceManager;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a NOT gate logic component
 */
public class DNOTGate extends DComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    static {
        try {
            ResourceManager.loadImages("DNOTGate", "Assets/NOTGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new DNOTGate at the given position
     * @param pos The top left corner of the component
     */
    public DNOTGate(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Creates a new DNOTGate at the given position
     * @param pos The top left corner of the component
     */
    public DNOTGate(Point2D.Double pos) {
        super("DNOTGate", new Dimension(100, 50), pos);
        linputs.put("I", 24);
        rinputs.put("O", 24);
    }

}
