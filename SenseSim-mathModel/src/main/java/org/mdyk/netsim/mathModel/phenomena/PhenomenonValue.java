package org.mdyk.netsim.mathModel.phenomena;


public class PhenomenonValue {

    protected double time;
    protected Object value;
    protected Class valueClass;

    public PhenomenonValue(double time , Object value, Class valueType) {
        this.time = time;
        this.value = value;
        this.valueClass = valueType;
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
}
