package com.coolspy3.hde;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

/**
 * Contains utility functions
 */
public final class Utils {

    private static final ThreadLocal<ArrayDeque<Throwable>> errors = ThreadLocal.withInitial(ArrayDeque::new);

    /**
     * Casts the given double to an integer
     * @param d The double to cast
     * @return The resulting integer
     */
    public static int asInt(double d) {
        return (int)d;
    }

    /**
     * Creates a point with the coordinates of the given point casted to integers
     * @param d The point to convert
     * @return A new point representing the location of the given point with the coordinates of the given point casted to integers
     */
    public static Point asInt(Point2D.Double d) {
        return new Point(asInt(d.x), asInt(d.y));
    }

    /**
     * Creates a Point2D.Double with the coordinates of the given point
     * @param d The point to convert
     * @return A new Point2D.Double with the coordinates of the given point
     */
    public static Point2D.Double asDouble(Point d) {
        return new Point2D.Double(d.x, d.y);
    }

    /**
     * Check the error queue to see if any errors have been logged
     * @return The most recent error or <code>null</code> if none have occurred
     * @see #clearErrors() 
     * @see #_throw(java.lang.Throwable) 
     */
    public static Throwable checkErrors() {
        return errors.get().poll();
    }

    /**
     * Clears the error queue
     * @see #checkErrors() 
     * @see #_throw(java.lang.Throwable) 
     */
    public static void clearErrors() {
        errors.get().clear();
    }

    /**
     * Draws the Line defined by the given parameters
     * @param g The Graphics context onto which to draw
     * @param p1 The start position of the line
     * @param p2 The end position of the line
     * @param isHoris Whether the line initially moves in the horizontal direction
     * @param movPer The percentage of the line between the start position and the first bend in the Line
     */
    public static void drawLine(Graphics g, Point p1, Point p2, boolean isHoris, double movPer) {
        drawLine(g, p1.x, p1.y, p2.x, p2.y, isHoris, movPer);
    }

    /**
     * Draws the Line defined by the given parameters
     * @param g The Graphics context onto which to draw
     * @param x1 The x-coordinate of the start position of the line
     * @param y1 The y-coordinate of the start position of the line
     * @param x2 The x-coordinate of the end position of the line
     * @param y2 The y-coordinate of the end position of the line
     * @param isHoris Whether the line initially moves in the horizontal direction
     * @param movPer The percentage of the line between the start position and the first bend in the Line
     */
    public static void drawLine(Graphics g, int x1, int y1, int x2, int y2, boolean isHoris, double movPer) {
        if(isHoris) {
            double lineLength = movPer * (x2-x1);
            g.drawLine(x1, y1, (int)(x1+lineLength), y1);
            g.drawLine((int)(x1+lineLength), y1, (int)(x1+lineLength), y2);
            g.drawLine((int)(x1+lineLength), y2, x2, y2);
        } else {
            double lineLength = movPer * (y2-y1);
            g.drawLine(x1, y1, x1, (int)(y1+lineLength));
            g.drawLine(x1, (int)(y1+lineLength), x2, (int)(y1+lineLength));
            g.drawLine(x2, (int)(y1+lineLength), x2, y2);
        }
    }

    /**
     * Constructs a rectangle from its top left and bottom right corners
     * @param topLeft The top left corner of the rectangle
     * @param bottomRight The bottom right corner f the rectangle
     * @return The resulting rectangle
     */
    public static Rectangle rectangleFromCorners(Point topLeft, Point bottomRight) {
        return new Rectangle(topLeft, new Dimension(bottomRight.x-topLeft.x, bottomRight.y-topLeft.y));
    }

    /**
     * Constructs a rectangle from two of its opposite corners
     * @param c1 One corner of the rectangle
     * @param c2 The opposite corner of the rectangle
     * @return The resulting rectangle
     */
    public static Rectangle rectangleFromAnyCorners(Point c1, Point c2) {
        return rectangleFromCorners(new Point(Math.min(c1.x, c2.x), Math.min(c1.y, c2.y)), new Point(Math.max(c1.x, c2.x), Math.max(c1.y, c2.y)));
    }

    //Source: https://stackoverflow.com/questions/20959796/rotate-90-degree-to-right-image-in-java
    /**
     * Rotates the given image 90 degrees clockwise
     * @param src The image to rotate
     * @return The rotated image
     */
    public static BufferedImage rotateClockwise90(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest = new BufferedImage(height, width, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }

    /**
     * Adds the given error to the error queue
     * @param t The error to add
     * @see #checkErrors() 
     * @see #clearErrors() 
     */
    public static void _throw(Throwable t) {
        if(t != null) {
            errors.get().addFirst(t);
        }
    }

    private Utils() {}

}
