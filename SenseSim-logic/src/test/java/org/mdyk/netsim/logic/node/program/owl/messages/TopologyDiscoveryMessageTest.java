package org.mdyk.netsim.logic.node.program.owl.messages;

import junit.framework.TestCase;
import org.junit.Test;

public class TopologyDiscoveryMessageTest {

    @Test
    public void toJSON() throws Exception {

        TopologyDiscoveryMessage tdm = new TopologyDiscoveryMessage(1,22);
        String json = tdm.toJSON();

        TestCase.assertEquals("{\"a\":\"td\",\"s\":1,\"nid\":22}",json);

    }

}