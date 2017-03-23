package org.mdyk.netsim.mathModel.observer.ecg;


import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;


public class EcgConfigurationSpace extends ConfigurationSpace {

    private double milivolts;

    public EcgConfigurationSpace(double milivolts) {
        this.milivolts = milivolts;
    }

    public double getMilivolts() {
        return this.milivolts;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(milivolts);
    }
}
