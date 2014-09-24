package org.mdyk.netsim.mathModel.event.time;

/**
 * Simple interface for handling fenomenon's time
 */
public interface IPhenomenonTime {

    public int fromTime();

    public int toTime();

    public boolean isInTime(int time);

}
