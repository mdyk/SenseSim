package org.mdyk.netsim.logic.node.program.owl.messages;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TopologyDiscoveryMessageTest {

    @Test
    public void toJSON() throws Exception {

        TopologyDiscoveryMessage tdm = new TopologyDiscoveryMessage(1);
        String json = tdm.toJSON();

        TestCase.assertEquals("{\"a\":\"td\",\"s\":1}",json);

    }

}