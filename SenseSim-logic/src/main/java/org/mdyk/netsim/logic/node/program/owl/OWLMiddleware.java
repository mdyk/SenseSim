package org.mdyk.netsim.logic.node.program.owl;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.List;
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

    private void populateIndyviduals() {

        // Dodanie do ontologii informacji o obserwowanym żołnierzu
        OWLClass soldierClass = df.getOWLClass(IRI.create(ontologyIRI,"#Soldier"));
        OWLNamedIndividual soldier = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#Soldier_"+deviceSimEntity.getDeviceLogic().getID()));
        OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(soldierClass , soldier);
        manager.addAxiom(ontology , classAssertion);

        // Dodanie do ontologii informacji o sensorach urządzenia
        List<SensorModel<?,?>> sensors = deviceAPI.api_getSensorsList();

        for(SensorModel sm : sensors) {
            OWLClass sensorClass = df.getOWLClass(IRI.create(ontologyIRI,"#"+sm.getName()));
            OWLNamedIndividual sensorIndividual = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#"+sm.getName()));
            OWLClassAssertionAxiom sensorAssertion = df.getOWLClassAssertionAxiom(sensorClass , sensorIndividual);
            manager.addAxiom(ontology , sensorAssertion);

            OWLObjectProperty hasSensor = df.getOWLObjectProperty(IRI.create(ontologyIRI + "#hasSensor"));
            OWLObjectPropertyAssertionAxiom hasSensorAssertion = df.getOWLObjectPropertyAssertionAxiom(hasSensor, soldier, sensorIndividual);
            manager.addAxiom(ontology , hasSensorAssertion);

        }

    }

    @Override
    public void initialize() {

        manager = OWLManager.createOWLOntologyManager();

        deviceAPI.api_setOnMessageHandler(new Function<Message, Object>() {
            @Override
            public Object apply(Message message) {

                Object messageContent = message.getMessageContent();

                if(messageContent instanceof String){
                    LOG.trace("messageContent is String");
                    String informationNeed = (String) messageContent;
                }

                return null;
            }
        });
        this.start();
    }

    public void setDeviceAPI(DeviceAPI api) {
        this.deviceAPI = api;
    }

    public void setDeviceSimEntity(DeviceSimEntity simEntity) {
        this.deviceSimEntity = simEntity;
        this.nodeId = this.deviceSimEntity.getDeviceLogic().getID();
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

//        try {
//            manager.saveOntology(ontology, new SystemOutDocumentTarget());
//        } catch (OWLOntologyStorageException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateOntologyDataProperties();

        try {
            manager.saveOntology(ontology, new SystemOutDocumentTarget());
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }
}
