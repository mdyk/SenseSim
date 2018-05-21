package org.mdyk.netsim.logic.node.program.owl.messages;

import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.infon.Infon;

import static org.junit.Assert.*;


public class InformationNeedAskMessageTest {

    @Test
    public void toJSON() throws Exception {
        String need = "<< relation, o1, o2, o3, (52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115) , t, 1 >>";
        String expectedJsonString = "{\"a\":\"in\",\"i\":\"<<relation,o1,o2,o3,(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115),t,1,>>\"}";

        Infon infon = new Infon(need);
        InformationNeedAskMessage iam = new InformationNeedAskMessage(infon);
        String iamJSON = iam.toJSON();

        TestCase.assertEquals(expectedJsonString , iamJSON);
    }

    @Test
    public void getInfon() throws Exception {

        String messageJsonString = "{\"a\":\"in\",\"i\":\"<<relation,o1,o2,o3,(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115),t,1,>>\"}";

        InformationNeedMessage inm = MessageParser.parseJSON(messageJsonString);
        TestCase.assertTrue(inm instanceof InformationNeedAskMessage);
        InformationNeedAskMessage iam = (InformationNeedAskMessage) inm;

        Infon infon = iam.getInfon();

        TestCase.assertEquals("relation" , infon.getRelation());
        TestCase.assertEquals("1" , infon.getPolarity());
        TestCase.assertEquals("t", infon.getTemporalLocation());
        TestCase.assertEquals("(52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115)", infon.getSpatialLocation());

    }

}