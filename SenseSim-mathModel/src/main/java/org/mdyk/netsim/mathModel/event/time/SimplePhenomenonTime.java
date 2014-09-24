package org.mdyk.netsim.mathModel.event.time;

/**
 * Simple implementation of IPhenomenonTime
 */
public class SimplePhenomenonTime implements IPhenomenonTime {

    private int fromTime;
    private int toTime;

    public SimplePhenomenonTime(int fromTime, int toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public int fromTime() {
        return fromTime;
    }

    @Override
    public int toTime() {
        return toTime;
    }

    @Override
    public boolean isInTime(int time) {
        if (time >= fromTime && time <= toTime) return true;
        return false;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimplePhenomenonTime that = (SimplePhenomenonTime) o;

        if(isInTime(that.fromTime) && isInTime(that.toTime)) return true;
        if (fromTime != that.fromTime) return false;
        return toTime == that.toTime;

    }

    @Override
    public int hashCode() {
        int result = fromTime;
        result = 31 * result + toTime;
        return result;
    }
}
