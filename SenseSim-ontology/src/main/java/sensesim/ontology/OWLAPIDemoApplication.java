package sensesim.ontology;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.io.StringDocumentTarget;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLObjectVisitorExAdapter;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class OWLAPIDemoApplication {

    public static final IRI pizza_iri = IRI
            .create("http://protege.stanford.edu/ontologies/pizza/pizza.owl");
    public static final IRI example_iri = IRI
            .create("http://www.semanticweb.org/ontologies/ont.owl");

    static OWLDataFactory df = OWLManager.getOWLDataFactory();

    public OWLOntologyManager create() {
        OWLOntologyManager m =
                OWLManager.createOWLOntologyManager();
        m.addIRIMapper(new AutoIRIMapper(
                new File("materializedOntologies"), true));
        return m; }

    public static void printHierarchy(OWLReasoner r, OWLClass clazz,
                                      int level, Set<OWLClass> visited) throws OWLException {
//Only print satisfiable classes to skip Nothing
        if (!visited.contains(clazz) && r.isSatisfiable(clazz)) {
            visited.add(clazz);
            for (int i = 0; i < level * 4; i++) {
                System.out.print(" ");
            }
//            System.out.println(labelFor(clazz, r.getRootOntology()));
            System.out.println(clazz);
// Find the children and recurse
            NodeSet<OWLClass> classes = r.getSubClasses(clazz, true);
            for (OWLClass child :  classes.getFlattened()) {
                printHierarchy(r, child, level + 1 , visited);
            }
        } }

    public static String labelFor(OWLEntity clazz, OWLOntology o) {
        LabelExtractor le = new LabelExtractor();
        Set<OWLAnnotation> annotations = clazz.getAnnotations(o);
        for (OWLAnnotation anno :  annotations) {
            String result = anno.accept(le);
            if (result != null) {
                return result;
            }
        }
        return clazz.getIRI().toString();
    }

    // Prints out the properties that instances must have
    public static void printProperties(
            OWLOntologyManager man, OWLOntology o,
            OWLReasoner reasoner, OWLClass cls) {
        System.out.println("Properties of " + cls);
        for (OWLObjectPropertyExpression prop :
                o.getObjectPropertiesInSignature()) {
            // To test if an instance of A MUST have a p-filler,
            // check for the satisfiability of A and not (some p Thing)
            // if this is unsatisfiable, then a p-filler is necessary
            OWLClassExpression restriction =
                    df.getOWLObjectSomeValuesFrom(prop, df.getOWLThing());
            OWLClassExpression intersection =
                    df.getOWLObjectIntersectionOf(cls,
                            df.getOWLObjectComplementOf(restriction));
            if (!reasoner.isSatisfiable(intersection))
                System.out.println("Instances of "
                        + cls + " must have " + prop);
        }
    }

    public static void main(String[] args) throws Exception {
        OWLOntologyManager m = (new OWLAPIDemoApplication()).create();

        OWLOntology o = m.loadOntologyFromOntologyDocument(pizza_iri);

        StringDocumentTarget target = new StringDocumentTarget();
        m.saveOntology(o, target);
        m.removeOntology(o);
        OWLOntology o2 = m
                .loadOntologyFromOntologyDocument(
                        new StringDocumentSource(target.toString()));


        // class A and class B
        OWLClass clsA = df.getOWLClass(IRI.create(pizza_iri + "#A"));
        OWLClass clsB = df.getOWLClass(IRI.create(pizza_iri + "#B")); // Now create the axiom
        OWLAxiom axiom = df.getOWLSubClassOfAxiom(clsA, clsB);
        // add the axiom to the ontology.
        AddAxiom addAxiom = new AddAxiom(o, axiom);
        // We now use the manager to apply the change
        m.applyChange(addAxiom);
        // remove the axiom from the ontology
        RemoveAxiom removeAxiom = new RemoveAxiom(o,axiom); m.applyChange(removeAxiom);

        clsA = df.getOWLClass(
                IRI.create(example_iri + "#A"));
        clsB = df.getOWLClass(
                IRI.create(example_iri + "#B"));
        SWRLVariable var = df.getSWRLVariable(
                IRI.create(example_iri + "#x"));
        SWRLClassAtom body = df.getSWRLClassAtom(clsA, var);
        SWRLClassAtom head = df.getSWRLClassAtom(clsB, var);
        SWRLRule rule = df.getSWRLRule(Collections.singleton(body),
                Collections.singleton(head));
        m.applyChange(new AddAxiom(o, rule));

        // We want to state that matthew has a father who is peter.
        OWLIndividual matthew = df.getOWLNamedIndividual(
        IRI.create(example_iri + "#matthew"));
        OWLIndividual peter = df.getOWLNamedIndividual(
                IRI.create(example_iri + "#peter"));
        // We need the hasFather property
        OWLObjectProperty hasFather = df.getOWLObjectProperty(
                IRI.create(example_iri + "#hasFather"));
        // matthew -> hasFather -> peter
        OWLAxiom assertion = df.getOWLObjectPropertyAssertionAxiom(
                hasFather, matthew, peter);
        // Finally, add the axiom to our ontology
        AddAxiom addAxiomChange = new AddAxiom(o, assertion);
        m.applyChange(addAxiomChange);

        m.saveOntology(o, new SystemOutDocumentTarget());

//        for (OWLClass cls : o.getClassesInSignature()) {
//            System.out.println(cls);
//        }
//
//        OWLClass clazz = df.getOWLThing();
//        System.out.println("Class : " + clazz);
//// Print the hierarchy below thing
//        printHierarchy(o, clazz, 0, new HashSet<OWLClass>());

        // Create a reasoner; it will include the imports closure
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(o);
        // Ask the reasoner to precompute some inferences reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY); // We can determine if the ontology is actually consistent assertTrue(reasoner.isConsistent());

//        Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses(); System.out.println("Unsatisfiable classes:");
//        // leave owl:Nothing out
//        for (OWLClass cls : bottomNode.getEntitiesMinusBottom()) {
//            System.out.println(labelFor(cls, o));
//        }

        // All sublcasses
//        for(OWLClass c : o.getClassesInSignature()) {
//            NodeSet<OWLClass> subclasses = reasoner.getSubClasses(c , true);
//            for(OWLClass subclass : subclasses.getFlattened()) {
//                System.out.println(labelFor(subclass , o) + " Subclass of " + labelFor(c,o));
//            }
//        }


        // All instances

        // reasoner from previous example...
        // for each class, look up the instances
        for (OWLClass c : o.getClassesInSignature()) {
            // the boolean argument specifies direct subclasses
            for (OWLNamedIndividual i :
                    reasoner.getInstances(c, true).getFlattened()) {
                System.out.println(labelFor(i, o) +":"+ labelFor(c, o));
                // look up all property assertions
                for (OWLObjectProperty op:
                        o.getObjectPropertiesInSignature()) {
                    NodeSet<OWLNamedIndividual> petValuesNodeSet =
                            reasoner.getObjectPropertyValues(i, op);
                    for (OWLNamedIndividual value :
                            petValuesNodeSet.getFlattened())
                        System.out.println(labelFor(i, o) + " " +
                                labelFor(op, o) + " " + labelFor(value, o));
                }
            }
        }


        // reasoner from previous example...
        // For this ontology, we know that classes, properties, ...have
        // IRIs of the form: ontology IRI + # + local name
        String iri = pizza_iri + "#Margherita";
        // Now we can query the reasoner
        // to determine the properties that
            // instances of Margherita MUST have
        OWLClass margherita = df.getOWLClass(IRI.create(iri));
        printProperties(m, o, reasoner, margherita);

    }

    static class LabelExtractor extends OWLObjectVisitorExAdapter<String>
            implements OWLAnnotationObjectVisitorEx<String> {
        @Override
        public String visit(OWLAnnotation annotation) {
            if (annotation.getProperty().isLabel()) {
                OWLLiteral c = (OWLLiteral) annotation.getValue();
                return c.getLiteral();
            }
            return null;
        }
    }
}