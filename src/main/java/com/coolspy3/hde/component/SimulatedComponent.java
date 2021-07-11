package com.coolspy3.hde.component;

import com.coolspy3.hde.ResourceManager;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Point2D;

/**
 * Represents a displayed component in the simulator
 */
public class SimulatedComponent implements DisplayableComponent {

    private static final long serialVersionUID = -7592748544023442782L;

    private final String imageName;
    private final Dimension size;
    private final Point2D.Double pos;
    private final int rotation;
    private final LogicComponent logicComp;

    /**
     * Creates a new SimulatedComponent
     * @param imageName The alias of the image which should be used when rendering this component
     * @param size The size of this component in component coordinate space
     * @param pos The position of this component in component coordinate space
     * @param rotation The rotation of this component as a number of increments of 90 degrees clockwise between 0 and 3
     * @param logicComp The LogicComponent that this SimulatedComponent is rendering
     */
    public SimulatedComponent(String imageName, Dimension size, Point2D.Double pos, int rotation, LogicComponent logicComp) {
        this.imageName = imageName;
        this.size = size;
        this.pos = pos;
        this.rotation = rotation;
        this.logicComp = logicComp;
    }

    public LogicComponent getLogicComponent() {
        return logicComp;
    }

    @Override
    public int getRotation() {
        return rotation;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public Point2D.Double getPosition() {
        return pos;
    }

    @Override
    public Image getImage() {
        return ResourceManager.getImage(getImageName() + (getRotation() * 90));
    }

}
