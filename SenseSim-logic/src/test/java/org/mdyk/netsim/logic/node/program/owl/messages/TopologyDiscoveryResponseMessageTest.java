package org.mdyk.netsim.logic.node.program.owl.messages;

import org.junit.Assert;
import org.junit.Test;
import org.mdyk.netsim.logic.util.GeoPosition;

import static org.junit.Assert.*;

/**
 * Created by michal on 27.03.2018.
 */
public class TopologyDiscoveryResponseMessageTest {
    @Test
    public void toJSON() throws Exception {
        TopologyDiscoveryResponseMessage tdmr = new TopologyDiscoveryResponseMessage(2 , new GeoPosition(52.230797,21.008139));
        String respojseJSON = tdmr.toJSON();
        Assert.assertEquals("{\"p\":\"52.230797#21.008139\",\"a\":\"tdr\",\"id\":2}" , respojseJSON);
    }

}