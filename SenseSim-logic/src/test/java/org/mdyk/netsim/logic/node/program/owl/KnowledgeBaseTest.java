package org.mdyk.netsim.logic.node.program.owl;

import org.junit.Test;
import org.mdyk.netsim.logic.infon.Infon;

import java.io.File;

/**
 * Created by michal on 17.09.2018.
 */
public class KnowledgeBaseTest {

    @Test
    public void populateKB() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/scenario-IN-distribution/cognitive-agent-ontology.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        File ontologyFile = new File(ontologyPath);


        KnowledgeBase kb = new KnowledgeBase("device-1");

        kb.loadOntology(ontologyFile,ontologyIRI);

        Infon i = new Infon("<<ofType,BodyTemperature,Phenomenon,?l,?t,1>>");

        kb.populateKB(i);

        kb.saveKBSnapshot(2.0);

    }

}