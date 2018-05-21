package org.mdyk.netsim.logic.node.program.owl.messages;

import org.junit.Test;
import org.mdyk.netsim.logic.infon.Infon;

import static org.junit.Assert.*;


public class InformationNeedAskMessageTest {

    @Test
    public void toJSON() throws Exception {
        String need = "<< relation, o1, o2, o3, (52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554#21.007243);(52.23079#21.007115) , t, 1 >>";

        Infon infon = new Infon(need);


    }

    @Test
    public void getInfon() throws Exception {
    }

}