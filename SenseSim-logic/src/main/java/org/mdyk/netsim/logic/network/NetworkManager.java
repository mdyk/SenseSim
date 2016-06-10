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
import org.mdyk.netsim.mathModel.network.GraphEdge;
import org.mdyk.netsim.mathModel.network.NetworkGraph;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class NetworkManager<P extends Position> {

    private static final Logger LOG = Logger.getLogger(NetworkManager.class);
    private List<DeviceNode<P>> sensorNodeList;

    // Map is actualized only when neighborhood changes
    private Map<Integer , List<DeviceNode<?>>> neighborhood;

    @Inject
    private NetworkGraph networkGraph;

    public NetworkManager() {
        sensorNodeList = new ArrayList<>();
        neighborhood = new HashMap<>();
        EventBusHolder.getEventBus().register(this);
    }

    public void addNode(DeviceNode<P> sensorNode) {
        sensorNodeList.add(sensorNode);
        networkGraph.addVertex(sensorNode);
        neighborhood.put(sensorNode.getID(), new LinkedList<>());
        EventBusHolder.getEventBus().post(EventFactory.createNewNodeEvent(sensorNode));
        actualizeNaighbours(sensorNode);
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
        LOG.debug(">> processEvent");
        switch(event.getEventType()){
            case NEW_NODE:
                LOG.debug("NEW_NODE event");
                DeviceNode sensorNode = (DeviceNode) event.getPayload();
                actualizeNaighbours(sensorNode);
                break;
            case NODE_POSITION_CHANGED:
                LOG.debug("NODE_POSITION_CHANGED event");
                actualizeNaighbours((DeviceNode) event.getPayload());
                break;
        }
        LOG.debug("<< processEvent");
    }

    public void actualizeNaighbours(DeviceNode changedSensor) {
        LOG.debug(">> actualizeNaighbours [device "+changedSensor.getID()+"]");

        for (DeviceNode sensor : sensorNodeList) {

            if(sensor == changedSensor){
                continue;
            }

            double distance = Functions.calculateDistance(changedSensor.getPosition(), sensor.getPosition());
            LOG.debug("distance = " + distance + " radio range: " + changedSensor.getRadioRange());
            if(distance <= Math.min(changedSensor.getRadioRange(), sensor.getRadioRange())) {
                if(!networkGraph.hasEdge(sensor,changedSensor)) {
                    networkGraph.addEdge(changedSensor,sensor);
                    LOG.debug("adding edge: [" + changedSensor.getID() + ";" + sensor.getID() + "]");
                    // TODO typowanie nie powinno odbywać się tutaj
                    EventBusHolder.getEventBus().post(new InternalEvent(EventType.EDGE_ADDED, new GraphEdge<GeoPosition>(changedSensor,sensor)));
                }
            }
            else {
                if(networkGraph.hasEdge(sensor,changedSensor)) {
                    networkGraph.removeEdge(sensor,changedSensor);
                    LOG.debug("removing edge: [" + changedSensor.getID() + ";" + sensor.getID() + "]");
                    // TODO typowanie nie powinno odbywać się tutaj
                    EventBusHolder.getEventBus().post(new InternalEvent(EventType.EDGE_REMOVED, new GraphEdge<GeoPosition>(changedSensor,sensor)));
                }
            }
        }

//        neighborhood.get(changedSensor.getID()).clear();
//
//        // TODO: ta konwersja powinna się odbywać w inny sposób (najlepiej bez iterowania za każdym razem po liście)
//        for(IDeviceModel sensorModel : networkGraph.listNeighbors(changedSensor)) {
//            neighborhood.get(changedSensor.getID()).add((DeviceNode) sensorModel);
//            // To samo trzeba zrobić w drugą stronę
//            if(!neighborhood.get(sensorModel.getID()).contains(changedSensor)) {
//                neighborhood.get(sensorModel.getID()).add(changedSensor);
//            }
//        }

        LOG.debug("<< actualizeNaighbours");
    }

    public List<IDeviceModel> getNeighborhood(DeviceNode<?> sensorNode) {
        return Optional.ofNullable(networkGraph.listNeighbors(sensorNode)).orElse(new ArrayList<>());
    }

    public void clearNodes() {
        sensorNodeList.clear();
        neighborhood.clear();
        networkGraph.clear();
    }

}

