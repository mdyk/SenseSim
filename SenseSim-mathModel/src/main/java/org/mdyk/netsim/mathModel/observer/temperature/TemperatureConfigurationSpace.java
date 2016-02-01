package org.mdyk.netsim.mathModel.observer.temperature;


import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;

public class TemperatureConfigurationSpace extends ConfigurationSpace {

    private double value;
    public static final double MIN = -273;
    public static final double MAX = Double.MAX_VALUE; // Should be equal to Absolute Hot

    public TemperatureConfigurationSpace(double temperature) {
        if(temperature > MAX || temperature < MIN) {
            throw new RuntimeException("Temperature not in <"+MIN+","+MAX+"> interval");
        }
        this.value = temperature;
    }

    public double getTemperature() {
        return value;
    }


}
