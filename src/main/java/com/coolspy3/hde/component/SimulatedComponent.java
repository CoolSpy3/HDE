package com.coolspy3.hde.component;

import com.coolspy3.hde.ResourceManager;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Point2D;

public class SimulatedComponent implements DisplayableComponent {

    private static final long serialVersionUID = -7592748544023442782L;

    private final String imageName;
    private final Dimension size;
    private final Point2D.Double pos;
    private final int rotaion;
    private final LogicComponent logicComp;

    public SimulatedComponent(String imageName, Dimension size, Point2D.Double pos, int rotaion, LogicComponent logicComp) {
        this.imageName = imageName;
        this.size = size;
        this.pos = pos;
        this.rotaion = rotaion;
        this.logicComp = logicComp;
    }

    public LogicComponent getLogicComponent() {
        return logicComp;
    }

    @Override
    public int getRotation() {
        return rotaion;
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
