package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
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

    public Sensor buildSensor(int id, GeoPosition position, int radioRange, double velocity, List<AbilityType> abilities){

        SensorLogic sensorLogic = sensorLogicFactory.buildSensorLogic(id,position,radioRange,velocity, abilities);
        SensorSimEntity sensorSimEntity = simEntityFactory.buildSensorSimEntity(sensorLogic);
        SensorAPI sensorAPI = sensorAPIFactory.buildSensorAPI(sensorSimEntity);
        Middleware middleware = middlewareFactory.buildMiddleware();

        sensorLogic.setSimEntity(sensorSimEntity);
        sensorSimEntity.setSensorLogic(sensorLogic);
        sensorAPI.setSimEntity(sensorSimEntity);

        return new Sensor(sensorLogic,sensorSimEntity,sensorAPI, middleware);
    }

}
