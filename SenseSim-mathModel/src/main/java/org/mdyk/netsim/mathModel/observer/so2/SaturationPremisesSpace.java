package org.mdyk.netsim.mathModel.observer.so2;

import org.mdyk.netsim.mathModel.observer.PresmisesSpace;



public class SaturationPremisesSpace extends PresmisesSpace {

    private double saturation;

    public SaturationPremisesSpace(double saturation) {
        this.saturation = saturation;
    }

    public double getSaturation() {
        return saturation;
    }

}
