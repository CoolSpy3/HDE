package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.I1O1LogicComponent;
import com.coolspy3.hde.component.LogicComponent;
import java.util.ArrayList;

/**
 * Represents a buffer logic component
 */
public class LBuffer extends I1O1LogicComponent {

    private static final long serialVersionUID = -3931048646112692087L;

    /**
     * Creates a new DBuffer at the given position
     * @param pos The top left corner of the buffer
     */
    public LBuffer(ArrayList<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("I");
        createPort("O");
    }

    @Override
    public boolean getNextState(boolean i) {
        return i;
    }

}
