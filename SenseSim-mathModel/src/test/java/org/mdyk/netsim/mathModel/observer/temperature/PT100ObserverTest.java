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

    @Test
    public void testGetPremiseAndConclusion() throws Exception {
        PT100Observer observer = new PT100Observer();

        TemperatureConfigurationSpace event = new TemperatureConfigurationSpace(36.6);

        ResistancePremisesSpace premise = observer.getPremises(event);

        TemperatureConfigurationSpace concludion = observer.getConclusion(premise);

        TestCase.assertTrue(concludion.getTemperature()<= (36.6 + 0.19) && concludion.getTemperature()>= (36.6 - 0.19));

    }

    @Test
    public void testInLoop() throws Exception {
        for(int i = 1 ; i <= 50 ; i++) {
            testGetPremiseAndConclusion();
        }
    }
}