package org.mdyk.netsim.logic.node.program.owl;


import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;

public class KnowledgeBase {

    public static final String UNKNOWN_RELATION = "isUnknown";
    private OntologyProcessor ontologyProcessor;

    public KnowledgeBase() {
        ontologyProcessor = new OntologyProcessor();
    }

    public void loadOntology(File ontologyFile , String ontologyIRI) throws OWLOntologyCreationException {
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);
    }

    public OntologyProcessor getOntologyProcessor() {
        return ontologyProcessor;
    }
}
