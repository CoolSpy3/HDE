package code;

import java.awt.Point;

/**
 * Represents a Point with a String tag
 * @see TaggedDoublePoint
 */
public class TaggedPoint extends Point {

    private static final long serialVersionUID = 8858277194439943011L;
    
    /**
     * The data associated with this point
     */
    public final String tag;

    /**
     * Constructs and initializes a Point with
     * coordinates (0,&nbsp;0) and a tag of <code>null</code>.
     */
    public TaggedPoint() {
        this(0, 0, null);
    }

    /**
     * Constructs and initializes a Point with
     * coordinates of the given point and sets the tag to <code>null</code>.
     * 
     * @param p The point from which to copy the coordinates
     */
    public TaggedPoint(Point p) {
        this(p, null);
    }

    /**
     * Constructs and initializes a Point with the
     * specified coordinates and sets the tag to <code>null</code>.
     *
     * @param x The X coordinate of the newly
     *          constructed Point
     * @param y The Y coordinate of the newly
     *          constructed Point
     */
    public TaggedPoint(int x, int y) {
        this(x, y, null);
    }

    /**
     * Constructs and initializes a Point with
     * coordinates of the given point.
     * 
     * @param p The point from which to copy the coordinates
     * @param tag The data to associate with this point
     */
    public TaggedPoint(Point p, String tag) {
        this(p.x, p.y, null);
    }

    /**
     * Constructs and initializes a Point with the
     * specified coordinates.
     *
     * @param x The X coordinate of the newly
     *          constructed Point
     * @param y The Y coordinate of the newly
     *          constructed Point
     * @param tag The data to associate with this point
     */
    public TaggedPoint(int x, int y, String tag) {
        super(x, y);
        this.tag = tag;
    }
    
}
