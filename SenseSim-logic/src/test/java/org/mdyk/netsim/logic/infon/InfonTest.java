package org.mdyk.netsim.logic.infon;

import junit.framework.TestCase;
import org.junit.Test;


public class InfonTest {

    @Test
    public void infonParseTest() throws Exception {

        String infonString = "<< relation, o1, o2, o3, l , t, 1 >>";

        Infon infon = new Infon(infonString);

        TestCase.assertEquals("relation" , infon.getRelation());
        TestCase.assertEquals("l" , infon.getSpatialLocation());
        TestCase.assertEquals("t" , infon.getTemporalLocation());
        TestCase.assertEquals("o1" , infon.getObjects().get(0));
        TestCase.assertEquals("o2" , infon.getObjects().get(1));
        TestCase.assertEquals("o3" , infon.getObjects().get(2));

    }


}