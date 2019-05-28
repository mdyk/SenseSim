package org.mdyk.netsim.logic.infon;

import junit.framework.TestCase;
import org.junit.Test;


public class InfonTest {

    @Test
    public void infonParseTest() throws Exception {

        // FIXME test do odkomentowania kiedy naprawiony zostanie sposób parsowania infonów

        String infonString = "<< relation, o1, o2, o3, l , t, 1 >>";

        Infon infon = new Infon(infonString);

        TestCase.assertEquals("relation" , infon.getRelation());
        TestCase.assertEquals("l" , infon.getSpatialLocation());
        TestCase.assertEquals("t" , infon.getTemporalLocation());
        TestCase.assertEquals("o1" , infon.getObjects().get(0));
        TestCase.assertEquals("o2" , infon.getObjects().get(1));
        TestCase.assertEquals("o3" , infon.getObjects().get(2));

    }

    @Test
    public void infonParseSpatialLocationTest() throws Exception {

        String infonString = "<< relation, o1, o2, o3, " +
                "(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554;21.007243);(52.23079#21.007115) , t, 1 >>";

        Infon infon = new Infon(infonString);

        TestCase.assertEquals("relation" , infon.getRelation());
        TestCase.assertEquals("(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554;21.007243);(52.23079#21.007115)" , infon.getSpatialLocation());
        TestCase.assertEquals("t" , infon.getTemporalLocation());
        TestCase.assertEquals("o1" , infon.getObjects().get(0));
        TestCase.assertEquals("o2" , infon.getObjects().get(1));
        TestCase.assertEquals("o3" , infon.getObjects().get(2));

    }

    @Test
    public void infonRelaptionParamTest() throws Exception {

        String infonString = "<< ?r, Soldier11, ?l , t, 1 >>";

        Infon infon = new Infon(infonString);



    }

    @Test
    public void threePlaceRelationTest() throws Exception {
        String infonString = "<< avgEquals, HeartRate, 0 , 20, ?l, ?t, 1 >>";

        Infon infon = new Infon(infonString);

        infon.getObjects();

    }


    @Test
    public void areObjectParamTest() throws Exception {
        String infonString = "<< << Delayed, ?o, ?l , t, ?p >> >>";

        Infon infon = new Infon(infonString);

        TestCase.assertTrue(infon.areObjectsParam());

    }

}