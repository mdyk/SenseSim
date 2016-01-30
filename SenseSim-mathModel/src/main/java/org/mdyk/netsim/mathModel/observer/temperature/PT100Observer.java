package org.mdyk.netsim.mathModel.observer.temperature;

import org.apache.log4j.Logger;
import org.mdyk.netsim.mathModel.observer.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Represents PT100 class thermometer sensor
 */
public class PT100Observer implements Observer<TemperatureConfigurationSpace , ResistancePremisesSpace>{

    private static final Logger LOG = Logger.getLogger(PT100Observer.class);

    // resistance for platinium for 100 ceclsius degrees.
    private final double referenceResistance = 100.0;

    private List<TempRangeResistance> resistanceRange;

    public PT100Observer() {
        this.resistanceRange = new ArrayList<>();

        resistanceRange.add(new TempRangeResistance(-200 , -150, 0.43, 0.24));
        resistanceRange.add(new TempRangeResistance(-150 , -100, 0.42, 0.19));
        resistanceRange.add(new TempRangeResistance(-100 , -50, 0.41, 0.14));
        resistanceRange.add(new TempRangeResistance(-50 , 0, 0.40, 0.14));
        resistanceRange.add(new TempRangeResistance(0 , 50, 0.39, 0.07));
        resistanceRange.add(new TempRangeResistance(50 , 100, 0.38, 0.12));
        resistanceRange.add(new TempRangeResistance(100 , 200, 0.37, 0.15));
        resistanceRange.add(new TempRangeResistance(200 , 350, 0.36, 0.27));

    }

    @Override
    public TemperatureConfigurationSpace getConfigurationSpace() {
        return null;
    }

    @Override
    public ResistancePremisesSpace getPremises(TemperatureConfigurationSpace event, Object... parameters) {
        LOG.trace(">> getPremises");

        double resistance = calculateResistanceForTemp(event.getTemperature());
        LOG.debug("Resistance for temp "+ event.getTemperature() +" is "+ resistance);

        LOG.trace("<< getPremises");
        return new ResistancePremisesSpace(resistance);
    }

    @Override
    public TemperatureConfigurationSpace getConclusion(ResistancePremisesSpace premises) {
        return null;
    }

    /**
     * Calculates resistance for a given temperature.
     * Equasion: res = S x t + 100 +/- err, where:
     *      - S - sensitivity for given temperature (i.e. 0,39 for 40 celsius degrees)
     *      - t - given temperature
     *      - err - error of the sensor (i.e. 0,07 for 40 celsius degrees)
     * @return
     *      value of the resistance
     */
    private double calculateResistanceForTemp(double temperature) {

        double resistance = Double.NaN;

        boolean found = false;

        Iterator<TempRangeResistance> rangeIt = this.resistanceRange.iterator();
        TempRangeResistance resistaneRange = null;
        while(rangeIt.hasNext() && !found) {
            TempRangeResistance range = rangeIt.next();
            if(range.isInRange(temperature)) {
                found = true;
                resistaneRange = range;
            }
        }

        if(found) {
            resistance = (temperature * resistaneRange.getSensitivity()) + 100 + resistaneRange.randomError();
        }

        return resistance;

    }

    private class TempRangeResistance {

        private double minTemp;
        private double maxTemp;
        private double sensitivity;
        private double resistanceError;

        public TempRangeResistance(double minTemp, double maxTemp, double sensitivity, double resistanceError) {
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.sensitivity = sensitivity;
            this.resistanceError = resistanceError;
        }

        public double getSensitivity() {
            return sensitivity;
        }

        public boolean isInRange(double temperature) {
            return temperature >= minTemp && temperature < maxTemp;
        }

        public double randomError() {
            Random rand = new Random();
            boolean positive = rand.nextBoolean();

            double error = rand.nextDouble() * this.resistanceError;

            if(!positive) {
                error *= -1;
            }

            return error;
        }

    }

}


