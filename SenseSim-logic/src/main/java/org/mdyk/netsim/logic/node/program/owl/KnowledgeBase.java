package org.mdyk.netsim.logic.node.program.owl;


import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.infon.Infon;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;

public class KnowledgeBase {

    public static final String UNKNOWN_RELATION = "isUnknown";
    private static final Logger LOG = Logger.getLogger(KnowledgeBase.class);
    private static final String OF_TYPE = "ofType";
    private static final String CONSISTS_OF = "consistsOf";
    private static final String PERCEIVES = "perceives";
    private static final String HAS_OBSERVER = "hasObserver";

    private OntologyProcessor op;
    private String kbName;

    public KnowledgeBase(String kbName) {
        this.op = new OntologyProcessor();
        this.kbName = kbName;
    }

    public void loadOntology(File ontologyFile , String ontologyIRI) throws OWLOntologyCreationException {
        LOG.trace(">> loadOntology ontologyFile="+ontologyFile+ " ontologyIRI="+ontologyIRI);
        op.loadOntology(ontologyFile,ontologyIRI);
        LOG.trace("<< loadOntology");
    }

    public void saveKBSnapshot(double simTime) {
        LOG.trace(">> saveKBSnapshot kbName="+kbName+" simTime="+simTime);
        op.saveOntologySnapshot(kbName,simTime);
        LOG.trace("<< saveKBSnapshot");
    }

    public void populateKB(Infon ... situation) {
        LOG.trace(">> populateKB kbName="+kbName);

        if(LOG.isDebugEnabled()) {
            for(Infon infon : situation) {
                LOG.debug(infon.toString());
            }
        }

        for(Infon infon : situation) {
            String relation = infon.getRelation();

            switch(relation) {
                case CONSISTS_OF:

                    break;

                case HAS_OBSERVER:

                    break;

                case OF_TYPE:
                    String childClass = infon.getObjects().get(0);
                    String parentClass = infon.getObjects().get(1);
                    addSubclass(childClass, parentClass);
                    break;

                case PERCEIVES:

                    break;
            }

        }

        LOG.trace("<< populateKB");
    }

    private void addSubclass(String child, String parent) {
        LOG.trace(">> addSubclass childClass="+child+" parentClass="+parent);

        OWLClass childClass = op.findClass(child);
        if(childClass == null) {
            childClass = op.createClass(child);
        }

        OWLClass parentClass = op.findClass(parent);

        OWLAxiomChange change = op.createSubclass(childClass,parentClass);

        op.applyChange(change);

        LOG.trace("<< addSubclass");
    }

    public OntologyProcessor getOntologyProcessor() {
        return op;
    }
}
