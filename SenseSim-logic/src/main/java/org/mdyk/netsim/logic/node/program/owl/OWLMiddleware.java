package org.mdyk.netsim.logic.node.program.owl;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
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

        OWLClass soldierClass = df.getOWLClass(IRI.create(ontologyIRI,"#Soldier"));
        OWLNamedIndividual soldier = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#Soldier_"+deviceSimEntity.getDeviceLogic().getID()));
        OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(soldierClass , soldier);
        manager.addAxiom(ontology , classAssertion);
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


}
