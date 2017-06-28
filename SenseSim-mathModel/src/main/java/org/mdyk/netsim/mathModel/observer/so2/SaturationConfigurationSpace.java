package org.mdyk.netsim.mathModel.observer.so2;

import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;


public class SaturationConfigurationSpace extends ConfigurationSpace {
 
    private double saturation;
    

    public SaturationConfigurationSpace(double saturation) {
        this.saturation = saturation;
    }

    public double getSaturation() {
        return saturation;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(saturation);
    }
}
