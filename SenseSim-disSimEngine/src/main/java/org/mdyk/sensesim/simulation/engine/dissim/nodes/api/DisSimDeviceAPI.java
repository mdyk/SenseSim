package org.mdyk.sensesim.simulation.engine.dissim.nodes.api;

import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.communication.message.SimpleMessage;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.DeviceNode;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimDeviceLogic;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimNodeEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.StartMoveActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class DisSimDeviceAPI implements DeviceAPI<GeoPosition> {

    private static final Logger LOG = Logger.getLogger(DisSimDeviceAPI.class);

    private DeviceSimEntity deviceSimEntity;

    public DisSimDeviceAPI(DeviceSimEntity deviceSimEntity) {
        this.deviceSimEntity = deviceSimEntity;
    }

    @Override
    public void setSimEntity(DeviceSimEntity deviceSimEntity) {
        this.deviceSimEntity = deviceSimEntity;
    }

    @Override
    public void api_setRoute(List<GeoPosition> route) {
        this.deviceSimEntity.getDeviceLogic().setRoute(route);
    }

    @Override
    public void api_startMove() {
        LOG.trace(">> api_startMove()");
        try {
            ((DisSimNodeEntity) deviceSimEntity).startMoveActivity = new StartMoveActivity((DisSimNodeEntity) deviceSimEntity);
            // FIXME przeprojektowanie interfejsów tak, żeby nie było konieczne rzutowanie
            ((DisSimDeviceLogic) deviceSimEntity.getDeviceLogic()).startMoveing();
        } catch (SimControlException e) {
            LOG.error(e.getMessage(),e);
        }
        LOG.trace("<< api_startMove()");
    }

    @Override
    public void api_stopMove() {
        LOG.trace(">> api_stopMove()");
        try {
            ((DisSimNodeEntity) deviceSimEntity).startMoveActivity.interrupt();
            ((DisSimNodeEntity) deviceSimEntity).endMoveActivity.interrupt();
            // FIXME przeprojektowanie interfejsów tak, żeby nie było konieczne rzutowanie
            ((DisSimDeviceLogic) deviceSimEntity.getDeviceLogic()).stopMoveing();
        } catch (SimControlException e) {
            LOG.error(e.getMessage() , e);
        }
        LOG.trace("<< api_stopMove()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void api_sendMessage(int messageId, int originSource, int originDest, Object content, Integer size) {
        Message message = new SimpleMessage(messageId, originSource, originDest , content, size);
        List<DeviceNode> neighbours =  ((DisSimDeviceLogic) deviceSimEntity.getDeviceLogic()).wirelessChannel.scanForNeighbors(deviceSimEntity.getDeviceLogic());
        List<DeviceNode<GeoPosition>> nodesToHop = deviceSimEntity.getDeviceLogic().getRoutingAlgorithm().getNodesToHop(deviceSimEntity.getDeviceLogic().getID(), originDest , message , neighbours);
        deviceSimEntity.getDeviceLogic().startCommunication(message, nodesToHop.toArray(new DeviceNode[nodesToHop.size()]));
    }

    @Override
    public Map<AbilityType, List<PhenomenonValue>> api_getObservations() {
        LOG.trace(">< api_getObservations()");
        return deviceSimEntity.getDeviceLogic().old_getObservations();
    }

    @Override
    public void api_setRoutingAlgorithm(RoutingAlgorithm<?> routingAlgorithm) {
        LOG.trace(">> api_setRoutingAlgorithm()");
        this.deviceSimEntity.getDeviceLogic().setRoutingAlgorithm(routingAlgorithm);
        LOG.trace("<< api_setRoutingAlgorithm()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> api_scanForNeighbors() {
        LOG.trace(">< api_scanForNeighbors()");

        List<DeviceNode> neighbours =  ((DisSimDeviceLogic) deviceSimEntity.getDeviceLogic()).wirelessChannel.scanForNeighbors(deviceSimEntity.getDeviceLogic());

        List<Integer> neighboursIds = new ArrayList<>();

        for (DeviceNode sensorNode : neighbours)  {
            neighboursIds.add(sensorNode.getID());
        }

        return neighboursIds;
    }

    @Override
    public GeoPosition api_getPosition() {
        LOG.trace(">< api_getPosition()");
        return new GeoPosition(deviceSimEntity.getDeviceLogic().getPosition());
    }

    @Override
    public void api_setOnMessageHandler(Function<Message, Object> handler) {
        ((DisSimDeviceLogic) deviceSimEntity.getDeviceLogic()).onMessageHandler = handler;
    }

    @Override
    public Integer api_getMyID() {
        return deviceSimEntity.getDeviceLogic().getID();
    }

    @Override
    public PhenomenonValue api_getCurrentObservation(AbilityType abilityType) {

        Map<AbilityType, List<PhenomenonValue>> obserations = deviceSimEntity.getDeviceLogic().old_getObservations();

        List<PhenomenonValue> valueList = obserations.get(abilityType);

        if (valueList.size() > 0){
            return valueList.get(valueList.size()-1);
        }   else {
            return null; // FIXME powinno zwracać NullPhenomenonValue
        }
    }
}