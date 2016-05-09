package org.mdyk.netsim.logic.node.program;

import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;

import java.util.List;

/**
 * Middleware for sensors
 */
public interface Middleware {

    /**
     * Initializes middleware when device is created.
     */
    public void initialize();

    public void setDeviceAPI(DeviceAPI api);

    public void setDeviceSimEntity(DeviceSimEntity simEntity);

    public void loadProgram(SensorProgram program);

    public List<SensorProgram> getPrograms();

    /**
     * Method executed during sensors work
     */
    public void execute();

}
