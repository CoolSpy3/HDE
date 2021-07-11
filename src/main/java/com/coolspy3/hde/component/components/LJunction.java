package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.LogicComponent;
import java.util.Collection;

/**
 * Represents a junction logic component
 */
public class LJunction extends LogicComponent {

    private static final long serialVersionUID = 5863682778397147940L;

    /**
     * Creates a new LJunction at the given position
     * @param connectedComponents A Collection containing Pairs mapping port ids on this component to
     * Pairs representing components connected to that component as well as the connected port on that component 
     */
    public LJunction(Collection<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("P");
    }

    @Override
    public void queueState() {}

    @Override
    public void pushState() {}

    @Override
    public boolean isActive() {
        return getInputState("P");
    }

}
