package org.mdyk.netsim.mathModel.phenomena;

import java.util.Comparator;

/**
 * Holds value of the phenomenon
 */
public class PhenomenonValue {

    protected double time;
    protected Object value;
    protected Class valueClass;

    public PhenomenonValue(double time , Object value) {
        this.time = time;

        if(value == null) {
            this.value = new NullPhenomenonValue();
            this.valueClass = NullPhenomenonValue.class;
        }
        else {
            this.value = value;
            this.valueClass = value.getClass();
        }
    }

    public double getTime() {
        return time;
    }

    public Object getValue() {
        return value;
    }

    public Class getValueClass() {
        return valueClass;
    }

    /**
     * Represents null value of the phenomenon
     */
    public static class NullPhenomenonValue{}

    @Override
    public String toString() {
        return "PhenomenonValue{" +
                "time=" + time +
                ", value=" + value.toString() +
                ", valueClass=" + valueClass +
                '}';
    }

    public static class DescTimeComparator implements Comparator<PhenomenonValue> {

        @Override
        public int compare(PhenomenonValue o1, PhenomenonValue o2) {

            if(o1.getTime() < o2.getTime()) {
                return 1;
            } else if (o1.getTime() > o2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

}
