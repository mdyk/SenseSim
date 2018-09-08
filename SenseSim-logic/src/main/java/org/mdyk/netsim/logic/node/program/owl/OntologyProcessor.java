package org.mdyk.netsim.logic.node.program.owl;


import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.*;

import java.io.File;

public class OntologyProcessor {

    private static final String relationClassName = "Relation";
    private static final String objectClassName = "Object";
    private static final String unknownClassName = "Unknown";

    String	prefix;
    private OWLOntology ontology;
    private String ontologyIRI;
    private OWLDataFactory df = OWLManager.getOWLDataFactory();
    private OWLOntologyManager manager;
    private PelletReasoner reasoner;

    public OntologyProcessor() {
        manager = OWLManager.createOWLOntologyManager();
    }

    public void loadOntology(File ontologyFile , String ontologyIRI) throws OWLOntologyCreationException {
        this.ontologyIRI = ontologyIRI;
        this.ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);

        prefix ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX ont: <"+this.ontologyIRI+"#>";

//        populateIndyviduals();

        try {
            manager.saveOntology(ontology, new SystemOutDocumentTarget());
            reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }

}
