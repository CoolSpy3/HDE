package com.coolspy3.hde.component;

import com.coolspy3.hde.Pair;
import java.util.Collection;

/**
 * Represents a LogicComponent with one input and one output named "I" and "O" respectively
 */
public abstract class I1O1LogicComponent extends LogicComponent {

    private static final long serialVersionUID = -8523870167606599713L;
    /**
     * The state of this component's output on the next call to {@link #pushState()}
     */
    protected boolean nextState;

    /**
     * Creates a new I1O1LogicComponent
     * @param connectedComponents A Collection containing Pairs mapping port ids on this component to
     * Pairs representing components connected to that component as well as the connected port on that component 
     */
    public I1O1LogicComponent(Collection<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
    }

    /**
     * Calculates the state of the output of this component after the next call to {@link #pushState()}
     * @param i The current state of the input of this component
     * @return The requested state of the output of this component after the next call to {@link #pushState()}
     */
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
