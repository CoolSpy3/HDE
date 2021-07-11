package com.coolspy3.hde.component;

import com.coolspy3.hde.Pair;
import java.util.ArrayList;

public abstract class LogicComponent extends BaseComponent<Pair<Boolean, ArrayList<Pair<LogicComponent, String>>>> {

    private static final long serialVersionUID = 2199705179257491866L;

    public LogicComponent(ArrayList<Pair<String, Pair<LogicComponent, String>>> connectedComponents) {
        connectedComponents.forEach(comp -> {
            createPort(comp.getT());
            ports.get(comp.getT()).getU().add(comp.getU());
        });
    }

    protected void createPort(String portId) {
        if(!ports.containsKey(portId)) {
            ports.put(portId, new Pair<>(false, new ArrayList<>()));
        }
    }

    public abstract void queueState();

    public abstract void pushState();

    public abstract boolean isActive();

    @SuppressWarnings("null")
    public boolean getPortState(String id) {
        return ports.containsKey(id) ? ports.get(id).getT() : false;
    }

    protected boolean getInputState(String id) {
        if(!ports.containsKey(id)) {
            return false;
        }
        return ports.get(id).getU().stream().map(comp -> comp.getT().getPortState(comp.getU())).anyMatch(Boolean::booleanValue);
    }

    protected void setPortState(String id, boolean state) {
        if(ports.containsKey(id)) {
            ports.get(id).setT(state);
        }
    }

}
