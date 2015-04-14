package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.node.program.*;

import javax.inject.Inject;

/**
 * Main wrapper which describes construct of a sensor.
 */
public class Sensor {

    private SensorLogic sensorLogic;
    private SensorSimEntity sensorSimEntity;
    private SensorAPI sensorAPI;
    private Middleware middleware;

    @Inject
    public Sensor(SensorLogic sensorLogic, SensorSimEntity sensorSimEntity, SensorAPI sensorAPI, Middleware middleware) {
        this.sensorLogic = sensorLogic;
        this.sensorSimEntity = sensorSimEntity;
        this.sensorAPI = sensorAPI;
        this.middleware = middleware;
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
}
