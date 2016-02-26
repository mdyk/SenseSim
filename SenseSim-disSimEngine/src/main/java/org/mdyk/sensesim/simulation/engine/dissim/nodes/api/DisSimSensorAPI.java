package org.mdyk.sensesim.simulation.engine.dissim.nodes.api;

import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.communication.message.SimpleMessage;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorNode;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimDeviceLogic;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimNodeEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.StartMoveActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class DisSimSensorAPI implements SensorAPI<GeoPosition> {

    private static final Logger LOG = Logger.getLogger(DisSimSensorAPI.class);

    private SensorSimEntity sensorSimEntity;

    public DisSimSensorAPI(SensorSimEntity sensorSimEntity) {
        this.sensorSimEntity = sensorSimEntity;
    }

    @Override
    public void setSimEntity(SensorSimEntity sensorSimEntity) {
        this.sensorSimEntity = sensorSimEntity;
    }

    @Override
    public void api_setRoute(List<GeoPosition> route) {
        this.sensorSimEntity.getDeviceLogic().setRoute(route);
    }

    @Override
    public void api_startMove() {
        LOG.trace(">> api_startMove()");
        try {
            ((DisSimNodeEntity) sensorSimEntity).startMoveActivity = new StartMoveActivity((DisSimNodeEntity) sensorSimEntity);
            // FIXME przeprojektowanie interfejsów tak, żeby nie było konieczne rzutowanie
            ((DisSimDeviceLogic) sensorSimEntity.getDeviceLogic()).startMoveing();
        } catch (SimControlException e) {
            LOG.error(e.getMessage(),e);
        }
        LOG.trace("<< api_startMove()");
    }

    @Override
    public void api_stopMove() {
        LOG.trace(">> api_stopMove()");
        try {
            ((DisSimNodeEntity) sensorSimEntity).startMoveActivity.interrupt();
            ((DisSimNodeEntity) sensorSimEntity).endMoveActivity.interrupt();
            // FIXME przeprojektowanie interfejsów tak, żeby nie było konieczne rzutowanie
            ((DisSimDeviceLogic) sensorSimEntity.getDeviceLogic()).stopMoveing();
        } catch (SimControlException e) {
            LOG.error(e.getMessage() , e);
        }
        LOG.trace("<< api_stopMove()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void api_sendMessage(int messageId, int originSource, int originDest, Object content, Integer size) {
        Message message = new SimpleMessage(messageId, originSource, originDest , content, size);
        List<SensorNode> neighbours =  ((DisSimDeviceLogic) sensorSimEntity.getDeviceLogic()).wirelessChannel.scanForNeighbors(sensorSimEntity.getDeviceLogic());
        List<SensorNode<GeoPosition>> nodesToHop = sensorSimEntity.getDeviceLogic().getRoutingAlgorithm().getNodesToHop(sensorSimEntity.getDeviceLogic().getID(), originDest , message , neighbours);
        sensorSimEntity.getDeviceLogic().startCommunication(message, nodesToHop.toArray(new SensorNode[nodesToHop.size()]));
    }

    @Override
    public Map<AbilityType, List<PhenomenonValue>> api_getObservations() {
        LOG.trace(">< api_getObservations()");
        return sensorSimEntity.getDeviceLogic().getObservations();
    }

    @Override
    public void api_setRoutingAlgorithm(RoutingAlgorithm<?> routingAlgorithm) {
        LOG.trace(">> api_setRoutingAlgorithm()");
        this.sensorSimEntity.getDeviceLogic().setRoutingAlgorithm(routingAlgorithm);
        LOG.trace("<< api_setRoutingAlgorithm()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> api_scanForNeighbors() {
        LOG.trace(">< api_scanForNeighbors()");

        List<SensorNode> neighbours =  ((DisSimDeviceLogic) sensorSimEntity.getDeviceLogic()).wirelessChannel.scanForNeighbors(sensorSimEntity.getDeviceLogic());

        List<Integer> neighboursIds = new ArrayList<>();

        for (SensorNode sensorNode : neighbours)  {
            neighboursIds.add(sensorNode.getID());
        }

        return neighboursIds;
    }

    @Override
    public GeoPosition api_getPosition() {
        LOG.trace(">< api_getPosition()");
        return new GeoPosition(sensorSimEntity.getDeviceLogic().getPosition());
    }

    @Override
    public void api_setOnMessageHandler(Function<Message, Object> handler) {
        ((DisSimDeviceLogic) sensorSimEntity.getDeviceLogic()).onMessageHandler = handler;
    }

    @Override
    public Integer api_getMyID() {
        return sensorSimEntity.getDeviceLogic().getID();
    }

    @Override
    public PhenomenonValue api_getCurrentObservation(AbilityType abilityType) {

        Map<AbilityType, List<PhenomenonValue>> obserations = sensorSimEntity.getDeviceLogic().getObservations();

        List<PhenomenonValue> valueList = obserations.get(abilityType);

        if (valueList.size() > 0){
            return valueList.get(valueList.size()-1);
        }   else {
            return null; // FIXME powinno zwracać NullPhenomenonValue
        }
    }
}
