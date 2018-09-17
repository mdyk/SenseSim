package org.mdyk.netsim.logic.node.program.owl;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;


public class OntologyProcessorTest {

    @Test
    public void relationExists() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/cognitive-agent-ontology2.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        OntologyProcessor ontologyProcessor = new OntologyProcessor();

        File ontologyFile = new File(ontologyPath);
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);

        TestCase.assertTrue(ontologyProcessor.relationExists("Immediate"));
        TestCase.assertFalse(ontologyProcessor.relationExists("Foo"));

    }

    @Test
    public void objectExists() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/cognitive-agent-ontology2.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        OntologyProcessor ontologyProcessor = new OntologyProcessor();

        File ontologyFile = new File(ontologyPath);
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);

        TestCase.assertTrue(ontologyProcessor.objectExists("Sensor"));
        TestCase.assertFalse(ontologyProcessor.objectExists("Foo"));

    }

}