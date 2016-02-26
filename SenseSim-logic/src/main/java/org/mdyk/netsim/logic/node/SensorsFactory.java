package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.node.statistics.SensorStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.logic.node.program.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Factory which provides instances of sensor nodes
 */
@Singleton
public class SensorsFactory {

    @Inject
    private SensorLogicFactory sensorLogicFactory;

    @Inject
    private SimEntityFactory simEntityFactory;

    @Inject
    private SensorAPIFactory sensorAPIFactory;

    @Inject
    private MiddlewareFactory middlewareFactory;

    @Inject
    private SensorStatisticsFactory sensorStatisticsFactory;

    public Device buildSensor(int id, GeoPosition position, int radioRange, int bandwidth, double velocity, List<AbilityType> abilities){

        SensorStatistics sensorStatistics = sensorStatisticsFactory.buildSensorStatistics();
        DeviceLogic deviceLogic = sensorLogicFactory.buildSensorLogic(id,position,radioRange, bandwidth, velocity, abilities);
        SensorSimEntity sensorSimEntity = simEntityFactory.buildSensorSimEntity(deviceLogic);
        SensorAPI sensorAPI = sensorAPIFactory.buildSensorAPI(sensorSimEntity);
        Middleware middleware = middlewareFactory.buildMiddleware();

        return new Device(deviceLogic,sensorSimEntity,sensorAPI, middleware, sensorStatistics);
    }

}
