package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.node.statistics.DeviceStatistics;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.logic.node.program.*;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Factory which provides instances of device nodes
 */
@Singleton
public class DevicesFactory {

    @Inject
    private DeviceLogicFactory deviceLogicFactory;

    @Inject
    private SimEntityFactory simEntityFactory;

    @Inject
    private APIFactory APIFactory;

    @Inject
    private MiddlewareFactory middlewareFactory;

    @Inject
    private DeviceStatisticsFactory deviceStatisticsFactory;

    @Deprecated
    public Device buildSensor(int id, String name, GeoPosition position, int radioRange, int bandwidth, double velocity, List<AbilityType> abilities , List<SensorModel<?,?>> sensors){

        DeviceStatistics deviceStatistics = deviceStatisticsFactory.buildSensorStatistics();
        DeviceLogic deviceLogic = deviceLogicFactory.buildSensorLogic(id,name,position,radioRange, bandwidth, velocity, abilities, sensors);
        DeviceSimEntity deviceSimEntity = simEntityFactory.buildSensorSimEntity(deviceLogic);
        DeviceAPI deviceAPI = APIFactory.buildSensorAPI(deviceSimEntity);
        Middleware middleware = middlewareFactory.buildMiddleware();

        return new Device(deviceLogic, deviceSimEntity, deviceAPI, middleware, deviceStatistics);
    }

    public Device buildSensor(int id, String name, GeoPosition position, int radioRange, int bandwidth, double velocity,
                              List<AbilityType> abilities , List<SensorModel<?,?>> sensors ,
                              List<CommunicationInterface> communicationInterfaces){

        DeviceStatistics deviceStatistics = deviceStatisticsFactory.buildSensorStatistics();
        DeviceLogic deviceLogic = deviceLogicFactory.buildSensorLogic(id,name,position,radioRange, bandwidth, velocity, abilities, sensors, communicationInterfaces);
        DeviceSimEntity deviceSimEntity = simEntityFactory.buildSensorSimEntity(deviceLogic);
        DeviceAPI deviceAPI = APIFactory.buildSensorAPI(deviceSimEntity);
        Middleware middleware = middlewareFactory.buildMiddleware();

        return new Device(deviceLogic, deviceSimEntity, deviceAPI, middleware, deviceStatistics);
    }


}
