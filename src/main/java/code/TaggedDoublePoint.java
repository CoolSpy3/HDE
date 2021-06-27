package code;

import java.awt.geom.Point2D;

/**
 * Represents a Point2D.Double with a String tag
 * @see TaggedPoint
 */
public class TaggedDoublePoint extends Point2D.Double {

    private static final long serialVersionUID = 8858277194439943011L;

    /**
     * The data associated with this point
     */
    public final String tag;

    /**
     * Constructs and initializes a Point2D with
     * coordinates (0,&nbsp;0) and a tag of <code>null</code>.
     */
    public TaggedDoublePoint() {
        this(0, 0, null);
    }

    /**
     * Constructs and initializes a Point2D with
     * coordinates of the given point and sets the tag to <code>null</code>.
     * 
     * @param p The point from which to copy the coordinates
     */
    public TaggedDoublePoint(Point2D.Double p) {
        this(p, null);
    }

    /**
     * Constructs and initializes a Point2D with the
     * specified coordinates and sets the tag to <code>null</code>.
     *
     * @param x The X coordinate of the newly
     *          constructed Point2D
     * @param y The Y coordinate of the newly
     *          constructed Point2D
     */
    public TaggedDoublePoint(double x, double y) {
        this(x, y, null);
    }

    /**
     * Constructs and initializes a Point2D with
     * coordinates of the given point.
     * 
     * @param p The point from which to copy the coordinates
     * @param tag The data to associate with this point
     */
    public TaggedDoublePoint(Point2D.Double p, String tag) {
        this(p.x, p.y, null);
    }

    /**
     * Constructs and initializes a Point2D with the
     * specified coordinates.
     *
     * @param x The X coordinate of the newly
     *          constructed Point2D
     * @param y The Y coordinate of the newly
     *          constructed Point2D
     * @param tag The data to associate with this point
     */
    public TaggedDoublePoint(double x, double y, String tag) {
        super(x, y);
        this.tag = tag;
    }

}
