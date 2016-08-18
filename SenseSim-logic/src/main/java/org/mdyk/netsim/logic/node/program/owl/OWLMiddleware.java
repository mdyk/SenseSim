package org.mdyk.netsim.logic.node.program.owl;


import aterm.ATermAppl;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.clarkparsia.pellet.sparqldl.engine.QueryEngine;
import com.clarkparsia.pellet.sparqldl.model.Query;
import com.clarkparsia.pellet.sparqldl.model.QueryResult;
import com.clarkparsia.pellet.sparqldl.model.ResultBinding;
import com.clarkparsia.pellet.sparqldl.parser.QueryParser;
import com.google.common.eventbus.Subscribe;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.infon.Infon;
import org.mdyk.netsim.logic.infon.message.InformationNeedContent;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.InferenceType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Middleware for devices with ontology capabilities.
 */
public class OWLMiddleware extends Thread implements Middleware {

    private static final Logger LOG = Logger.getLogger(OWLMiddleware.class);
    // TODO do przeniesienia do innej klasy odpowiedzialnej za wykonywanie zapytań
    String	prefix;
    QueryParser queryParser = QueryEngine.getParser();
    private DeviceAPI deviceAPI;
    private DeviceSimEntity deviceSimEntity;
    private int nodeId;
    private OWLOntology ontology;
    private String ontologyIRI;
    private OWLDataFactory df = OWLManager.getOWLDataFactory();
    private OWLOntologyManager manager;
    // keys are  ids of information need
    private Map<Integer , Infon> informationNeeds = new HashMap<>();
    private Map<Integer , String> informationNeedResponse = new HashMap<>();
    private String soldierName;
    private OWLNamedIndividual soldier;
    private String deviceName;
    private OWLNamedIndividual device;

    // TODO lista znanych przez urządzenie obiektów

    private void populateIndyviduals() {

        // Dodanie do ontologii informacji o obserwowanym żołnierzu
        OWLClass soldierClass = df.getOWLClass(IRI.create(ontologyIRI,"#BiologicalObject"));
        soldier = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#"+soldierName));
        OWLClassAssertionAxiom soldierClassAssertion = df.getOWLClassAssertionAxiom(soldierClass , soldier);
        manager.addAxiom(ontology , soldierClassAssertion);

        OWLClass deviceClass = df.getOWLClass(IRI.create(ontologyIRI,"#Device"));
        device = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#"+deviceName));
        OWLClassAssertionAxiom deviceClassAssertion = df.getOWLClassAssertionAxiom(deviceClass , device);
        manager.addAxiom(ontology , deviceClassAssertion);

        OWLObjectProperty diagnosedByDeviceProp = df.getOWLObjectProperty(IRI.create(ontologyIRI + "#Object_diagnosed-by_Device"));
        OWLObjectPropertyAssertionAxiom diagnosedByDeviceAssertion = df.getOWLObjectPropertyAssertionAxiom(diagnosedByDeviceProp, soldier, device);
        manager.addAxiom(ontology , diagnosedByDeviceAssertion);


        // Dodanie do ontologii informacji o sensorach urządzenia
        List<SensorModel<?,?>> sensors = deviceAPI.api_getSensorsList();

        for(SensorModel sm : sensors) {
            OWLClass sensorClass = df.getOWLClass(IRI.create(ontologyIRI,"#"+sm.getName()));
            OWLNamedIndividual sensorIndividual = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#"+sm.getName()+"_"+deviceSimEntity.getDeviceLogic().getID()));
            OWLClassAssertionAxiom sensorAssertion = df.getOWLClassAssertionAxiom(sensorClass , sensorIndividual);
            manager.addAxiom(ontology , sensorAssertion);

//            OWLObjectProperty monitoredBySensorProp = df.getOWLObjectProperty(IRI.create(ontologyIRI + "#Object_monitored-by_Sensor"));
//            OWLObjectPropertyAssertionAxiom monitoredBySensorAssertion = df.getOWLObjectPropertyAssertionAxiom(monitoredBySensorProp, soldier, sensorIndividual);
//            manager.addAxiom(ontology , monitoredBySensorAssertion);

            OWLObjectProperty deviceSensorProp = df.getOWLObjectProperty(IRI.create(ontologyIRI + "#Device_contains_Sensor"));
            OWLObjectPropertyAssertionAxiom deviceSensorAssertion = df.getOWLObjectPropertyAssertionAxiom(deviceSensorProp, device, sensorIndividual);
            manager.addAxiom(ontology , deviceSensorAssertion);

        }

        double rand = Math.random();

        String objectState;

        if(rand <= 0.3) {
            objectState = "immediate";
        } else if (rand > 0.3 &&  rand <= 0.6 ) {
            objectState = "delayed";
        } else {
            objectState = "ok";
        }

        OWLClass objectStateClass = df.getOWLClass(IRI.create(ontologyIRI,"#ObjectState"));
        OWLNamedIndividual objectStateInd = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#"+objectState));
        OWLClassAssertionAxiom objectStateClassAssertion = df.getOWLClassAssertionAxiom(objectStateClass , objectStateInd);
        manager.addAxiom(ontology , objectStateClassAssertion);


    }

    @Override
    public void initialize() {

        EventBusHolder.getEventBus().register(this);

        manager = OWLManager.createOWLOntologyManager();

        deviceAPI.api_setOnMessageHandler(new Function<Message, Object>() {
            @Override
            public Object apply(Message message) {

                Object messageContent = message.getMessageContent();

                if(messageContent instanceof InformationNeedContent){
                    InformationNeedContent informationNeedContent = (InformationNeedContent) messageContent;
                    if (informationNeeds.containsKey(informationNeedContent.getInformationNeedString().hashCode())) {
                        Infon infon = new Infon(informationNeedContent.getInformationNeedString());
                        informationNeeds.put(informationNeedContent.getInformationNeedString().hashCode() , infon);
                        processInformationNeed(informationNeedContent.getInformationNeedString().hashCode());
                    }
                }

                return null;
            }
        });
        this.start();
    }

