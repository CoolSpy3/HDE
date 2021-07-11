package com.coolspy3.hde.component.components;

import com.coolspy3.hde.Pair;
import com.coolspy3.hde.component.I1O1LogicComponent;
import com.coolspy3.hde.component.LogicComponent;
import java.util.ArrayList;

/**
 * Represents a NOT gate logic component
 */
public class LNOTGate extends I1O1LogicComponent {

    private static final long serialVersionUID = -6769111536184014829L;

    /**
     * Creates a new DNOTGate at the given position
     * @param pos The top left corner of the component
     */
    public LNOTGate(ArrayList<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
        createPort("I");
        createPort("O");
    }

    @Override
    public boolean getNextState(boolean i) {
        return !i;
    }

}
