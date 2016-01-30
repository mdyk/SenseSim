package org.mdyk.netsim.mathModel.observer.temperature;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;



public class PT100ObserverTest {

    @Test
    public void testGetPremises() throws Exception {

        PT100Observer observer = new PT100Observer();

        TemperatureConfigurationSpace event = new TemperatureConfigurationSpace(36.6);

        ResistancePremisesSpace premise = observer.getPremises(event);

        TestCase.assertTrue(premise.getResistance()<= (114.274 + 0.07) && premise.getResistance()>= (114.274 - 0.07));



    }
}