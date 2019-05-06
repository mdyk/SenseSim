package org.mdyk.netsim.logic.node.program.owl.messages;

import org.junit.Test;
import org.mdyk.netsim.logic.infon.Infon;

import static org.junit.Assert.*;

/**
 * Created by michal on 12/11/2018.
 */
public class InformationNeedRespMessageTest {
    @Test
    public void toJSON() throws Exception {
        String infonS = "<<isUnknown,immediate,?l,?t,1,>>";
        String expectedMessage = "{\"a\":\"inr\",\"s\":3,\"nid\":259056125,\"i\":\"<<isUnknown,immediate,?l,?t,1,>>\",\"psn\":[]}";

        InformationNeedRespMessage inr = new InformationNeedRespMessage(3,259056125,null);
        inr.addInfon(new Infon(infonS));

        assertEquals(expectedMessage , inr.toJSON());

    }

    @Test
    public void toJSON_multipleInfons() {
        String infon1 = "<<isUnknown1,immediate,?l,?t,1,>>";
        String infon2 = "<<isUnknown2,immediate,?l,?t,1,>>";
        String infon3 = "<<isUnknown3,immediate,?l,?t,1,>>";

        InformationNeedRespMessage inr = new InformationNeedRespMessage(3,259056125);
        inr.addInfon(new Infon(infon1));
        inr.addInfon(new Infon(infon2));
        inr.addInfon(new Infon(infon3));

        System.out.println(inr.toJSON());

    }

    @Test
    public void getInfons() throws Exception {
        String json = "{\"a\":\"inr\",\"s\":3,\"nid\":259056125,\"i\":\"<<isUnknown,immediate,?l,?t,1,>>;<<isUnknown,immediate,?l,?t,1,>>\",\"psn\":[]}";
        InformationNeedMessage inm = MessageParser.parseJSON(json);

        assertTrue(inm instanceof InformationNeedRespMessage);

        InformationNeedRespMessage inrp = (InformationNeedRespMessage) inm;

        assertTrue(inrp.getInfons().size() == 2);

    }

}