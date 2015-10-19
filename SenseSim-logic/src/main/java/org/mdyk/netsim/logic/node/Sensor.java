package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.node.program.*;
import org.mdyk.netsim.logic.node.statistics.SensorStatistics;

import javax.inject.Inject;

/**
 * Main wrapper which describes construct of a sensor.
 */
public class Sensor {

    private SensorLogic sensorLogic;
    private SensorSimEntity sensorSimEntity;
    private SensorAPI sensorAPI;
    private Middleware middleware;
    private SensorStatistics statistics;

    @Inject
    public Sensor(SensorLogic sensorLogic, SensorSimEntity sensorSimEntity, SensorAPI sensorAPI, Middleware middleware, SensorStatistics sensorStatistics) {
        this.sensorLogic = sensorLogic;
        this.sensorSimEntity = sensorSimEntity;
        this.sensorAPI = sensorAPI;
        this.middleware = middleware;
        this.statistics = sensorStatistics;

        sensorStatistics.setSensor(this);
        sensorLogic.setSimEntity(sensorSimEntity);
        sensorSimEntity.setSensorLogic(sensorLogic);
        sensorSimEntity.setMiddleware(middleware);
        sensorAPI.setSimEntity(sensorSimEntity);
        middleware.setSensorAPI(sensorAPI);
        middleware.setSensorSimEntity(sensorSimEntity);
        middleware.initialize();
    }

    public SensorLogic getSensorLogic() {
        return sensorLogic;
    }

    public SensorSimEntity getSensorSimEntity() {
        return sensorSimEntity;
    }

    public SensorAPI getSensorAPI() {
        return sensorAPI;
    }

    public Middleware getMiddleware() {
        return middleware;
    }

    public SensorStatistics getStatistics() {
        return this.statistics;
    }
}
