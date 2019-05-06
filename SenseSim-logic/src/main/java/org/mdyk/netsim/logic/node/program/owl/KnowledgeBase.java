package org.mdyk.netsim.logic.node.program.owl;


import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.infon.Infon;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

import java.io.File;
import java.util.*;

public class KnowledgeBase {


    public static final String UNKNOWN_RELATION = "isUnknown";
    private static final Logger LOG = Logger.getLogger(KnowledgeBase.class);
    private static final String OF_TYPE = "ofType";
    private static final String CONSISTS_OF = "consistsOf";
    private static final String PERCEIVES = "perceives";
    private static final String HAS_OBSERVER = "hasObserver";
    private OntologyProcessor op;
    private String kbName;
    private Map<String, RelationDefinition> retionDefinitions;
    private Map<String, StandardRelationDefinition> standardRelationDefinitions;

    public KnowledgeBase(String kbName) {
        this.op = new OntologyProcessor();
        this.kbName = kbName;
        this.retionDefinitions = new HashMap<>();
        populateStandardRelations();
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
            String classFrom = infon.getObjects().get(0);
            String classTo = infon.getObjects().get(1);
            switch(relation) {
                case CONSISTS_OF:
                    addProperyToClass(CONSISTS_OF,classFrom,classTo);
                    break;

                case HAS_OBSERVER:
                    // TODO weryfikacja czy klasy są odowiednich typow: hasObserver(Sensor, Observer)
                    addProperyToClass(HAS_OBSERVER,classFrom,classTo);
                    break;

                case OF_TYPE:
                    String childClass = infon.getObjects().get(0);
                    String parentClass = infon.getObjects().get(1);
                    addSubclass(childClass, parentClass);
                    break;

                case PERCEIVES:
                    // TODO weryfikacja czy klasy są odowiednich typow: hasObserver(Object, StateOfAffair)
                    addProperyToClass(PERCEIVES,classFrom,classTo);
                    break;
            }

        }

