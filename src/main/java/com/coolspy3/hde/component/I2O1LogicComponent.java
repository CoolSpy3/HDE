package com.coolspy3.hde.component;

import com.coolspy3.hde.Pair;
import java.util.Collection;

/**
 * Represents a LogicComponent with two inputs and one output named "I1", "I2", and "O" respectively
 */
public abstract class I2O1LogicComponent extends LogicComponent {

    private static final long serialVersionUID = -8523870167606599713L;
    /**
     * The state of this component's output on the next call to {@link #pushState()}
     */
    protected boolean nextState;

    /**
     * Creates a new I2O1LogicComponent
     * @param connectedComponents A Collection containing Pairs mapping port ids on this component to
     * Pairs representing components connected to that component as well as the connected port on that component 
     */
    public I2O1LogicComponent(Collection<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        super(connectedComponents);
    }

    /**
     * Calculates the state of the output of this component after the next call to {@link #pushState()}
     * @param i1 The current state of the first input of this component ("I1")
     * @param i2 The current state of the second input of this component ("I2")
     * @return The requested state of the output of this component after the next call to {@link #pushState()}
     */
    public abstract boolean getNextState(boolean i1, boolean i2);

    @Override
    public void queueState() {
        nextState = getNextState(getPortState("I1"), getPortState("I2"));
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
