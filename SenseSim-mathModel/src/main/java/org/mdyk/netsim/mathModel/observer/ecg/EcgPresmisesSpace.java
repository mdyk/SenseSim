package org.mdyk.netsim.mathModel.observer.ecg;

import org.mdyk.netsim.mathModel.observer.PresmisesSpace;


public class EcgPresmisesSpace extends PresmisesSpace {

    private double milivolts;

    public EcgPresmisesSpace(double milivolts) {
        this.milivolts = milivolts;
    }

    public double getMilivolts() {
        return milivolts;
    }
}
