package org.mdyk.netsim.mathModel.observer.temperature;

import org.mdyk.netsim.mathModel.observer.PresmisesSpace;

/**
 * Represents premises space for resistance temperature detectors.
 */
public class ResistancePremisesSpace extends PresmisesSpace {

    private double resistance;
    private double resistanceSensitivity;

    public ResistancePremisesSpace(double resistance) {
        this.resistance = resistance;
    }

    public double getResistance() {
        return resistance;
    }
}
