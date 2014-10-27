package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import com.google.inject.assistedinject.Assisted;
import dissim.simspace.SimModel;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.CommunicationStatus;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.movement.geo.GeoMovementAlgorithm;
import org.mdyk.netsim.logic.movement.geo.GeoRouteMovementAlgorithm;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.logic.node.geo.RoutedGeoSensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import javax.inject.Inject;
import java.util.List;


// TODO zmiana nazwy albo całkowite usunięcie tej klasy
public class DisSimRoutedSensorNode extends DefaultSensorModel<GeoPosition> implements RoutedGeoSensorNode {


    private static final Logger logger = Logger.getLogger(DisSimRoutedSensorNode.class);

    protected List<GeoPosition> route;
    protected GeoMovementAlgorithm currentMovementAlg;
    protected Environment environment;
    protected WirelessChannel wirelessChannel;
    protected DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;



    @Inject
    public DisSimRoutedSensorNode(@Assisted("id") int id, @Assisted GeoPosition position,
                                  @Assisted("radioRange") int radioRange,
                                  @Assisted double velocity, @Assisted List<AbilityType> abilities,
                                  Environment environment, WirelessChannel wirelessChannel) {
        super(id, position, radioRange, velocity, abilities);

        this.currentMovementAlg = new GeoRouteMovementAlgorithm();
        this.environment = environment;
        this.wirelessChannel = wirelessChannel;

    }

    @Override
    public void sense() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setLatitude(double latitude) {
        this.position.setLongitude(latitude);
    }

    @Override
    public double getLatitude() {
        return position.getLatitude();
    }

    @Override
    public void setLongitude(double longitude) {
        position.setLongitude(longitude);
    }

    @Override
    public double getLongitude() {
        return position.getLongitude();
    }

    @Override
    public List<GeoPosition> getRoute() {
        return route;
    }

    @Override
    public void setRoute(List<GeoPosition> route) {
        this.route = route;
        currentMovementAlg.setRoute(getRoute());
    }

    @Override
    public void startNode() {
        disSimRoutedSensorNodeEntity = new DisSimRoutedSensorNodeEntity(SimModel.getInstance().getCommonSimContext() , this);
    }

    @Override
    public void stopNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pauseNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resumeNode() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void work() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void move() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void startCommunication(Object message, ISensorModel<GeoPosition>... receivers) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CommunicationStatus getCommunicationStatus() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }



}
