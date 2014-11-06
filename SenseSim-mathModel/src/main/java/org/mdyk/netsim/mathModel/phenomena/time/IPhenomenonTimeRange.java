package org.mdyk.netsim.mathModel.phenomena.time;

/**
 * Simple interface for handling fenomenon's time
 */
public interface IPhenomenonTimeRange {

    public double fromTime();

    public double toTime();

    public boolean isInTime(double time);

}
