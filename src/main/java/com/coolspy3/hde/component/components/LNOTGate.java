package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.I1O1LogicComponent;
import com.coolspy3.hde.component.LogicComponent;
import java.util.Collection;

/**
 * Represents a NOT gate logic component
 */
public class LNOTGate extends I1O1LogicComponent {

    private static final long serialVersionUID = -6769111536184014829L;

    /**
     * Creates a new LNOTGate at the given position
     * @param connectedComponents A Collection containing Pairs mapping port ids on this component to
     * Pairs representing components connected to that component as well as the connected port on that component t
     */
    public LNOTGate(Collection<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("I");
        createPort("O");
    }

    @Override
    public boolean getNextState(boolean i) {
        return !i;
    }

}
