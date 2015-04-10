package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimContext;
import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;


public class DisSimNodeEntity extends BasicSimEntity implements SensorSimEntity {

    private static final Logger LOG = Logger.getLogger(DisSimNodeEntity.class);

    //FIXME
    public StartMoveActivity startMoveActivity;
    //FIXME
    public EndMoveActivity endMoveActivity;
    //FIXME
    public StartSenseActivity startSenseActivity;
    //FIXME
    public EndSenseActivity endSenseActivity;

    protected SensorLogic sensorLogic;

    protected Environment environment;

    public DisSimNodeEntity(BasicSimContext context, SensorLogic sensorLogic, Environment environment) {
        super(context);
        this.setSensorLogic(sensorLogic);
        this.environment = environment;
    }

    public void startNode() {
        try {
            this.startMoveActivity = new StartMoveActivity(this);
            this.startSenseActivity = new StartSenseActivity(this);
        } catch (SimControlException e) {
           LOG.error(e.getMessage(),e);
        }

    }

    public DisSimSensorLogic getProgrammableNode() {
        return (DisSimSensorLogic) sensorLogic;
    }


//    @Override
//    public void setSimEntity(SensorSimEntity sensorSimEntity) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void api_setRoute(List<GeoPosition> route) {
//        this.sensorLogic.setRoute(route);
//    }
//
//    @Override
//    public void api_startMove() {
//        LOG.trace(">> api_startMove()");
//        try {
//            startMoveActivity = new StartMoveActivity(this);
////            programmableNode.startMoveing();
//        } catch (SimControlException e) {
//            LOG.error(e.getMessage(),e);
//        }
//        LOG.trace("<< api_startMove()");
//    }
//
//    @Override
//    public void api_stopMove() {
//        LOG.trace(">> api_stopMove()");
//        try {
//            startMoveActivity.interrupt();
//            endMoveActivity.interrupt();
//        } catch (SimControlException e) {
//            LOG.error(e.getMessage() , e);
//        }
//        LOG.trace("<< api_stopMove()");
//    }

//    @Override
//    @SuppressWarnings("unchecked")
//    public void api_sendMessage(int destinationID, Message<?> message) {
//        List<SensorNode<GeoPosition>> nodesToHop = sensorLogic.getRoutingAlgorithm().getNodesToHop(sensorLogic.getID(), destinationID , message , api_scanForNeighbors());
//        sensorLogic.startCommunication(message, nodesToHop.toArray(new SensorNode[nodesToHop.size()]));
//    }

//    @Override
//    public Map<AbilityType, List<PhenomenonValue>> api_getObservations() {
//        LOG.trace(">< api_getObservations()");
//        return programmableNode.getObservations();
//    }
//
//    @Override
//    public void api_setRoutingAlgorithm(RoutingAlgorithm<?> routingAlgorithm) {
//        LOG.trace(">> api_setRoutingAlgorithm()");
//        this.programmableNode.setRoutingAlgorithm(routingAlgorithm);
//        LOG.trace("<< api_setRoutingAlgorithm()");
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public List<SensorNode<GeoPosition>> api_scanForNeighbors() {
//        LOG.trace(">< api_scanForNeighbors()");
//        return programmableNode.wirelessChannel.scanForNeighbors(programmableNode);
//    }
//
//    @Override
//    public GeoPosition api_getPosition() {
//        return programmableNode.getPosition();
//    }
//
//    @Override
//    public void api_setOnMessageHandler(Function<Message<?>, Object> handler) {
//        this.programmableNode.onMessageHandler = handler;
//    }

    @Override
    public void setSensorLogic(SensorLogic sensorLogic) {
        this.sensorLogic = sensorLogic;
    }

    @Override
    public SensorLogic getSensorLogic() {
        return sensorLogic;
    }
}
