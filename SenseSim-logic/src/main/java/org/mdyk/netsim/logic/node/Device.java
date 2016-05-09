package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.communication.routing.FloodingRouting;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.node.program.*;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;

import javax.inject.Inject;

/**
 * Main wrapper which describes construct of a device.
 */
public class Device {

    private DeviceLogic deviceLogic;
    private DeviceSimEntity deviceSimEntity;
    private DeviceAPI deviceAPI;
    private Middleware middleware;
    private DeviceStatistics statistics;

    @Inject
    public Device(DeviceLogic deviceLogic, DeviceSimEntity deviceSimEntity, DeviceAPI deviceAPI, Middleware middleware, DeviceStatistics deviceStatistics) {
        this.deviceLogic = deviceLogic;
        this.deviceSimEntity = deviceSimEntity;
        this.deviceAPI = deviceAPI;
        this.middleware = middleware;
        this.statistics = deviceStatistics;

        deviceStatistics.setDevice(this);
        deviceLogic.setSimEntity(deviceSimEntity);
        deviceLogic.setDeviceStatistics(deviceStatistics);
        // FIXME to powinno znajdować się w konfiguracji !!!!
        deviceLogic.setRoutingAlgorithm(new FloodingRouting(deviceStatistics));
        deviceSimEntity.setDeviceLogic(deviceLogic);
        deviceSimEntity.setMiddleware(middleware);
        deviceAPI.setSimEntity(deviceSimEntity);
        middleware.setDeviceAPI(deviceAPI);
        middleware.setDeviceSimEntity(deviceSimEntity);
        middleware.initialize();
    }

    public DeviceLogic getDeviceLogic() {
        return deviceLogic;
    }

    public DeviceSimEntity getDeviceSimEntity() {
        return deviceSimEntity;
    }

    public DeviceAPI getDeviceAPI() {
        return deviceAPI;
    }

    public Middleware getMiddleware() {
        return middleware;
    }

    public DeviceStatistics getStatistics() {
        return this.statistics;
    }
}
