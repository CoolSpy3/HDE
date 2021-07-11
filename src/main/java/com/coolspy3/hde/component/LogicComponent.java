package com.coolspy3.hde.component;

import com.coolspy3.hde.Pair;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a component which handles a logic state and interacts with other components
 */
public abstract class LogicComponent extends BaseComponent<Pair<Boolean, ArrayList<Pair<LogicComponent, String>>>> {

    private static final long serialVersionUID = 2199705179257491866L;

    /**
     * Creates a new LogicComponent
     * @param connectedComponents A Collection containing Pairs mapping port ids on this component to
     * Pairs representing components connected to that component as well as the connected port on that component 
     */
    public LogicComponent(Collection<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        connectedComponents.forEach(comp -> {
            createPort(comp.getT());
            ports.get(comp.getT()).getU().add(comp.getU());
        });
    }

    /**
     * Creates an entry for the given port in this component's port map if it does not yet exist
     * @param portId The portId of the port to create
     */
    protected void createPort(String portId) {
        if(!ports.containsKey(portId)) {
            ports.put(portId, new Pair<>(false, new ArrayList<>()));
        }
    }

    /**
     * Reads the current state of the input(s) of this component which will be used to calculate the next state of this component's outputs
     * @see #pushState() 
     */
    public abstract void queueState();

    /**
     * Updates the outputs of this component in response to the state of its inputs as was retrieved by the previous call to {@link #queueState()}
     */
    public abstract void pushState();

    /**
     * @return Whether any of the outputs of this component is active
     */
    public abstract boolean isActive();

    /**
     * Retrieves the state of the given port of this LogicComponent
     * @param id The id of the port to check
     * @return Whether this component is outputting a signal on that port
     */
    @SuppressWarnings("null")
    public boolean getPortState(String id) {
        return ports.containsKey(id) ? ports.get(id).getT() : false;
    }

    /**
     * Retrieves the state of the given input port
     * @param id The id of the port to check
     * @return Whether any of the components connected to the given port are outputting a signal to that port
     */
    protected boolean getInputState(String id) {
        if(!ports.containsKey(id)) {
            return false;
        }
        return ports.get(id).getU().stream().map(comp -> comp.getT().getPortState(comp.getU())).anyMatch(Boolean::booleanValue);
    }

    /**
     * Sets the state of the given output port
     * @param id The id of the port to set
     * @param state The new state of the given port
     */
    protected void setPortState(String id, boolean state) {
        if(ports.containsKey(id)) {
            ports.get(id).setT(state);
        }
    }

}
