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
import org.mdyk.netsim.logic.infon.message.ResponseForNeedContent;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.program.owl.messages.*;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.logic.util.PositionParser;
import org.mdyk.netsim.mathModel.Functions;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Middleware for devices with ontology capabilities.
 */
public class OWLMiddleware extends Thread implements Middleware {

    private static final Logger LOG = Logger.getLogger(OWLMiddleware.class);
    private static int msgId = 0;
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
    @Deprecated
    private Map<Integer , InformationNeedContent> informationNeeds = new HashMap<>();

    private Map<Integer , InformationNeedAskMessage> informationNeedAskMsgs = new HashMap<>();

    private Map<Integer , String> informationNeedResponse = new HashMap<>();
    private String soldierName;
    private OWLNamedIndividual soldier;
    private String deviceName;
    private OWLNamedIndividual device;
    private Map<Integer , String> communicationInterfaces;
    private Map<Integer, List<Neighbour>> neighbours = new HashMap<>();

    private HashMap <Integer , Boolean> resendResponse = new HashMap<>();

    // TODO lista znanych przez urządzenie obiektów

    private void populateIndyviduals() {

        // Dodanie do ontologii informacji o obserwowanym żołnierzu
        OWLClass soldierClass = df.getOWLClass(IRI.create(ontologyIRI,"#BiologicalObjNect"));
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
            objectState = "minimal";
        }

//        objectState = "ok";

