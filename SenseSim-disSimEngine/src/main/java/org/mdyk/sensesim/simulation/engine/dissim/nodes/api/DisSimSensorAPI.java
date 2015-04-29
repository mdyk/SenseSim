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
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimNodeEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimSensorLogic;
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
        this.sensorSimEntity.getSensorLogic().setRoute(route);
    }

    @Override
    public void api_startMove() {
        LOG.trace(">> api_startMove()");
        try {
            ((DisSimNodeEntity) sensorSimEntity).startMoveActivity = new StartMoveActivity((DisSimNodeEntity) sensorSimEntity);
            // FIXME przeprojektowanie interfejsów tak, żeby nie było konieczne rzutowanie
            ((DisSimSensorLogic) sensorSimEntity.getSensorLogic()).startMoveing();
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
            ((DisSimSensorLogic) sensorSimEntity.getSensorLogic()).stopMoveing();
        } catch (SimControlException e) {
            LOG.error(e.getMessage() , e);
        }
        LOG.trace("<< api_stopMove()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void api_sendMessage(int originSource, int originDest, Object content, Integer size) {
        Message message = new SimpleMessage(originDest, originSource, originDest , content, size);
        List<SensorNode> neighbours =  ((DisSimSensorLogic) sensorSimEntity.getSensorLogic()).wirelessChannel.scanForNeighbors(sensorSimEntity.getSensorLogic());
        List<SensorNode<GeoPosition>> nodesToHop = sensorSimEntity.getSensorLogic().getRoutingAlgorithm().getNodesToHop(sensorSimEntity.getSensorLogic().getID(), originDest , message , neighbours);
        sensorSimEntity.getSensorLogic().startCommunication(message, nodesToHop.toArray(new SensorNode[nodesToHop.size()]));
    }

    @Override
    public Map<AbilityType, List<PhenomenonValue>> api_getObservations() {
        LOG.trace(">< api_getObservations()");
        return sensorSimEntity.getSensorLogic().getObservations();
    }

    @Override
    public void api_setRoutingAlgorithm(RoutingAlgorithm<?> routingAlgorithm) {
        LOG.trace(">> api_setRoutingAlgorithm()");
        this.sensorSimEntity.getSensorLogic().setRoutingAlgorithm(routingAlgorithm);
        LOG.trace("<< api_setRoutingAlgorithm()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> api_scanForNeighbors() {
        LOG.trace(">< api_scanForNeighbors()");

        List<SensorNode> neighbours =  ((DisSimSensorLogic) sensorSimEntity.getSensorLogic()).wirelessChannel.scanForNeighbors(sensorSimEntity.getSensorLogic());

        List<Integer> neighboursIds = new ArrayList<>();

        for (SensorNode sensorNode : neighbours)  {
            neighboursIds.add(sensorNode.getID());
        }

        return neighboursIds;
    }

    @Override
    public GeoPosition api_getPosition() {
        LOG.trace(">< api_getPosition()");
        return new GeoPosition(sensorSimEntity.getSensorLogic().getPosition());
    }

    @Override
    public void api_setOnMessageHandler(Function<Message, Object> handler) {
        ((DisSimSensorLogic) sensorSimEntity.getSensorLogic()).onMessageHandler = handler;
    }

    @Override
    public Integer api_getMyID() {
        return sensorSimEntity.getSensorLogic().getID();
    }
}
