package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.I2O1LogicComponent;
import com.coolspy3.hde.component.LogicComponent;
import java.util.ArrayList;

/**
 * Represents a NOR gate logic component
 */
public class LNORGate extends I2O1LogicComponent {

    private static final long serialVersionUID = -8603875797697029282L;

    /**
     * Creates a new DNORGate at the given position
     * @param pos The top left corner of the component
     */
    public LNORGate(ArrayList<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("I1");
        createPort("I2");
        createPort("O");
    }

    @Override
    public boolean getNextState(boolean i1, boolean i2) {
        return !(i1 || i2);
    }

}