        OWLClass objectStateClass = df.getOWLClass(IRI.create(ontologyIRI,"#ObjectState"));
        OWLNamedIndividual objectStateInd = df.getOWLNamedIndividual(IRI.create(ontologyIRI , "#"+objectState));
        OWLClassAssertionAxiom objectStateClassAssertion = df.getOWLClassAssertionAxiom(objectStateClass , objectStateInd);
        manager.addAxiom(ontology , objectStateClassAssertion);


    }

    @Override
    public void initialize() {

        EventBusHolder.getEventBus().register(this);

        communicationInterfaces = deviceAPI.api_listCommunicationInterfaces();

        manager = OWLManager.createOWLOntologyManager();

        deviceAPI.api_setOnMessageHandler(new Function<Message, Object>() {
            @Override
            public Object apply(Message message) {

                // TODO wiadomosci przesyłane w postaci JSON lub cbor. Rozpoznanie na podstawie rodzaju akcji
                Object messageContent = message.getMessageContent();
                InformationNeedMessage inm = MessageParser.parseJSON(String.valueOf(messageContent));

                switch (inm.getMessageType()) {
                    case TOPOLOGY_DISCOVERY_ASK:
                        TopologyDiscoveryMessage tdm = (TopologyDiscoveryMessage) inm;
                        responseForTopologyDiscovery(message.getCommunicationInterface(),tdm);
                        break;

                    case TOPOLOGY_DISCOVERY_RESP:
                        TopologyDiscoveryResponseMessage tdmr = (TopologyDiscoveryResponseMessage) inm;
                        handleTopologyDiscoveryResp(tdmr);
                        break;

                    case INFORMATION_NEED_ASK:
                        InformationNeedAskMessage inam = (InformationNeedAskMessage) inm;
//                        handleInformationNeedAsk(inam);
                        processInformationNeed(inam);
                        break;

                }

//                if(messageContent instanceof InformationNeedContent){
//                    InformationNeedContent informationNeedContent = (InformationNeedContent) messageContent;
//                    if (!informationNeeds.containsKey(informationNeedContent.getInformationNeedString().hashCode())) {
//                        informationNeeds.put(informationNeedContent.getInformationNeedString().hashCode() , informationNeedContent);
//                        processInformationNeed(informationNeedContent.getInformationNeedString().hashCode());
//                    }
//                }
//
//                if(messageContent instanceof ResponseForNeedContent) {
//                    ResponseForNeedContent responseForNeedContent = (ResponseForNeedContent) messageContent;
//
//                    // if device has response for current need then append received need to it and resend
//                    if(informationNeedResponse.containsKey(responseForNeedContent.getNeedId())) {
//
//                        if(!responseForNeedContent.getContent().contains(informationNeedResponse.get(responseForNeedContent.getNeedId()))) {
//                            informationNeedResponse.put(responseForNeedContent.getNeedId(), informationNeedResponse.get(responseForNeedContent.getNeedId())+" & "+responseForNeedContent.getContent());
//                            resendResponse.put(responseForNeedContent.getNeedId(), true);
//                        }
//                    }
//
//                    if (responseForNeedContent.getAskingNodeId() == nodeId) {
//                        EventBusHolder.post(EventType.INFORMATION_NEED_FULLLFILLED , responseForNeedContent);
//                    }
//                }

                return null;
            }
        });
        this.start();
    }

    public void responseForTopologyDiscovery(int commInterface, TopologyDiscoveryMessage tdm) {
        LOG.trace(">> responseForTopologyDiscovery tdm = " + tdm.toString());
        actualizeNeighbours();
        GeoPosition position = (GeoPosition) deviceAPI.api_getPosition();
        TopologyDiscoveryResponseMessage topologyDiscoveryResponseMessage = new TopologyDiscoveryResponseMessage(deviceAPI.api_getMyID() , position);
        deviceAPI.api_sendMessage(nextMsgId(),nodeId , tdm.getSourceNode() , commInterface, topologyDiscoveryResponseMessage.toJSON(), topologyDiscoveryResponseMessage.getSize());
        LOG.trace("<< responseForTopologyDiscovery");
    }

    public void handleTopologyDiscoveryResp(TopologyDiscoveryResponseMessage tdrm){
        LOG.trace(">> handleTopologyDiscoveryResp tdrm = " + tdrm.toString());
        this.updateNeighbourPosition(tdrm.getNodeId(),tdrm.getPosition());
        LOG.trace("<< handleTopologyDiscoveryResp");
    }

    public void handleInformationNeedAsk(InformationNeedAskMessage inam) {
        LOG.trace(">> handleInformationNeedAsk");
        // TODO
        LOG.trace("<< handleInformationNeedAsk");
    }

    // FIXME potrzebny poważny refactor
    private void processInformationNeed(int informationNeedId) {
        LOG.trace(">> processInformationNeed");

        Infon needInfon = this.informationNeeds.get(informationNeedId).getInfon();

        // TODO obsluga pozostalych przypadkow
        if (needInfon.isRelationParam() &&
                !needInfon.isPolarityParam() &&
                !needInfon.areObjectsParam() &&
                !needInfon.isSpatialLocationParam() &&
                !needInfon.isTemporalLocationParam()) {

            // sprawdzenie czy relacja jest znana
            if (ontology.containsClassInSignature(IRI.create(ontologyIRI, "#" + needInfon.getRelationParam().getRelationType()))) {
                // sprawdzenie czy znane sa obiekty
                // TODO sprawdzenie wszystkich obiektów z infonów
                if (needInfon.getObjects().get(0).equals(soldierName)) {
//                    QueryExecution qe = SparqlDLExecutionFactory.create( q, m );

                    PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);

                    String queryString = "SELECT distinct ?ind \n"
                            + "WHERE { ?ind rdf:type ont:" + needInfon.getRelationParam().getRelationType() + " }\n";

                    Query q = queryParser.parse(prefix + queryString, reasoner.getKB());
                    QueryResult qr = QueryEngine.exec(q);

                    if (qr.size() == 1) {
                        ResultBinding resultBinding = qr.iterator().next();
                        ATermAppl aTermAppl = qr.getResultVars().get(0);
                        String result = resultBinding.getValue(aTermAppl).getName();

                        result = result.split("#")[1];

                        Infon infonResult = new Infon(needInfon);
                        infonResult.setRelation(result);

                        if (!this.informationNeedResponse.containsKey(informationNeedId)) {
                            this.informationNeedResponse.put(informationNeedId, infonResult.toString());
                            this.resendResponse.put(informationNeedId, true);
                        } else if (infonResult.toString().hashCode() != informationNeedResponse.get(informationNeedId).hashCode()) {
                            this.informationNeedResponse.put(informationNeedId, infonResult.toString());
                            this.resendResponse.put(informationNeedId, true);
                        }
                    }
                }
            }
            // wykoanie zapytania do ontologii
        } else if (!needInfon.isRelationParam() &&
                needInfon.isPolarityParam() &&
                !needInfon.areObjectsParam() &&
                !needInfon.isSpatialLocationParam() &&
                !needInfon.isTemporalLocationParam()) {

            if (ontology.containsClassInSignature(IRI.create(ontologyIRI, "#" + needInfon.getRelationParam().getRelationType()))) {
                // sprawdzenie czy znane sa obiekty
                // TODO sprawdzenie wszystkich obiektów z infonów
                if (needInfon.getObjects().get(0).equals(soldierName)) {

                    PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);

                    String queryString = "SELECT distinct ?ind \n"
                            + "WHERE { ?ind rdf:type ont:" + needInfon.getRelationParam().getRelationType() + " }\n";

                    Query q = queryParser.parse(prefix + queryString, reasoner.getKB());
                    QueryResult qr = QueryEngine.exec(q);

                    if (qr.size() == 1) {
                        ResultBinding resultBinding = qr.iterator().next();
                        ATermAppl aTermAppl = qr.getResultVars().get(0);
                        String result = resultBinding.getValue(aTermAppl).getName();

                        result = result.split("#")[1];

                        Infon infonResult = new Infon(needInfon);

                        if (result.equalsIgnoreCase(needInfon.getRelationParam().getRelationValue())) {
                            infonResult.setPolarity("1");
                        } else {
                            infonResult.setPolarity("0");
                        }

                        if (!this.informationNeedResponse.containsKey(informationNeedId)) {
                            this.informationNeedResponse.put(informationNeedId, infonResult.toString());
                            this.resendResponse.put(informationNeedId, true);
                        } else if (infonResult.toString().hashCode() != informationNeedResponse.get(informationNeedId).hashCode()) {
                            this.informationNeedResponse.put(informationNeedId, infonResult.toString());
                            this.resendResponse.put(informationNeedId, true);
                        }
                    }

                }
            }
        } else if (!needInfon.isRelationParam() &&
                    !needInfon.isPolarityParam() &&
                    needInfon.areObjectsParam() &&
                    !needInfon.isSpatialLocationParam() &&
                    !needInfon.isTemporalLocationParam()) {

                if (ontology.containsClassInSignature(IRI.create(ontologyIRI, "#" + needInfon.getRelationParam().getRelationType()))) {
                    // sprawdzenie czy znane sa obiekty
//                    // TODO sprawdzenie wszystkich obiektów z infonów
                    // nie ma sprawdzania obiektow bo obiekty sa parametrami
//                    if (needInfon.getObjects().get(0).equals(soldierName)) {

                        boolean isObjectInRelation = false;

                        PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);

                        String queryString = "SELECT distinct ?ind \n"
                                + "WHERE { ?ind rdf:type ont:" + needInfon.getRelationParam().getRelationType() + " }\n";


                        Query q = queryParser.parse(prefix + queryString, reasoner.getKB());
                        QueryResult qr = QueryEngine.exec(q);

                        if (qr.size() == 1) {
                            ResultBinding resultBinding = qr.iterator().next();
                            ATermAppl aTermAppl = qr.getResultVars().get(0);
                            String result = resultBinding.getValue(aTermAppl).getName();

                            result = result.split("#")[1];

                            Infon infonResult = new Infon(needInfon);

                            if (result.equalsIgnoreCase(needInfon.getRelationParam().getRelationValue())) {
                                ArrayList<String> objects = new ArrayList<>();
                                objects.add(soldierName);
                                infonResult.setObjects(objects);
                                isObjectInRelation = true;
                            }

                            if(isObjectInRelation) {
                                if (!this.informationNeedResponse.containsKey(informationNeedId)) {
                                    this.informationNeedResponse.put(informationNeedId, infonResult.toString());
                                    this.resendResponse.put(informationNeedId, true);
                                }
                                // TODO aktualizacja

//                                else if (infonResult.toString().hashCode() != informationNeedResponse.get(informationNeedId).hashCode()) {
//                                    this.informationNeedResponse.put(informationNeedId, infonResult.toString());
//                                    this.resendResponse.put(informationNeedId, true);
//                                }
                            }
                        }

//                    }
                }

            }

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
    public void loadProgram(String code) {

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
//                    this.informationNeeds.put(informationNeed.getValue().hashCode() , new InformationNeedContent(informationNeed.getKey() , informationNeed.getValue()));
                    Infon infon = new Infon(informationNeed.getValue());

                    InformationNeedAskMessage askMessage = new InformationNeedAskMessage(this.nodeId, infon);

                    this.informationNeedAskMsgs.put(informationNeed.getValue().hashCode(), askMessage);
                    processInformationNeed(askMessage);
                }

                break;
        }
    }

    // TODO najlepiej gdyby na wejsciu był JSON
    private void processInformationNeed(InformationNeedAskMessage informationNeedAsk) {
        topologyDiscovery();
        resendInformationNeed(informationNeedAsk);
    }

    private void resendInformationNeed(InformationNeedAskMessage informationNeedAsk) {
        LOG.trace(">> resendInformationNeed");

//        InformationNeedContent informationNeedContent = new InformationNeedContent(informationNeed.getKey() , informationNeed.getValue());
        List<GeoPosition> needArea = PositionParser.parsePositionsList(informationNeedAsk.getInfon().getSpatialLocation());

        boolean sentToNeighbourInRange = false;
        for(Integer commInt : this.neighbours.keySet()) {
            for(Neighbour n : this.neighbours.get(commInt)) {
                if (Functions.isPointInRegion(n.getPosition() , needArea) ) {
                    deviceAPI.api_sendMessage(this.nextMsgId(),deviceAPI.api_getMyID(),n.getId(), commInt,informationNeedAsk.toJSON(), informationNeedAsk.getSize());
                    sentToNeighbourInRange = true;
                }
            }
        }

        if(!sentToNeighbourInRange) {
            for(Integer commInt : this.neighbours.keySet()) {
                for(Neighbour n : this.neighbours.get(commInt)) {
                    deviceAPI.api_sendMessage(this.nextMsgId(),deviceAPI.api_getMyID(),n.getId(), commInt,informationNeedAsk.toJSON(), informationNeedAsk.getSize());
                }
            }
        }

        LOG.trace("<< resendInformationNeed");
    }

    @Deprecated
    private void resendInformationNeedResponse(int informationNeedId, Integer askingNode , String informationNeedResponse) {
        LOG.trace(">> resendInformationNeedResponse");
        List<Integer> neighbours = deviceAPI.api_scanForNeighbors();

        ResponseForNeedContent responseContent = new ResponseForNeedContent(informationNeedId, askingNode , informationNeedResponse);

        for (Integer neighbour : neighbours) {
            deviceAPI.api_sendMessage(msgId++, deviceAPI.api_getMyID(), neighbour,responseContent , responseContent.getContent().getBytes().length );
        }
        LOG.trace("<< resendInformationNeedResponse");
    }

    /**
     * Verifies which known devices are within the area of the information need
     * @param informationNeedContent - content of the information need basing on which the device should decide whether
     * @return Map<Integer, List<Integer>> - map which keys represent id of the communication interface and values are lists of devices (ids)
     *          which are in the area of the Information Need.
     */
    public Map<Integer, List<Integer>> checkKnownDevicesINArea(Map<Integer, List<Integer>> knownDevices, InformationNeedContent informationNeedContent) {

        String spatialLocation = informationNeedContent.getInfon().getSpatialLocation();

        // TODO weryfikacja w jakiej formie jest lokalizacja. MOże być w postaci punktow lat lon lub konkretnego obiektu/nazwanego obszaru

        // Przypadek kiedy są to punkty lat/lon
        // (52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554;21.007243);(52.23079#21.007115)

        List<GeoPosition> geoPosition =  parseLatLonLocation(spatialLocation);

        Functions.isPointInRegion((GeoPosition) this.deviceAPI.api_getPosition(), geoPosition);

        topologyDiscovery();


        return null;
    }

    public List<GeoPosition> parseLatLonLocation(String spatialLocationString) {

        List<GeoPosition> geoPositions = new ArrayList<>();

        // TODO weryfikacja formatu
        String[] pointsStrings = spatialLocationString.split(";");

        for(String pointString : pointsStrings) {
            pointString = pointString.replace("(","").replace(")","");
            String[] point = pointString.split("#");

            geoPositions.add(new GeoPosition(Double.parseDouble(point[0]),Double.parseDouble(point[1])));
        }

        return geoPositions;
    }

    /**
     * Zadanie związane z odkryciem topologii i położenia węzłów
     */
    public void topologyDiscovery() {
        this.actualizeNeighbours();
        sendPositionQuery();

        boolean wait = true;
        int count = 1;

        LOG.debug("--- Waiting for position from neighbours [simTime="+deviceSimEntity.getSimTime()+"]" );

        while (wait && count < 20) {
            for(Integer commInt : neighbours.keySet()) {
                for(Neighbour n : this.neighbours.get(commInt)) {
                    wait &= (n.getPosition() == null);
                }
            }

            // FIXME dodac opoznienie symulacyjne
            try {
                Thread.sleep(500);
                count ++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        LOG.debug("--- Position from all neighbours received [simTime="+deviceSimEntity.getSimTime()+"]");

    }

    private void sendPositionQuery() {
        TopologyDiscoveryMessage tdm = new TopologyDiscoveryMessage(this.nodeId);

        for(Integer commInt : communicationInterfaces.keySet()) {
            for(Neighbour n : this.neighbours.get(commInt)) {
                deviceAPI.api_sendMessage(msgId++, deviceAPI.api_getMyID(), n.getId(), commInt, tdm.toJSON(), tdm.getSize());
            }
        }

    }

    public void actualizeNeighbours() {
        for(Integer commInt : communicationInterfaces.keySet()) {
            List<Integer> neighboursIds = deviceAPI.api_scanForNeighbors(commInt);
            List<Neighbour> neighbours = new ArrayList<>();

            for(Integer id : neighboursIds) {
                Neighbour neighbour = new Neighbour(id , null);
                neighbours.add(neighbour);
            }

            this.neighbours.put(commInt, neighbours);
        }
    }

    private void updateNeighbourPosition(int neighbourId, GeoPosition position) {
        for(Integer commInt : neighbours.keySet()) {
            for(Neighbour n : this.neighbours.get(commInt)) {
                if(n.getId() == neighbourId) {
                    n.setPosition(position);
                }
            }
        }
    }

    @Override
    public void run() {

        // FIXME powinno to odbywać się jako poprawne zdarzenia symulacyjne
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Integer informationNeedId : informationNeeds.keySet()) {
                this.processInformationNeed(informationNeedId);
                if( resendResponse.containsKey(informationNeedId) && resendResponse.get(informationNeedId)) {
                    resendInformationNeedResponse(informationNeedId,informationNeeds.get(informationNeedId).getAskingNodeId(), informationNeedResponse.get(informationNeedId));
                    resendResponse.put(informationNeedId , false);
                }
            }


//            if (Math.random() < 0.2) {
//                double rand = Math.random();
//                String objectState;
//                if (rand <= 0.3) {
//                    objectState = "immediate";
//                } else if (rand > 0.3 && rand <= 0.6) {
//                    objectState = "delayed";
//                } else {
//                    objectState = "minimal";
//                }
//
////        objectState = "ok";
//
////                OWLNamedIndividual ind = fac.getOWLNamedIndividual(IRI.create("http://www.semanticweb.org/diana/ontologies/2013/0/picture4.owl#Water1"));
////                OWLEntityRemover remover = new OWLEntityRemover(Collections.singleton(localPicture));
////                remover.visit(ind);
////// or ind.accept(remover);
////                manager.applyChanges(remover.getChanges());
//
//                OWLClass objectStateClass = df.getOWLClass(IRI.create(ontologyIRI, "#ObjectState"));
//                OWLNamedIndividual objectStateInd = df.getOWLNamedIndividual(IRI.create(ontologyIRI, "#" + objectState));
//                OWLClassAssertionAxiom objectStateClassAssertion = df.getOWLClassAssertionAxiom(objectStateClass, objectStateInd);
//                manager.addAxiom(ontology, objectStateClassAssertion);
//            }

        }





//        updateOntologyDataProperties();
//
//        try {
//            manager.saveOntology(ontology, new SystemOutDocumentTarget());
//        } catch (OWLOntologyStorageException e) {
//            e.printStackTrace();
//        }
    }

    private int nextMsgId() {
        msgId++;
        return nodeId * 10000 + msgId;
    }

    private class Neighbour {
        int id;
        GeoPosition position;

        public Neighbour(int id, GeoPosition position) {
            this.id = id;
            this.position = position;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public GeoPosition getPosition() {
            return position;
        }

        public void setPosition(GeoPosition position) {
            this.position = position;
        }
    }

}
