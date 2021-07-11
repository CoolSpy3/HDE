package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.LogicComponent;
import java.util.ArrayList;

/**
 * Represents a EJunction component
 */
public class LJunction extends LogicComponent {

    private static final long serialVersionUID = 5863682778397147940L;

    /**
     * Creates a new Junction at the given position
     * @param pos The top left corner of the component
     */
    public LJunction(ArrayList<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
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
