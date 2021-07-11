package com.coolspy3.hde.component;

import com.coolspy3.hde.ResourceManager;
import com.coolspy3.hde.Utils;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

public interface DisplayableComponent extends Serializable {

    /**
     * @return the current rotation of this component from its default position as a number of increments of 90 degrees clockwise
     */
    public int getRotation();

    public String getImageName();

    public Dimension getSize();

    public Point2D.Double getPosition();

    /**
     * @return A rectangle representing the space occupied by this component
     */
    public default Rectangle getBounds() {
        return new Rectangle(Utils.asInt(getPosition()), getSize());
    }

    /**
     * Retrieves the image which should be used to render this component.
     * This uses the {@link ResourceManager#getImage(java.lang.String)} function to search for an image with the id <code>imageName + (rotation * 90)</code>
     * @return The image which should be used to render this component with any required rotation applied
     * @see #getImageName()
     * @see #getRotation() 
     */
    public Image getImage();

}
