package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.I2O1LogicComponent;
import com.coolspy3.hde.component.LogicComponent;
import java.util.ArrayList;

/**
 * Represents an AND gate logic component
 */
public class LANDGate extends I2O1LogicComponent {

    private static final long serialVersionUID = 4400313451368937084L;

    /**
     * Creates a new DANDGate at the given position
     * @param pos The top left corner of the component
     */
    public LANDGate(ArrayList<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("I1");
        createPort("I2");
        createPort("O");
    }

    @Override
    public boolean getNextState(boolean i1, boolean i2) {
        return i1 && i2;
    }

}
