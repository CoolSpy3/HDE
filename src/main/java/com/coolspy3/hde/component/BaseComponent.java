package com.coolspy3.hde.component;

import com.coolspy3.hde.ResourceManager;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a component which contains an id and a set of ports
 * @param <T> The data type to associate with the ports on this component
 */
public class BaseComponent<T> implements Serializable {

    private static final long serialVersionUID = 8551699257836397079L;

    private final long id;
    /**
     * A Map mapping port ids on this component to a component-defined value
     */
    public final HashMap<String, T> ports;

    /**
     * Creates a new BaseComponent and allocates it an id
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public BaseComponent() {
        // Allocate an id for this component
        this.id = ResourceManager.allocId(this);

        // Initialize variables
        this.ports = new HashMap<>();
    }

    /**
     * @return The unique id of this component
     * @see ResourceManager#allocId(com.coolspy3.hde.component.BaseComponent) 
     */
    public long getId() {
        return id;
    }

}
