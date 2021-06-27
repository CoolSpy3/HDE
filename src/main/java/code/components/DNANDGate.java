package code.components;

import code.DComponent;
import code.ResourceManager;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Represents a NAND gate logic component
 */
public class DNANDGate extends DComponent {

    private static final long serialVersionUID = -2448682944040958907L;

    static {
        try {
            ResourceManager.loadImages("DNANDGate", "Assets/NANDGate.png");
        } catch(IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Creates a new DNANDGate at the given position
     * @param pos The top left corner of the component
     */
    public DNANDGate(Point pos) {
        this(new Point2D.Double(pos.x, pos.y));
    }

    /**
     * Creates a new DNANDGate at the given position
     * @param pos The top left corner of the component
     */
    public DNANDGate(Point2D.Double pos) {
        super("DNANDGate", new Dimension(100, 50), pos);
        linputs.put("I1", 10);
        linputs.put("I2", 40);
        rinputs.put("O", 24);
    }

}
