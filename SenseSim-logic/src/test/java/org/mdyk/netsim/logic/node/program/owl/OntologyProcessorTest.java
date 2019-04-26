package org.mdyk.netsim.logic.node.program.owl;

import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.infon.Infon;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class OntologyProcessorTest {

    @Test
    public void relationExists() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/scenario-IN-distribution/cognitive-agent-ontology.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        OntologyProcessor ontologyProcessor = new OntologyProcessor();

        File ontologyFile = new File(ontologyPath);
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);

        TestCase.assertTrue(ontologyProcessor.relationExists("Immediate"));
        TestCase.assertFalse(ontologyProcessor.relationExists("Foo"));

    }

    @Test
    public void objectExists() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/scenario-IN-distribution/cognitive-agent-ontology.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        OntologyProcessor ontologyProcessor = new OntologyProcessor();

        File ontologyFile = new File(ontologyPath);
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);

        TestCase.assertTrue(ontologyProcessor.objectExists("Sensor"));
        TestCase.assertFalse(ontologyProcessor.objectExists("Foo"));

    }

    @Test
    public void findProperty() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/scenario-IN-distribution/cognitive-agent-ontology.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        OntologyProcessor ontologyProcessor = new OntologyProcessor();

        File ontologyFile = new File(ontologyPath);
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);

        TestCase.assertNotNull(ontologyProcessor.findProperty("consistsOf"));

    }

    @Test
    public void saveOntologySnapshot() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/SenseSim-app/src/main/resources/scenario-IN-distribution/cognitive-agent-ontology.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";

        OntologyProcessor ontologyProcessor = new OntologyProcessor();

        File ontologyFile = new File(ontologyPath);
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);

        ontologyProcessor.saveOntologySnapshot("ontologyTest", 1.0);

        File snapshotFile = new File("ontologyTest-1.0.owl");

        TestCase.assertTrue(snapshotFile.exists());

        TestCase.assertTrue(snapshotFile.delete());

    }

    @Test
    public void getSubclasses() throws Exception {
        String ontologyPath = "/Users/michal/Documents/Workspace/SenseSim/Device-dev_1-0.0.owl";
        String ontologyIRI = "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology";


        OntologyProcessor ontologyProcessor = new OntologyProcessor();

        File ontologyFile = new File(ontologyPath);
        ontologyProcessor.loadOntology(ontologyFile,ontologyIRI);


        NodeSet<OWLClass> subClasses =  ontologyProcessor.getSubclasses("Sensor");

        for (OWLClass subClass : subClasses.getFlattened()) {
            System.out.println(subClass);
            subClass.getObjectPropertiesInSignature();

        }

//        Node<OWLClass> subClasses =  ontologyProcessor.getSubclasses("Observer");
    }

    @Test
    public void tempertaureUnknown() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("Device-dev_1-0.0.owl");

        OntologyProcessor op = new OntologyProcessor();
        op.loadOntology(new File(resource.getFile()), "http://www.semanticweb.org/michal/ontologies/2018/7/cognitive-agent-ontology");

        String objectName = "Temperature";

        OWLClass clazz = op.findClass(objectName);

        NodeSet<OWLClass> superClasses = op.getSuperClasses(clazz);

        if(superClasses.containsEntity(op.findClass("StateOfAffair"))) {

        }



    }

    @Test
    public void test() {


        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("Device-dev_1-0.0.owl");

        File file = new File(resource.getFile());
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology;

        try {
            ontology = manager.loadOntologyFromOntologyDocument(file);

            Set<OWLClass> classes;
            Set<OWLObjectProperty> prop;
            Set<OWLDataProperty> dataProp;
            Set<OWLNamedIndividual> individuals;

            classes = ontology.getClassesInSignature();
            prop = ontology.getObjectPropertiesInSignature();
            dataProp = ontology.getDataPropertiesInSignature();
            individuals = ontology.getIndividualsInSignature();
            //configurator = new OWLAPIOntologyConfigurator(this);

            System.out.println("Classes");
            System.out.println("--------------------------------");

            String sota = "Temperature";
            String property = "perceives";
            List<OWLClass> sensors = new ArrayList<>();

            for (OWLSubClassOfAxiom op : ontology.getAxioms(AxiomType.SUBCLASS_OF)) {
//                    if (op.getDomain().equals(cls)) {
//                        for(OWLObjectProperty oop : op.getObjectPropertiesInSignature()){
//                            System.out.println("\t\t +: " + oop.getIRI().getShortForm());
//                        }
//                        //System.out.println("\t\t +: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
//                    }

//                    System.out.println(op.getSuperClass().getObjectPropertiesInSignature());
                    if(op.getSuperClass() instanceof OWLObjectSomeValuesFrom){

                        OWLClass owlClass = op.getSubClass().asOWLClass();

                        OWLObjectSomeValuesFrom owlObjectSomeValuesFrom = (OWLObjectSomeValuesFrom) op.getSuperClass();

                        OntologyProcessor ontologyProcessor = new OntologyProcessor();

                        owlObjectSomeValuesFrom.getProperty().asOWLObjectProperty();

                        if(ontologyProcessor.labelForProperty(owlObjectSomeValuesFrom.getProperty().asOWLObjectProperty()).equalsIgnoreCase(property)) {
                            if(ontologyProcessor.labelForClass(owlObjectSomeValuesFrom.getFiller().asOWLClass()).equalsIgnoreCase(sota)) {
                                sensors.add(owlClass);
                            }
                        }

//                        System.out.println(ontologyProcessor.labelForProperty(owlObjectSomeValuesFrom.getProperty().asOWLObjectProperty()));
//
//
//                        System.out.println(owlObjectSomeValuesFrom);
                    }

//                System.out.println(op);

            }


//            for (OWLClass cls : classes) {
//                System.out.println("+: " + cls.getIRI().getShortForm());
//
//                System.out.println(" \tObject Property Domain");
//                for (OWLSubClassOfAxiom op : ontology.getAxioms(AxiomType.SUBCLASS_OF)) {
////                    if (op.getDomain().equals(cls)) {
////                        for(OWLObjectProperty oop : op.getObjectPropertiesInSignature()){
////                            System.out.println("\t\t +: " + oop.getIRI().getShortForm());
////                        }
////                        //System.out.println("\t\t +: " + op.getProperty().getNamedProperty().getIRI().getShortForm());
////                    }
//
////                    System.out.println(op.getSuperClass().getObjectPropertiesInSignature());
////                    if(op.getSuperClass() instanceof OWLObjectSomeValuesFrom){
////                        OWLObjectSomeValuesFrom owlObjectSomeValuesFrom = (OWLObjectSomeValuesFrom) op.getSuperClass();
////
////                        owlObjectSomeValuesFrom.getFiller().asOWLClass();
////                        System.out.println(owlObjectSomeValuesFrom);
////                    }
//
//                    System.out.println(op);
//
//                }

//                System.out.println(" \tData Property Domain");
//                for (OWLDataPropertyDomainAxiom dp : ontology.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN)) {
//                    if (dp.getDomain().equals(cls)) {
//                        for(OWLDataProperty odp : dp.getDataPropertiesInSignature()){
//                            System.out.println("\t\t +: " + odp.getIRI().getShortForm());
//                        }
//                        //System.out.println("\t\t +:" + dp.getProperty());
//                    }
//                }

//            }

        } catch (OWLOntologyCreationException ex) {
//            Logger.getLogger(OntologyAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}