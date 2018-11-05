package org.mdyk.netsim.logic.node.program.owl;


import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.infon.Infon;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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

    //FIXME obsługa statusów wynikowych
    public Set<KBProcessStatus> populateKB(Infon ... situation) {
        LOG.trace(">> populateKB kbName="+kbName);

        Set<KBProcessStatus> processStatuses = new HashSet<>();

        if(LOG.isDebugEnabled()) {
            for(Infon infon : situation) {
                LOG.debug(infon.toString());
            }
        }

        for(Infon infon : situation) {
            String relation = infon.getRelation();

            switch(relation) {
                case CONSISTS_OF:
                    String classFrom = infon.getObjects().get(0);
                    String classTo = infon.getObjects().get(1);
                    addProperyToClass(CONSISTS_OF,classFrom,classTo);
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
        return processStatuses;
    }

    private void addProperyToClass(String property, String classFrom, String classTo) {
        LOG.trace(">> addProperyToClass property="+property+" classFrom="+classFrom+" classTo="+classTo);

        OWLClass owlClassFrom = op.findClass(classFrom);
        OWLClass owlClassTo = op.findClass(classTo);

        OWLObjectProperty owlProperty = op.findProperty(property);

        if (owlClassFrom != null && owlClassTo != null && owlProperty != null) {

            OWLAxiomChange change = op.associateObjectPropertyWithClass(owlProperty,owlClassFrom,owlClassTo);
            op.applyChange(change);

        } else {
            // FIXME poprawna obsługa wyjątków.
            throw new RuntimeException("[addProperyToClass] nie istnieje klasa lub property ");
        }

        LOG.trace("<< addProperyToClass");
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
