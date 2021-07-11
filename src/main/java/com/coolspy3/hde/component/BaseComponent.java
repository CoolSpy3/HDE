package com.coolspy3.hde.component;

import com.coolspy3.hde.ResourceManager;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a component in editing mode
 */
public class BaseComponent<T> implements Serializable {

    private static final long serialVersionUID = 8551699257836397079L;

    private final long id;
    /**
     * A Map mapping port ids on the left side of this component to their distances from the top left corner of this component
     */
    public final HashMap<String, T> ports;

    /**
     * Creates a new DComponent with the given information
     * @param imageName The alias of the image which should be used when rendering this component
     * @param size The size of this component in component coordinate space
     * @param pos The position of this component in component coordinate space
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
     * @see ResourceManager#allocId(code.DComponent) 
     */
    public long getId() {
        return id;
    }

}