    private void processInformationNeed(int informationNeedId) {
        LOG.trace(">> processInformationNeed");

        Infon needInfon = this.informationNeeds.get(informationNeedId);

        // TODO obsluga pozostalych przypadkow
        if (needInfon.isRelationParam() &&
                !needInfon.isPolarityParam() &&
                !needInfon.isAreObjectsParam() &&
                !needInfon.isSpatialLocationParam() &&
                !needInfon.isTemporalLocationParam()) {

            // sprawdzenie czy relacja jest znana
            if (ontology.containsClassInSignature(IRI.create(ontologyIRI , "#"+needInfon.getRelationParam().getRelationType()))) {
                // sprawdzenie czy znane sa obiekty
                // TODO sprawdzenie wszystkich obiektów z infonów
                if (needInfon.getObjects().get(0).equals(soldierName)) {
//                    QueryExecution qe = SparqlDLExecutionFactory.create( q, m );

                    PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner( ontology );

                    String queryString = queryString = "SELECT distinct ?ind \n"
                                                        + "WHERE { ?ind rdf:type ont:"+needInfon.getRelationParam().getRelationType()+" }\n";;

                    Query q = queryParser.parse(prefix + queryString , reasoner.getKB());
                    QueryResult qr = QueryEngine.exec(q);

                    if (qr.size() == 1 ) {
                        ResultBinding resultBinding = qr.iterator().next();
                        ATermAppl aTermAppl = qr.getResultVars().get(0);
                        String result = resultBinding.getValue(aTermAppl).getName();
                        this.informationNeedResponse.put(informationNeedId , result);
                    }


                }

            } else {
                // TODO
            }

            // wykoanie zapytania do ontologii

        }


//        PelletReasoner
        PelletReasoner reasoner = PelletReasonerFactory.getInstance().createNonBufferingReasoner(ontology);
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

//        reasoner.
        LOG.trace("<< processInformationNeed");
    }


    public void setDeviceAPI(DeviceAPI api) {
        this.deviceAPI = api;
    }

    public void setDeviceSimEntity(DeviceSimEntity simEntity) {
        this.deviceSimEntity = simEntity;
        this.nodeId = this.deviceSimEntity.getDeviceLogic().getID();
        soldierName = "Soldier_"+deviceSimEntity.getDeviceLogic().getID();
        deviceName = "Device_"+deviceSimEntity.getDeviceLogic().getID();

    }

    @Override
    public void loadProgram(SensorProgram program) {

    }

    @Override
    public List<SensorProgram> getPrograms() {
        return null;
    }

    @Override
    public void execute() {
        updateOntologyDataProperties();
    }

    private void updateOntologyDataProperties() {
        List<SensorModel> sensors = deviceAPI.api_getSensorsList();
        for(SensorModel sm : sensors) {
            OWLNamedIndividual sensorIndividual = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#"+sm.getName()));
            ConfigurationSpace observation = deviceAPI.api_getSensorCurrentObservation(sm);
            OWLLiteral owlObservationData = df.getOWLLiteral(Double.parseDouble(observation.getStringValue()));
            OWLDataProperty valueProperty = df.getOWLDataProperty(IRI.create(ontologyIRI , "#value"));
            manager.addAxiom(ontology, df.getOWLDataPropertyAssertionAxiom(valueProperty, sensorIndividual, owlObservationData));
        }
    }

    public void loadOntology(File ontologyFile , String ontologyIRI) throws OWLOntologyCreationException {
        this.ontologyIRI = ontologyIRI;
        this.ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);

        prefix ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX ont: <"+this.ontologyIRI+"#>";

        populateIndyviduals();

        try {
            manager.saveOntology(ontology, new SystemOutDocumentTarget());
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void handleEvents(InternalEvent event) {
        switch (event.getEventType()) {
            case ANSWER_INFORMATION_NEED:
                Pair<Integer, String> informationNeed = (Pair<Integer, String>) event.getPayload();
                // The program should be installed in current node.
                if(informationNeed.getKey()!=null && informationNeed.getKey().equals(nodeId)){
                    LOG.debug(informationNeed.getValue());
                    this.informationNeeds.put(informationNeed.getValue().hashCode() , new Infon(informationNeed.getValue()));

                }
                resendInformationNeed(informationNeed);
                break;
        }
    }

    private void resendInformationNeed(Pair<Integer, String> informationNeed) {
        LOG.trace(">> resendInformationNeed");
        List<Integer> neighbours = deviceAPI.api_scanForNeighbors();

        InformationNeedContent informationNeedContent = new InformationNeedContent(informationNeed.getKey() , informationNeed.getValue());

        for (Integer neighbour : neighbours) {
            deviceAPI.api_sendMessage(informationNeedContent.getAskingNodeId(), deviceAPI.api_getMyID(), neighbour, informationNeedContent , informationNeedContent.getInformationNeedString().getBytes().length );
        }
        LOG.trace("<< resendInformationNeed");
    }

    @Override
    public void run() {

        // FIXME powinno to odbywać się jako poprawne zdarzenia symulacyjne
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Integer informationNeedId : informationNeeds.keySet()) {
            this.processInformationNeed(informationNeedId);
        }



//        updateOntologyDataProperties();
//
//        try {
//            manager.saveOntology(ontology, new SystemOutDocumentTarget());
//        } catch (OWLOntologyStorageException e) {
//            e.printStackTrace();
//        }
    }
}
