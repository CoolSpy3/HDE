package code.components;

import code.DComponent;
import code.ResourceManager;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a buffer logic component
 */
public class DBuffer extends DComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    static {
        try {
            ResourceManager.loadImages("Buffer", "Assets/Buffer.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new DBuffer at the given position
     * @param pos The top left corner of the buffer
     */
    public DBuffer(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Creates a new DBuffer at the given position
     * @param pos The top left corner of the buffer
     */
    public DBuffer(Point2D.Double pos) {
        super("Buffer", new Dimension(100, 50), pos);
        linputs.put("I", 24);
        rinputs.put("O", 24);
    }

}
