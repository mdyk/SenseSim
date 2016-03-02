package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.communication.routing.FloodingRouting;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.netsim.logic.node.program.*;
import org.mdyk.netsim.logic.node.statistics.SensorStatistics;

import javax.inject.Inject;

/**
 * Main wrapper which describes construct of a device.
 */
public class Device {

    private DeviceLogic deviceLogic;
    private SensorSimEntity sensorSimEntity;
    private SensorAPI sensorAPI;
    private Middleware middleware;
    private SensorStatistics statistics;

    @Inject
    public Device(DeviceLogic deviceLogic, SensorSimEntity sensorSimEntity, SensorAPI sensorAPI, Middleware middleware, SensorStatistics sensorStatistics) {
        this.deviceLogic = deviceLogic;
        this.sensorSimEntity = sensorSimEntity;
        this.sensorAPI = sensorAPI;
        this.middleware = middleware;
        this.statistics = sensorStatistics;

        sensorStatistics.setDevice(this);
        deviceLogic.setSimEntity(sensorSimEntity);
        deviceLogic.setSensorStatistics(sensorStatistics);
        // FIXME to powinno znajdować się w konfiguracji !!!!
        deviceLogic.setRoutingAlgorithm(new FloodingRouting(sensorStatistics));
        sensorSimEntity.setDeviceLogic(deviceLogic);
        sensorSimEntity.setMiddleware(middleware);
        sensorAPI.setSimEntity(sensorSimEntity);
        middleware.setSensorAPI(sensorAPI);
        middleware.setSensorSimEntity(sensorSimEntity);
        middleware.initialize();
    }

    public DeviceLogic getDeviceLogic() {
        return deviceLogic;
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
