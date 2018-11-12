package org.mdyk.netsim.logic.node.program.owl;


import com.google.common.eventbus.AllowConcurrentEvents;
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
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

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

    private DeviceAPI deviceAPI;
    private DeviceSimEntity deviceSimEntity;
    private int nodeId;

    /**
     * Kolekcja zapytań o informację
     */
    private Map<Integer , InformationNeedAskMessage> informationNeedAskMsgs = new HashMap<>();

    /**
     * Kolekcja określająca ile razy dana potrzeba informacyjna zaostała rozesłana.
     */
    private Map<Integer , Integer> informationNeedAskResendCount = new HashMap<>();

    private Map<Integer , String> informationNeedResponse = new HashMap<>();

    private Map<Integer , String> communicationInterfaces;
    private Map<Integer, List<Neighbour>> neighbours = new HashMap<>();

    /**
     * keys are neigbours ids
     */
    private Map<Integer, Neighbour> neighboursCombinedList = new HashMap<>();

    private HashMap <Integer , Boolean> resendResponse = new HashMap<>();

    private KnowledgeBase kb;

//    private OntologyProcessor ontologyProcessor;


    @Override
    public void initialize() {

        EventBusHolder.getEventBus().register(this);

        communicationInterfaces = deviceAPI.api_listCommunicationInterfaces();

        kb = new KnowledgeBase("Device-"+this.deviceSimEntity.getDeviceLogic().getName());

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
                        processInformationNeed(inam, true);
                        break;

                    case INFORMATION_NEED_RESP:
                        InformationNeedRespMessage inrm = (InformationNeedRespMessage) inm;
                        handleInformationNeedResponse(inrm);
                        break;

                }

                return null;
            }
        });
        this.start();
    }

    public void responseForTopologyDiscovery(int commInterface, TopologyDiscoveryMessage tdm) {
        LOG.trace(">> responseForTopologyDiscovery tdm = " + tdm.toString());
        actualizeNeighbours();
        GeoPosition position = (GeoPosition) deviceAPI.api_getPosition();
        TopologyDiscoveryResponseMessage topologyDiscoveryResponseMessage = new TopologyDiscoveryResponseMessage(deviceAPI.api_getMyID() , position, tdm.getNeedId());
        deviceAPI.api_sendMessage(nextMsgId(),nodeId , tdm.getSourceNode() , commInterface, topologyDiscoveryResponseMessage.toJSON(), topologyDiscoveryResponseMessage.getSize());
        LOG.trace("<< responseForTopologyDiscovery");
    }

    public void handleTopologyDiscoveryResp(TopologyDiscoveryResponseMessage tdrm){
        LOG.trace(">> handleTopologyDiscoveryResp tdrm = " + tdrm.toString());                 
        this.updateNeighbourPosition(tdrm.getNodeId(),tdrm.getPosition());

        // Rozesłanie tylko jeśli była taka potrzeba
       // this.resendInformationNeed(tdrm.getNodeId(), tdrm.getNeedId());

        LOG.trace("<< handleTopologyDiscoveryResp");
    }

    public void handleInformationNeedResponse(InformationNeedRespMessage inrm) {
        LOG.trace(">> handleInformationNeedResponse");

        // 1. zweryfikowanie czy jestem odbiorcą
        if(inrm.getSourceNode() == this.getId()) {
            // oznaczenie że udzielono odpowiedzi na potrzebę
            this.informationNeedResponse.put(inrm.getId(), inrm.toJSON());

            Infon respInfon = inrm.getInfon();
            LOG.info("Reponse Infon = " + respInfon);
            // Zapisanie odpowiedzi w BD.
            kb.populateKB(respInfon);

        }


        // 2. jesli nie jestem odbiorcą to wyszukanie obsłużonych potrzeb i wybranie z nich odbiorcy

        LOG.trace("<< handleInformationNeedResponse");
    }

    public void handleInformationNeedAsk(InformationNeedAskMessage inam) {
        LOG.trace(">> handleInformationNeedAsk");
        // TODO
        LOG.trace("<< handleInformationNeedAsk");
    }

    public void setDeviceAPI(DeviceAPI api) {
        this.deviceAPI = api;
    }

    public void setDeviceSimEntity(DeviceSimEntity simEntity) {
        this.deviceSimEntity = simEntity;
        this.nodeId = this.deviceSimEntity.getDeviceLogic().getID();
//        soldierName = "Soldier_"+deviceSimEntity.getDeviceLogic().getID();
//        deviceName = "Device_"+deviceSimEntity.getDeviceLogic().getID();
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
        //updateOntologyDataProperties();
    }

    public void loadOntology(File ontologyFile , String ontologyIRI) throws OWLOntologyCreationException {
        kb.loadOntology(ontologyFile,ontologyIRI);
        kb.saveKBSnapshot(this.deviceSimEntity.getSimTime());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleEvents(InternalEvent event) {
        try {
            switch (event.getEventType()) {
                case ANSWER_INFORMATION_NEED:
                    Pair<Integer, String> informationNeed = (Pair<Integer, String>) event.getPayload();
                    // The program should be installed in current node.
                    if (informationNeed.getKey() != null && informationNeed.getKey().equals(nodeId)) {
                        LOG.debug(informationNeed.getValue());
//                    this.informationNeeds.put(informationNeed.getValue().hashCode() , new InformationNeedContent(informationNeed.getKey() , informationNeed.getValue()));
                        Infon infon = new Infon(informationNeed.getValue());

                        InformationNeedAskMessage askMessage = new InformationNeedAskMessage(this.nodeId, infon);
                        processInformationNeed(askMessage, true);
                    }

                    break;
            }
        } catch(Exception exc) {
            LOG.error(exc.getMessage(), exc);
        }
    }

    private void processInformationNeed(InformationNeedAskMessage informationNeedAsk, boolean verify) {
        informationNeedAsk.processedInNode(this.nodeId);
        this.informationNeedAskMsgs.put(informationNeedAsk.getId() , informationNeedAsk);
        this.informationNeedAskResendCount.put(informationNeedAsk.getId(), 0);
        
        if(verify) {
            List<INProcessStatus> inProcessStatus = verifyInformationNeed(informationNeedAsk);

            if(inProcessStatus.contains(INProcessStatus.UPDATE_TOPOLOGY)) {
                topologyDiscovery(informationNeedAsk);
            }

            if(inProcessStatus.contains(INProcessStatus.RESEND)) {
                resendInformationNeed(informationNeedAsk.getId());
            }

            if(inProcessStatus.contains(INProcessStatus.ASK_FOR_RELATION)) {
                String relation = informationNeedAsk.getInfon().getRelation();

                Infon relAskInfon = new Infon("<<"+KnowledgeBase.UNKNOWN_RELATION+","+relation+",?l,?t,1>>");
                InformationNeedAskMessage relationUnknownNeed = new InformationNeedAskMessage(this.nodeId, relAskInfon);
                relationUnknownNeed.processedInNode(this.nodeId);
                for (Integer neighbourId : neighboursCombinedList.keySet()) {
                    Neighbour neighbour = neighboursCombinedList.get(neighbourId);
                    deviceAPI.api_sendMessage(this.nextMsgId(), deviceAPI.api_getMyID(), neighbour.getId(), neighbour.commInt, relationUnknownNeed.toJSON(), relationUnknownNeed.getSize());
                }
            }

            if(inProcessStatus.contains(INProcessStatus.PROCESS_IN_NODE)) {
                respondForInformationNeed(informationNeedAsk);
            }

        }
        //resendInformationNeed(informationNeedAsk);
    }

    // Publiczne na potrzeby testów
    public List<INProcessStatus> verifyInformationNeed(InformationNeedAskMessage informationNeedAsk) {

        List<INProcessStatus> inProcessStatus = new ArrayList<>();

        // Verify if relation is known
        String relation = informationNeedAsk.getInfon().getRelation();
        boolean relationExists = kb.getOntologyProcessor().relationExists(relation);

        List<String> unknownObjects = new ArrayList<>();
        for(String object : informationNeedAsk.getInfon().getObjects()) {
            if(!kb.getOntologyProcessor().objectExists(object)) {
                unknownObjects.add(object);
            }
        }

        //FIXME uogólnienie warunku tak żeby nie musieć w sposób  szczególny traktować relacji unknown
        if ((relationExists && unknownObjects.size() == 0) || relation.equalsIgnoreCase(KnowledgeBase.UNKNOWN_RELATION)) {
            // Procesowanie potrzeby
            //respondForInformationNeed(informationNeedAsk);
            inProcessStatus.add(INProcessStatus.PROCESS_IN_NODE);
            inProcessStatus.add(INProcessStatus.RESEND);
            if(!informationNeedAsk.getInfon().isSpatialLocationParam()) {
                inProcessStatus.add(INProcessStatus.UPDATE_TOPOLOGY);
                inProcessStatus.add(INProcessStatus.RESEND);
            }

        }
        else if (!relationExists && unknownObjects.size() == 0) {
            // Zapytanie o relację. Dodoanie jej do ontologii w gałęzi unknown
//            Infon relAskInfon = new Infon("<<"+KnowledgeBase.UNKNOWN_RELATION+","+relation+",?l,t,1>>");
//            InformationNeedAskMessage relationUnknownNeed = new InformationNeedAskMessage(this.nodeId, relAskInfon);
//            processInformationNeed(relationUnknownNeed, false);

            inProcessStatus.add(INProcessStatus.ASK_FOR_RELATION);
            if(!informationNeedAsk.getInfon().isSpatialLocationParam()) {
                inProcessStatus.add(INProcessStatus.UPDATE_TOPOLOGY);
                inProcessStatus.add(INProcessStatus.RESEND);
            }

        }
        else if (relationExists && unknownObjects.size() > 0) {
            // Wysłanie zapytania z prośbą o wyjaśnienie obiektów. Dodanie obiektów do gałęzi unknown
            inProcessStatus.add(INProcessStatus.ASK_FOR_OBJECTS);

            if(!informationNeedAsk.getInfon().isSpatialLocationParam()) {
                inProcessStatus.add(INProcessStatus.UPDATE_TOPOLOGY);
                inProcessStatus.add(INProcessStatus.RESEND);
            }
        }
        else if (!relationExists && unknownObjects.size() > 0) {
            // Wysłanie zapytania z prośbą o wyjaśnienie relacji i obiektów. Dodanie obu do gałęzi unknown
//            Infon relAskInfon = new Infon("<<"+KnowledgeBase.UNKNOWN_RELATION+","+relation+",?l,t,1>>");
//            InformationNeedAskMessage relationUnknownNeed = new InformationNeedAskMessage(this.nodeId, relAskInfon);
//            // Nie ma weryfikacji bo urządzenie na pewno nie zna odpowiedzi na to żądanie
//            processInformationNeed(relationUnknownNeed, false);
            inProcessStatus.add(INProcessStatus.ASK_FOR_OBJECTS);
            inProcessStatus.add(INProcessStatus.ASK_FOR_RELATION);

            if(!informationNeedAsk.getInfon().isSpatialLocationParam()) {
                inProcessStatus.add(INProcessStatus.UPDATE_TOPOLOGY);
                inProcessStatus.add(INProcessStatus.RESEND);
            }
        }

        return inProcessStatus;

    }

    private void respondForInformationNeed(InformationNeedAskMessage inam) {
        // relacja i obiekty są znane

        InformationNeedRespMessage inrm = null;

        //FIXME obsługa isUnknown (do uspójnienia z innymi relacjami)
        if(inam.getInfon().getRelation().equalsIgnoreCase(KnowledgeBase.UNKNOWN_RELATION)) {
            //FIXME na razie odesłanie informacji o tym że relacja/obiekt nie są znane
            Infon relRespInfon = new Infon(inam.getInfon());
            inrm = new InformationNeedRespMessage(this.nodeId,inam.getId(),relRespInfon);

            // Ustalenie we własnej bazie wiedzy jak odpoweidzieć

            // Sparwdzenie czy wystepuje takie pojęcie w KB

        }

        if(inrm != null) {
            sendInformationNeedResponse(inrm, inam);
        }

        //TODO Przypadek kiedy trzeba przeslac zapytanie dalej:


    }

    private void sendInformationNeedResponse(InformationNeedRespMessage inrm, InformationNeedAskMessage inam) {
        this.actualizeNeighbours();

        // Wysyłka do nadawcy żądania
        if(neighboursCombinedList.keySet().contains(inrm.getSourceNode())) {
            Neighbour sender = neighboursCombinedList.get(inrm.getSourceNode());
            deviceAPI.api_sendMessage(this.nextMsgId(),deviceAPI.api_getMyID(),sender.getId(), sender.commInt,inrm.toJSON(), inrm.getSize());
        }

        if(inam != null) {
            for (Integer processedNode : inam.getProcessedInNodes()) {
                Neighbour n = neighboursCombinedList.get(processedNode);

                if (n != null) {
                    deviceAPI.api_sendMessage(this.nextMsgId(), deviceAPI.api_getMyID(), n.getId(), n.commInt, inrm.toJSON(), inrm.getSize());
                }
            }
        } else {
            for (Integer neighbourId : neighboursCombinedList.keySet()) {
                Neighbour neighbour = neighboursCombinedList.get(neighbourId);
                deviceAPI.api_sendMessage(this.nextMsgId(), deviceAPI.api_getMyID(), neighbour.getId(), neighbour.commInt, inrm.toJSON(), inrm.getSize());
            }
        }

//        for(Integer commInt : this.neighbours.keySet()) {
//            if(sendingFinished) break;
//            for(Neighbour n : this.neighbours.get(commInt)) {
//
//                if(n.getId() == inam.getSourceNode()) {
//                    deviceAPI.api_sendMessage(this.nextMsgId(),deviceAPI.api_getMyID(),n.getId(), commInt,inrm.toJSON(), inrm.getSize());
//                    sendingFinished = true;
//                    break;
//                }
//
//            }
//        }
    }

    private void resendInformationNeed(int inamId) {
        for(Integer nId : neighboursCombinedList.keySet()) {
            resendInformationNeed(nId,inamId);
        }
    }


    private void resendInformationNeed(int neighbourId, int inamId) {

        InformationNeedAskMessage informationNeedAsk = this.informationNeedAskMsgs.get(inamId);

        List<GeoPosition> needArea = new ArrayList<>();
        // Określa czy przy rozsyłaniu potrzeby ma być uwzględnione położenie
        boolean areaImportant = false;
        if(!informationNeedAsk.getInfon().isSpatialLocationParam()) {
            needArea = PositionParser.parsePositionsList(informationNeedAsk.getInfon().getSpatialLocation());
            areaImportant = true;
        }

        //FIXME do poprawy tak żeby nie trzeba było iterowac po wszystkich sasiadach
        for(Integer commInt : this.neighbours.keySet()) {
            for(Neighbour n : this.neighbours.get(commInt)) {
                if (n.getId() == neighbourId  && !informationNeedAsk.wasProcessedBy(n.getId())) {
                    // FIXME sprawdzenie pozycji jest nadmiarowe. DO poprawy proces uzyskiwania położenia od sąsiadów.
                    if(areaImportant && n.getPosition() != null && !Functions.isPointInRegion(n.getPosition() , needArea) ) {
                        continue;
                    }
                    deviceAPI.api_sendMessage(this.nextMsgId(),deviceAPI.api_getMyID(),n.getId(), commInt,informationNeedAsk.toJSON(), informationNeedAsk.getSize());
                    int count = this.informationNeedAskResendCount.get(informationNeedAsk.getId());
                    this.informationNeedAskResendCount.put(informationNeedAsk.getId(), count);
                }
            }
        }
    }

    private boolean isDeviceInNeedArea(GeoPosition devicePosition, InformationNeedAskMessage inam) {
        List<GeoPosition> needArea = PositionParser.parsePositionsList(inam.getInfon().getSpatialLocation());
        return Functions.isPointInRegion(devicePosition , needArea);
    }

    @Deprecated
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
    @Deprecated
    public Map<Integer, List<Integer>> checkKnownDevicesInArea(Map<Integer, List<Integer>> knownDevices, InformationNeedContent informationNeedContent) {

        String spatialLocation = informationNeedContent.getInfon().getSpatialLocation();

        // TODO weryfikacja w jakiej formie jest lokalizacja. MOże być w postaci punktow lat lon lub konkretnego obiektu/nazwanego obszaru

        // Przypadek kiedy są to punkty lat/lon
        // (52.231073#21.007506);(52.230797#21.008139);(52.230488#21.007726);(52.230554;21.007243);(52.23079#21.007115)

        List<GeoPosition> geoPosition =  parseLatLonLocation(spatialLocation);

        Functions.isPointInRegion((GeoPosition) this.deviceAPI.api_getPosition(), geoPosition);

//        topologyDiscovery();


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
    public void topologyDiscovery(InformationNeedAskMessage informationNeedAsk) {
        this.actualizeNeighbours();
        sendPositionQuery(informationNeedAsk);

//        deviceAPI.api_stayIdleFor(2);


//        boolean wait = true;
//        int count = 1;
//
//        LOG.debug("--- Waiting for position from neighbours [simTime="+deviceSimEntity.getSimTime()+"]" );
//
//        while (wait && count < 40) {
//            for(Integer commInt : neighbours.keySet()) {
//                for(Neighbour n : this.neighbours.get(commInt)) {
//                    wait &= (n.getPosition() == null);
//                }
//            }
//
////            // FIXME dodac opoznienie symulacyjne
            try {
                Thread.sleep(2000);
//                count ++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//
//        }
//
//        LOG.debug("--- Position from all neighbours received [simTime="+deviceSimEntity.getSimTime()+"]");

    }

    private void sendPositionQuery(InformationNeedAskMessage informationNeedAsk) {
        TopologyDiscoveryMessage tdm = new TopologyDiscoveryMessage(this.nodeId, informationNeedAsk.getId());

        for(Integer commInt : communicationInterfaces.keySet()) {
            for(Neighbour n : this.neighbours.get(commInt)) {
                if(!informationNeedAsk.wasProcessedBy(n.getId())) {
                    deviceAPI.api_sendMessage(msgId++, deviceAPI.api_getMyID(), n.getId(), commInt, tdm.toJSON(), tdm.getSize());
                }
            }
        }

    }

    public void actualizeNeighbours() {

        neighboursCombinedList.clear();

        for(Integer commInt : communicationInterfaces.keySet()) {
            List<Integer> neighboursIds = deviceAPI.api_scanForNeighbors(commInt);
            List<Neighbour> neighbours = new ArrayList<>();

            for(Integer id : neighboursIds) {
                Neighbour neighbour = new Neighbour(id , null, commInt);
                neighbours.add(neighbour);
                this.neighboursCombinedList.put(neighbour.getId(),neighbour);
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
        }
    }

    private int nextMsgId() {
        msgId++;
        return nodeId * 10000 + msgId;
    }

    public KnowledgeBase getKb() {
        return this.kb;
    }

    private class Neighbour {
        int id;
        GeoPosition position;
        Integer commInt;

        public Neighbour(int id, GeoPosition position, Integer commInt) {
            this.id = id;
            this.position = position;
            this.commInt = commInt;
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
