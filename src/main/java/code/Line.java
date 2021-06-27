package code;

import java.io.Serializable;

/**
 * Represents a line connecting two components
 */
public class Line implements Serializable {

    private static final long serialVersionUID = 1800996128086140283L;
    
    /**
     * The id of the first component to which this Line is connected
     */
    public final int compId1;
    /**
     * The id of the port on the first component to which this Line is connected
     */
    public final String portId1;
    /**
     * The id of the second component to which this Line is connected
     */
    public final int compId2;
    /**
     * The id of the port on the second component to which this Line is connected
     */
    public final String portId2;
    /**
     * Whether the line initially moves in the horizontal direction
     */
    public final boolean isHoris;
    /**
     * The percentage of the line between the first component and the first bend in the Line
     */
    public final double movPer;

    /**
     * Creates a new Line
     * @param compId1 The id of the first component to which this Line is connected
     * @param portId1 The id of the port on the first component to which this Line is connected
     * @param compId2 The id of the second component to which this Line is connected
     * @param portId2 The id of the port on the second component to which this Line is connected
     * @param isHoris Whether the line initially moves in the horizontal direction
     * @param movPer The percentage of the line between the first component and the first bend in the Line
     */
    public Line(int compId1, String portId1, int compId2, String portId2, boolean isHoris, double movPer) {
        this.compId1 = compId1;
        this.portId1 = portId1;
        this.compId2 = compId2;
        this.portId2 = portId2;
        this.isHoris = isHoris;
        this.movPer = movPer;
    }
    
}
