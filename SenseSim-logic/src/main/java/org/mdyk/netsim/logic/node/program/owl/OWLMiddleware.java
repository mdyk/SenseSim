package org.mdyk.netsim.logic.node.program.owl;


import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
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
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        updateOntologyDataProperties();

        try {
            manager.saveOntology(ontology, new SystemOutDocumentTarget());
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }
}
