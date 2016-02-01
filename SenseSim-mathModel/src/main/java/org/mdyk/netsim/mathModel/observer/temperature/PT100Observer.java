package org.mdyk.netsim.mathModel.observer.temperature;

import org.apache.log4j.Logger;
import org.mdyk.netsim.mathModel.observer.ObserverModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Represents PT100 class thermometer sensor
 */
public class PT100Observer implements ObserverModel<TemperatureConfigurationSpace , ResistancePremisesSpace> {

    private static final Logger LOG = Logger.getLogger(PT100Observer.class);

    private final String name = "PT100";

    // resistance for platinium for 0 ceclsius degrees.
    private final double referenceResistance = 100.0;

    private List<TempRangeResistance> resistanceRange;

    public PT100Observer() {
        this.resistanceRange = new ArrayList<>(8);

        resistanceRange.add(new TempRangeResistance(-200 , -150, 18.52 , 39.72, 0.43, 0.24));
        resistanceRange.add(new TempRangeResistance(-150 , -100, 39.72, 60.26, 0.42, 0.19));
        resistanceRange.add(new TempRangeResistance(-100 , -50, 60.26 , 80.31, 0.41, 0.14));
        resistanceRange.add(new TempRangeResistance(-50 , 0, 80.31 , 100.0,  0.40, 0.14));
        resistanceRange.add(new TempRangeResistance(0 , 50, 100.0 , 119.40,  0.39, 0.07));
        resistanceRange.add(new TempRangeResistance(50 , 100, 119.40 , 138.51,  0.38, 0.12));
        resistanceRange.add(new TempRangeResistance(100 , 200, 138.51 , 175.86, 0.37, 0.15));
        resistanceRange.add(new TempRangeResistance(200 , 350, 175.86 , 229.72, 0.36, 0.27));

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
        LOG.trace(">> getConclusion");
        double conclusionTemperature = calculateTempForResistance(premises.getResistance());

        TemperatureConfigurationSpace conclusion = new TemperatureConfigurationSpace(conclusionTemperature);
        LOG.debug("Temperature for resistance "+ premises.getResistance() +" is "+ conclusionTemperature);

        LOG.trace(">> getConclusion");
        return conclusion;
    }

    @Override
    public String getName() {
        return name;
    }


    /**
     * Calculates temperature from given resistance using equasion:
     *  temp = (res - 100) / S, where:
     *      - S - sensitivity for given temperature (i.e. 0,39 for 40 celsius degrees)
     * @param resistance
     *      input resistance
     * @return
     *      temperature for given resistance
     */
    private double calculateTempForResistance(double resistance) {

        double temperature = Double.NaN;
        boolean found = false;

        Iterator<TempRangeResistance> rangeIt = this.resistanceRange.iterator();
        TempRangeResistance resistaneRange = null;
        while(rangeIt.hasNext() && !found) {
            TempRangeResistance range = rangeIt.next();
            if(range.isInResistanceRange(resistance)) {
                found = true;
                resistaneRange = range;
            }
        }

        if (found) {
            temperature = (resistance - this.referenceResistance) / resistaneRange.getSensitivity();
        }


        return temperature;
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
            if(range.isInTempRange(temperature)) {
                found = true;
                resistaneRange = range;
            }
        }

        if(found) {
            resistance = (temperature * resistaneRange.getSensitivity()) + referenceResistance + resistaneRange.randomError();
        }

        return resistance;

    }

    private class TempRangeResistance {

        private double minTemp;
        private double maxTemp;
        private double minNominalResistance;
        private double maxNomminalResistance;
        private double sensitivity;
        private double resistanceError;


        public TempRangeResistance(double minTemp, double maxTemp, double minNominalResistance, double maxNomminalResistance, double sensitivity, double resistanceError) {
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.minNominalResistance = minNominalResistance;
            this.maxNomminalResistance = maxNomminalResistance;
            this.sensitivity = sensitivity;
            this.resistanceError = resistanceError;
        }

        public double getSensitivity() {
            return sensitivity;
        }

        public boolean isInTempRange(double temperature) {
            return temperature >= minTemp && temperature < maxTemp;
        }

        public boolean isInResistanceRange(double resistance) {
            return resistance >= minNominalResistance && resistance < maxNomminalResistance;
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


