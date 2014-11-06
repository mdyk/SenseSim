package org.mdyk.netsim.mathModel.phenomena.time;

/**
 * Simple implementation of IPhenomenonTimeRange
 */
public class SimplePhenomenonTimeRange implements IPhenomenonTimeRange {

    private double fromTime;
    private double toTime;

    public SimplePhenomenonTimeRange(double fromTime, double toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public double fromTime() {
        return fromTime;
    }

    @Override
    public double toTime() {
        return toTime;
    }

    @Override
    public boolean isInTime(double time) {
        if (time >= fromTime && time <= toTime) return true;
        return false;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePhenomenonTimeRange that = (SimplePhenomenonTimeRange) o;

        if(isInTime(that.fromTime) && isInTime(that.toTime)) return true;
        if (fromTime != that.fromTime) return false;
        return toTime == that.toTime;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(fromTime);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(toTime);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
