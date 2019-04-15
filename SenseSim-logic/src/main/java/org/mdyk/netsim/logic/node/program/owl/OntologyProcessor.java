package org.mdyk.netsim.logic.node.program.owl;


import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class OntologyProcessor {

    public static final String relationClassName = "Relation";
    public static final String objectClassName = "Object";
    public static final String unknownClassName = "Unknown";
    public static final String phenomenonClass = "Phenomenon";
    public static final String stateOfAffairClass = "StateOfAffair";
    public static final String sensorClass = "Sensor";

    private static final Logger LOG = Logger.getLogger(OntologyProcessor.class);


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


        try {
            manager.saveOntology(ontology, new SystemOutDocumentTarget());
            reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }

    /**
     * Saves current ontology in a different file
     */
    public void saveOntologySnapshot(String fileName, double simTime) {
        LOG.trace(">> saveOntologySnapshot simTime="+simTime);

        //Create a file for the new format
        File fileformated = new File(fileName+"-"+simTime+".owl");

        try {
            fileformated.createNewFile();
            manager.saveOntology(ontology, new FileDocumentTarget(fileformated));
        } catch (OWLOntologyStorageException | IOException e) {
            throw new RuntimeException(e);
        }

        LOG.trace("<< saveOntologySnapshot");
    }

    public boolean relationExists(String relationName) {

        if(relationName.equalsIgnoreCase("isUnknown")) {
            return true;
        }

        return subClassExists(relationClassName , relationName);
    }

    public boolean isRelationUnknown(String relationName) {
        if(relationName.equalsIgnoreCase("isUnknown")) {
            return false;
        }

        return subClassExists(unknownClassName , relationName);
    }

    public boolean objectExists(String objectName) {
        return subClassExists(objectClassName , objectName);
    }

    public boolean subClassExists(String parentClass , String childClass) {
        OWLClass realtionClass = findClass(parentClass);

        // FIXME
        assert realtionClass != null;

        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(realtionClass, true);

        for (OWLClass subClass : subClasses.getFlattened()) {

            if(labelForClass(subClass).equals(childClass)) {
                return true;
            }

        }

        return false;
    }

    public NodeSet<OWLClass> getSubclasses(String parentClass) {
        OWLClass realtionClass = findClass(parentClass);



        return reasoner.getSubClasses(realtionClass, false);
    }


    public String labelForProperty(OWLObjectProperty objectProperty) {
        String stringId = objectProperty.toStringID();
        return stringId.split("#")[1];
    }

    public String labelForClass(OWLClass owlClass) {
        String stringId = owlClass.toStringID();
        return stringId.split("#")[1];
    }

    public OWLClass createClass(String name) {
        String iri = ontologyIRI+"#"+name;
        return createClass(convertStringToIRI(iri));
    }

    public OWLClass createClass(IRI iri) {
        return df.getOWLClass(iri);
    }

    public OWLClass findClass(String name) {
        OWLClass owlClass = null;

        for (OWLClass cls : ontology.getClassesInSignature()) {
            if(labelForClass(cls).equals(name)) {
                owlClass = cls;
            }
        }

        return owlClass;
    }

    public OWLObjectProperty findProperty(String propertyName) {
        OWLObjectProperty property = null;

        for(OWLObjectProperty prop : ontology.getObjectPropertiesInSignature()) {
            if(labelForProperty(prop).equals(propertyName)) {
                property = prop;
            }
        }

        return property;
    }

    public OWLAxiomChange createSubclass(OWLClass subclass, OWLClass superclass) {
        return new AddAxiom(ontology, df.getOWLSubClassOfAxiom(subclass, superclass));
    }

    private IRI convertStringToIRI(String ns) {
        return IRI.create(ns);
    }

    /**
     * With ontology o, property in refHolder points to a refTo.
     *
     * @param property  the data property reference
     * @param refHolder the container of the property
     * @param refTo     the class the property points to
     * @return a patch to the ontology
     */
    public OWLAxiomChange associateObjectPropertyWithClass(OWLObjectProperty property,
                                                           OWLClass refHolder,
                                                           OWLClass refTo) {
        OWLClassExpression hasSomeRefTo = df.getOWLObjectSomeValuesFrom(property, refTo);
        OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(refHolder, hasSomeRefTo);
        return new AddAxiom(ontology, ax);
    }

    public void applyChange(OWLAxiomChange... axiom) {
        applyChanges(axiom);
    }

    private void applyChanges(OWLAxiomChange... axioms) {
        manager.applyChanges(Arrays.asList(axioms));
        reasoner.flush();
    }

    public OWLIndividual createIndividual(String individualName) {
        String iri = ontologyIRI+"#"+individualName;
        return createIndividual(convertStringToIRI(iri));
    }

    private OWLIndividual createIndividual(IRI iri) {
        return df.getOWLNamedIndividual(iri);
    }

    public OWLAxiomChange detachParentClass(OWLClass subclass, OWLClass superclass) {
        return new RemoveAxiom(ontology, df.getOWLSubClassOfAxiom(subclass, superclass));
    }

    public OWLAxiomChange associateIndividualWithClass(OWLClass clazz,
                                                       OWLIndividual individual) {
        return new AddAxiom(ontology, df.getOWLClassAssertionAxiom(clazz, individual));
    }


    public OWLOntology getOntology() {
        return ontology;
    }
}
