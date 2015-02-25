package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimContext;
import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.RoutingAlgorithm;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class DisSimNodeEntity extends BasicSimEntity implements SensorAPI<GeoPosition> {

    private static final Logger LOG = Logger.getLogger(DisSimNodeEntity.class);

    protected DisSimProgrammableNode programmableNode;

    protected StartMoveActivity startMoveActivity;
    protected EndMoveActivity endMoveActivity;
    protected StartSenseActivity startSenseActivity;
    protected EndSenseActivity endSenseActivity;



    public DisSimNodeEntity(BasicSimContext context, DisSimProgrammableNode routedNode) {
        super(context);
        this.programmableNode = routedNode;
    }

    public void startNode() {
        try {
            this.startMoveActivity = new StartMoveActivity(this);
            this.startSenseActivity = new StartSenseActivity(this);
        } catch (SimControlException e) {
           LOG.error(e.getMessage(),e);
        }

    }

    public DisSimProgrammableNode getProgrammableNode() {
        return programmableNode;
    }


    @Override
    public void api_setRoute(List<GeoPosition> route) {
        this.programmableNode.setRoute(route);
    }

    @Override
    public void api_startMove() {
        LOG.trace(">> api_startMove()");
        try {
            startMoveActivity = new StartMoveActivity(this);
            programmableNode.startMoveing();
        } catch (SimControlException e) {
            LOG.error(e.getMessage(),e);
        }
        LOG.trace("<< api_startMove()");
    }

    @Override
    public void api_stopMove() {
        LOG.trace(">> api_stopMove()");
        try {
            startMoveActivity.interrupt();
            endMoveActivity.interrupt();
        } catch (SimControlException e) {
            LOG.error(e.getMessage() , e);
        }
        LOG.trace("<< api_stopMove()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void api_sendMessage(int destinationID, Message<?> message) {
        List<SensorNode<GeoPosition>> nodesToHop = programmableNode.getRoutingAlgorithm().getNodesToHop(programmableNode.getID(), destinationID , message , api_scanForNeighbors());
        programmableNode.startCommunication(message, nodesToHop.toArray(new SensorNode[nodesToHop.size()]));
    }

    @Override
    public Map<AbilityType, List<PhenomenonValue>> api_getObservations() {
        LOG.trace(">< api_getObservations()");
        return programmableNode.getObservations();
    }

    @Override
    public void api_setRoutingAlgorithm(RoutingAlgorithm<?> routingAlgorithm) {
        LOG.trace(">> api_setRoutingAlgorithm()");
        this.programmableNode.setRoutingAlgorithm(routingAlgorithm);
        LOG.trace("<< api_setRoutingAlgorithm()");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SensorNode<GeoPosition>> api_scanForNeighbors() {
        LOG.trace(">< api_scanForNeighbors()");
        return programmableNode.wirelessChannel.scanForNeighbors(programmableNode);
    }

    @Override
    public GeoPosition api_getPosition() {
        return programmableNode.getPosition();
    }

    @Override
    public void api_setOnMessageHandler(Function<Message<?>, Object> handler) {
        this.programmableNode.onMessageHandler = handler;
    }
}
