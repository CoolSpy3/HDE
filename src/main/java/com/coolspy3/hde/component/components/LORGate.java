package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.I2O1LogicComponent;
import com.coolspy3.hde.component.LogicComponent;
import java.util.Collection;

/**
 * Represents an OR gate logic component
 */
public class LORGate extends I2O1LogicComponent {

    private static final long serialVersionUID = 9116449861443196463L;

    /**
     * Represents an LORGate gate logic component
     * @param connectedComponents A Collection containing Pairs mapping port ids on this component to
     * Pairs representing components connected to that component as well as the connected port on that component 
     */
    public LORGate(Collection<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("I1");
        createPort("I2");
        createPort("O");
    }

    @Override
    public boolean getNextState(boolean i1, boolean i2) {
        return i1 || i2;
    }

}
