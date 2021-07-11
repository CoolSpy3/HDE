package com.coolspy3.hde;

/**
 * Represents a group of two objects
 * @param <T> The type of the first object associated with this Pair
 * @param <U> The type of the second object associated with this Pair
 */
public class Pair<T, U> {

    private T t;
    private U u;

    /**
     * Creates a new Pair
     * @param t The first object associated with this Pair
     * @param u The second object associated with this Pair
     */
    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    /**
     * @return The first object associated with this Pair
     */
    public T getT() {
        return t;
    }

    /**
     * Sets the first object associated with this Pair
     * @param t The first object to associate with this Pair
     */
    public void setT(T t) {
        this.t = t;
    }

    /**
     * @return The second object associated with this Pair
     */
    public U getU() {
        return u;
    }

    /**
     * Sets the second object associated with this Pair
     * @param u The second object to associate with this Pair
     */
    public void setU(U u) {
        this.u = u;
    }

}
