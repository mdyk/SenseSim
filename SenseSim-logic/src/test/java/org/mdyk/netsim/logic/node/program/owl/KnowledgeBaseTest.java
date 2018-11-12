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

        Infon i1 = new Infon("<<ofType,BodyTemperature,Phenomenon,?l,?t,1>>");

        kb.populateKB(i1);

        kb.saveKBSnapshot(2.0);

        Infon i2 = new Infon("<<ofType,Temperature,StateOfAffair,?l,?t,1>>");

        kb.populateKB(i2);

        Infon i3 = new Infon("<<consistsOf,BodyTemperature,Temperature,?l,?t,1>>");

        kb.populateKB(i3);

        kb.saveKBSnapshot(3.0);

        Infon i4 = new Infon("<<ofType,Soldier,Object,?l,?t,1>>");
        Infon i5 = new Infon("<<consistsOf,Soldier,BodyTemperature,?l,?t,1>>");

        kb.populateKB(i4,i5);

        kb.saveKBSnapshot(4.0);


    }

    @Test
    public void addRelation() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/scenario-IN-distribution/cognitive-agent-ontology.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        File ontologyFile = new File(ontologyPath);


        KnowledgeBase kb = new KnowledgeBase("device-1");

        kb.loadOntology(ontologyFile,ontologyIRI);

        Infon infon = new Infon("<<lessThan,BodyTemperature,36,?l,?t,1>>");

        kb.addRelation("immediate", KnowledgeBase.LogicOperator.AND, infon);

        kb.saveKBSnapshot(5.0);

    }

}