        LOG.trace("<< populateKB");
        return processStatuses;
    }

    public void addRelation(String relationName, LogicOperator operator, List<Infon> infons) {
        Infon[] infArray = new Infon[infons.size()];
        infArray = infons.toArray(infArray);
        addRelation(relationName,operator,infArray);
    }

    // FIXME to powinno się znajdować w metodzie populateKB
    public void addRelation(String relationName, LogicOperator operator, Infon ... infons) {
        if(!op.relationExists(relationName)){
            addSubclass(relationName,OntologyProcessor.relationClassName);
        }

        OWLClass relationClass = op.findClass(relationName);

        RelationDefinition definition = new RelationDefinition(relationName, operator , infons);
        this.retionDefinitions.put(relationName, definition);

        // Dodanie indyvidual do ontologii
        OWLIndividual individual = op.createIndividual(relationName);
        op.applyChange(op.associateIndividualWithClass(relationClass , individual));

        if(op.isRelationUnknown(relationName)) {

        }

    }

    public List<Infon> collectKnowledgeAboutObject(String objectName) {

        List<Infon> infons = new ArrayList<>();

        OWLClass clazz = op.findClass(objectName);

        if(clazz == null) {
            return infons;
        }

        NodeSet<OWLClass> superClasses = op.getSuperClasses(clazz);

        for (OWLClass owlClass : superClasses.getFlattened()) {
            Infon i = new Infon("<< "+ OF_TYPE +" , " + objectName + " , "+ op.labelForClass(owlClass) +", ?l, ?t, 1 >>");
            infons.add(i);
        }

        if(superClasses.containsEntity(op.findClass("StateOfAffair"))) {
            List<String> sensors = sensorsWhichPerceivesSOA(objectName);

            for (String sensor : sensors) {
                Infon i = new Infon("<< "+ PERCEIVES +","+ sensor +"," + objectName + " , ?l, ?t, 1 >>");
                infons.add(i);
            }

        }

        return infons;
    }

    public List<String> sensorsWhichPerceivesSOA(String stateOfAffairName) {
        List<String> sensorsNames = new ArrayList<>();


        for (OWLSubClassOfAxiom op : getOntologyProcessor().getOntology().getAxioms(AxiomType.SUBCLASS_OF)) {

            if(op.getSuperClass() instanceof OWLObjectSomeValuesFrom){

                OWLClass owlClass = op.getSubClass().asOWLClass();

                OWLObjectSomeValuesFrom owlObjectSomeValuesFrom = (OWLObjectSomeValuesFrom) op.getSuperClass();
                OntologyProcessor ontologyProcessor = new OntologyProcessor();
                owlObjectSomeValuesFrom.getProperty().asOWLObjectProperty();

                if(ontologyProcessor.labelForProperty(owlObjectSomeValuesFrom.getProperty().asOWLObjectProperty()).equalsIgnoreCase(PERCEIVES)) {
                    if(ontologyProcessor.labelForClass(owlObjectSomeValuesFrom.getFiller().asOWLClass()).equalsIgnoreCase(stateOfAffairName)) {
                        String sensorName = ontologyProcessor.labelForClass(owlClass);
                        sensorsNames.add(sensorName);
                    }
                }
            }
        }

        return sensorsNames;
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

    public RelationDefinition getRelationDefinition(String relationName) {
        return this.retionDefinitions.get(relationName);
    }

    public StandardRelationDefinition getStandardRelationDefinition(String relationName) {
        return this.standardRelationDefinitions.get(relationName);
    }

    public boolean isPhenomenon(String objName) {
        return op.subClassExists(OntologyProcessor.phenomenonClass , objName);
    }

    public boolean isStateOfAffair(String objName) {
        return op.subClassExists(OntologyProcessor.stateOfAffairClass , objName);
    }

    public void addUnknownRelation(String relationName) {
        LOG.trace(">> addUnknownRelation");

        OWLClass unknownClass = op.findClass(OntologyProcessor.unknownClassName);
        OWLClass relationClass = op.findClass(OntologyProcessor.relationClassName);

        OWLClass newRelation = op.createClass(relationName);

        OWLAxiomChange c1 = op.createSubclass(newRelation,unknownClass);
        OWLAxiomChange c2 = op.createSubclass(newRelation,relationClass);

        op.applyChange(c1,c2);

        LOG.trace("<< addUnknownRelation");
    }

    public void deatchUnknownRelation(String className) {
        OWLClass unknownClass = op.findClass(OntologyProcessor.unknownClassName);
        OWLClass clazz = op.findClass(className);

        op.applyChange(op.detachParentClass(clazz,unknownClass));

    }

    public boolean isRelationUnknown(String relation) {
        return op.isRelationUnknown(relation);
    }

    public void addUnknownObject(String unknownObject) {

    }

    // FIXME
    public boolean isObjectUnknown(String object) {
        return !op.objectExists(object);
    }

    public OntologyProcessor getOntologyProcessor() {
        return op;
    }


    private void populateStandardRelations() {
        this.standardRelationDefinitions = new HashMap<>();

        StandardRelationDefinition lessThan = new StandardRelationDefinition("lessThan", "StateOfAffair", "Integer", OWLClass.class, Double.class);

        standardRelationDefinitions.put("lessThan" , lessThan);

        // TODO pozostałe relacje

    }


    public enum LogicOperator {OR, AND}


    public class RelationDefinition {
        private List<Infon> infons;
        private LogicOperator operator;
        private String name;

        public RelationDefinition(String name, LogicOperator operator, Infon ... infons) {
            this.infons = Arrays.asList(infons);
            this.operator = operator;
            this.name = name;
        }

        public List<Infon> getInfons() {
            return infons;
        }

        public LogicOperator getOperator() {
            return operator;
        }

        public String getName() {
            return name;
        }
    }


    public class StandardRelationDefinition {
        private String name;
        private String firstArg;
        private String secondArg;

        private Class firstArgType;
        private Class scondArgType;


        public StandardRelationDefinition(String name, String firstArg, String secondArg, Class firstArgType, Class scondArgType) {
            this.name = name;
            this.firstArg = firstArg;
            this.secondArg = secondArg;
            this.firstArgType = firstArgType;
            this.scondArgType = scondArgType;
        }

        public String getName() {
            return name;
        }

        public String getFirstArg() {
            return firstArg;
        }

        public String getSecondArg() {
            return secondArg;
        }

        public Class getFirstArgType() {
            return firstArgType;
        }

        public Class getScondArgType() {
            return scondArgType;
        }

        public boolean standInRelation (Object firstArg , Object secondArg) {
            Double arg1 = (Double) firstArg;
            Double arg2 = (Double) secondArg;

            return arg1 < arg2;

        }


    }

}
