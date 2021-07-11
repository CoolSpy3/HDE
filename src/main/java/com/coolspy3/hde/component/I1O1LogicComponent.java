package com.coolspy3.hde.component;

import com.coolspy3.hde.Pair;
import java.util.ArrayList;

public abstract class I1O1LogicComponent extends LogicComponent {

    private static final long serialVersionUID = -8523870167606599713L;
    protected boolean nextState;

    public I1O1LogicComponent(ArrayList<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
    }

    public abstract boolean getNextState(boolean i);

    @Override
    public void queueState() {
        nextState = getNextState(getPortState("I1"));
    }

    @Override
    public void pushState() {
        setPortState("O", nextState);
    }

    @Override
    public boolean isActive() {
        return getPortState("O");
    }

}
