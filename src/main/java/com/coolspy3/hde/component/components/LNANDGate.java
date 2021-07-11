package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.I2O1LogicComponent;
import com.coolspy3.hde.component.LogicComponent;
import java.util.Collection;

/**
 * Represents a NAND gate logic component
 */
public class LNANDGate extends I2O1LogicComponent {

    private static final long serialVersionUID = -4392448629555895912L;

    /**
     * Creates a new LNANDGate at the given position
     * @param connectedComponents A Collection containing Pairs mapping port ids on this component to
     * Pairs representing components connected to that component as well as the connected port on that component 
     */
    public LNANDGate(Collection<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("I1");
        createPort("I2");
        createPort("O");
    }

    @Override
    public boolean getNextState(boolean i1, boolean i2) {
        return !(i1 && i2);
    }

}
