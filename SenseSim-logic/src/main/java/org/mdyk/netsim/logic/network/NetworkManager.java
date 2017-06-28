package org.mdyk.netsim.logic.network;

import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.event.EventType;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.device.DeviceNode;
import org.mdyk.netsim.mathModel.device.IDeviceModel;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.Functions;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.network.GraphEdge;
import org.mdyk.netsim.mathModel.network.NetworkGraph;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class NetworkManager<P extends Position> {

    private static final Logger LOG = Logger.getLogger(NetworkManager.class);
    private List<DeviceNode<P>> sensorNodeList;

    @Inject
    @Deprecated
    private NetworkGraph networkGraph;

    /**
     * Holds communication graphs for particular communication interfaces. Keys are identifiers of interfaces
     * and values are graphs.
     */
    private HashMap<Integer , NetworkGraph> communicationGraphs;

    public NetworkManager() {
        sensorNodeList = new ArrayList<>();
        communicationGraphs = new HashMap<>();
        EventBusHolder.getEventBus().register(this);
    }

    public void addNode(DeviceNode<P> deviceNode) {
        sensorNodeList.add(deviceNode);

        for(CommunicationInterface communicationInterface : deviceNode.getCommunicationInterfaces()) {
            if(!communicationGraphs.containsKey(communicationInterface.getId())) {
                communicationGraphs.put(communicationInterface.getId() , new NetworkGraph());
            }
        }

        EventBusHolder.getEventBus().post(EventFactory.createNewNodeEvent(deviceNode));
        actualizeNaighbours(deviceNode);
    }

    public void runNodes() {
        LOG.debug(">> runScenario");
        for (DeviceNode sensorNode : sensorNodeList) {
            LOG.info("Starting node: " + sensorNode.getID());
            sensorNode.startNode();
        }
        LOG.debug("<< runScenario");
    }

    public void pauseNodes() {
        LOG.debug(">> pauseScenario");
        for (DeviceNode sensorNode : sensorNodeList) {
            LOG.info("Pausing node: " + sensorNode.getID());
            sensorNode.pauseNode();
        }
        LOG.debug("<< pauseScenario");
    }

    public void resumeNodes() {
        LOG.debug(">> resumeScenario");
        for (DeviceNode sensorNode : sensorNodeList) {
            LOG.info("Resume node: " + sensorNode.getID());
            sensorNode.resumeNode();
        }
        LOG.debug("<< resumeScenario");
    }

    public void stopNodes() {
        LOG.debug(">> stopScenario");
        for (DeviceNode sensorNode : sensorNodeList) {
            LOG.info("Stop node: " + sensorNode.getID());
            sensorNode.stopNode();
        }
        LOG.debug("<< stopScenario");
    }

    @Subscribe
    public void processEvent(InternalEvent event) {
//        LOG.debug(">> processEvent");
        switch(event.getEventType()){
            case NEW_NODE:
                LOG.debug("NEW_NODE event");
                DeviceNode sensorNode = (DeviceNode) event.getPayload();
                actualizeNaighbours(sensorNode);
                break;
//            case NODE_POSITION_CHANGED:
//                LOG.debug("NODE_POSITION_CHANGED event");
//                actualizeNaighbours((DeviceNode) event.getPayload());
//                break;
        }
//        LOG.debug("<< processEvent");
    }

    public void actualizeNaighbours(DeviceNode changedDevice) {
        LOG.debug(">> actualizeNaighbours [device "+changedDevice.getID()+"]");
                                                        
        for (DeviceNode device : sensorNodeList) {

            if(device == changedDevice){
                continue;
            }
            
            List<CommunicationInterface> communicationInterfaces =  changedDevice.getCommunicationInterfaces();

            for(CommunicationInterface communicationInterface : communicationInterfaces) {
                // Devices have the same type of communication interfaces
                if(device.getCommunicationInterface(communicationInterface.getId()) != null){
                    LOG.debug("Device "+device.getID()+" contains communication interface " + communicationInterface.getId());

                    NetworkGraph networkGraph = this.communicationGraphs.get(communicationInterface.getId());

                    if(communicationInterface.getTopologyType() == CommunicationInterface.TopologyType.FIXED){
                        // Adding edge basing on fixed topology
                        if(communicationInterface.getConnectedDevices().contains(device.getID())
                            && device.getCommunicationInterface(communicationInterface.getId()).getConnectedDevices().contains(changedDevice.getID()) ) {
                            if (!networkGraph.hasEdge(device, changedDevice)) {
                                addEdge(networkGraph,device, changedDevice, communicationInterface);
                            }
                        }
                        // Removing edge if at least one device disconnects
                        else if(!communicationInterface.getConnectedDevices().contains(device.getID())
                                || !device.getCommunicationInterface(communicationInterface.getId()).getConnectedDevices().contains(changedDevice.getID()) ) {
                            if (networkGraph.hasEdge(device, changedDevice)) {
                                removeEdge(networkGraph , device , changedDevice , communicationInterface);
                            }
                        }
                    }

                    if(communicationInterface.getTopologyType() == CommunicationInterface.TopologyType.ADHOC) {
                        double distance = Functions.calculateDistance(changedDevice.getPosition(), device.getPosition());

                        if(distance <= Math.min(communicationInterface.getRadioRange(), device.getCommunicationInterface(communicationInterface.getId()).getRadioRange())) {
                            if(!networkGraph.hasEdge(device,changedDevice)) {
                                addEdge(networkGraph,device, changedDevice, communicationInterface);
                            }
                        }
                        else {
                            if(networkGraph.hasEdge(device,changedDevice)) {
                                removeEdge(networkGraph , device , changedDevice , communicationInterface);
                            }
                        }
                    }
                }
            }
        }
        
        LOG.debug("<< actualizeNaighbours");
    }


    private void addEdge(NetworkGraph networkGraph, DeviceNode device, DeviceNode changedDevice, CommunicationInterface communicationInterface) {
        LOG.debug("adding edge: [" + changedDevice.getID() + ";" + device.getID() + "]");
        networkGraph.addEdge(changedDevice,device);
        // TODO typowanie nie powinno odbywać się tutaj
        HashMap<Integer, GraphEdge<?>> payload = new HashMap<>();
        payload.put(communicationInterface.getId(), new GraphEdge<>(changedDevice, device));
        EventBusHolder.getEventBus().post(new InternalEvent(EventType.EDGE_ADDED,payload));
    }

    private void removeEdge(NetworkGraph networkGraph, DeviceNode device, DeviceNode changedDevice, CommunicationInterface communicationInterface) {
        LOG.debug("removing edge: [" + changedDevice.getID() + ";" + device.getID() + "]");
        networkGraph.removeEdge(device,changedDevice);
        // TODO typowanie nie powinno odbywać się tutaj
        HashMap<Integer, GraphEdge<?>> payload = new HashMap<>();
        payload.put(communicationInterface.getId(), new GraphEdge<>(changedDevice, device));
        EventBusHolder.getEventBus().post(new InternalEvent(EventType.EDGE_REMOVED,payload));
    }

    @Deprecated
    public List<IDeviceModel> getNeighborhood(DeviceNode<?> sensorNode) {
        return Optional.ofNullable(networkGraph.listNeighbors(sensorNode)).orElse(new ArrayList<>());
    }

    public List<IDeviceModel> getNeighborhood(DeviceNode<?> sensorNode , int commIntId) {
        return Optional.ofNullable(this.communicationGraphs.get(commIntId).listNeighbors(sensorNode)).orElse(new ArrayList<>());
    }

    public void clearNodes() {
        sensorNodeList.clear();
        this.communicationGraphs.clear();
    }

}

