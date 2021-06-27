package code.components;

import code.DComponent;
import code.ResourceManager;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a Junction component
 */
public class Junction extends DComponent {

    private static final long serialVersionUID = -3396877843973306762L;

    static {
        try {
            ResourceManager.loadImages("Junction", "Assets/Junction.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new Junction at the given position
     * @param pos The top left corner of the component
     */
    public Junction(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Creates a new Junction at the given position
     * @param pos The top left corner of the component
     */
    public Junction(Point2D.Double pos) {
        super("Junction", new Dimension(20, 20), pos);
        binputs.put("P1", 9);
        linputs.put("P2", 9);
        rinputs.put("P3", 9);
        tinputs.put("P4", 9);
    }

}
