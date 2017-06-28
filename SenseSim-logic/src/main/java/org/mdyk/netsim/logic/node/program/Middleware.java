package org.mdyk.netsim.logic.node.program;

import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;

import java.awt.image.VolatileImage;
import java.util.List;

/**
 * Middleware for sensors
 */
public interface Middleware {

    /**
     * Initializes middleware when device is created.
     */
    void initialize();

    void setDeviceAPI(DeviceAPI api);

    void setDeviceSimEntity(DeviceSimEntity simEntity);

    void loadProgram(SensorProgram program);

    /**
     * Loads program basing on its source code
     * @param code
     */
    void loadProgram(String code);

    List<SensorProgram> getPrograms();

    /**
     * Method executed during sensors work
     */
    void execute();

}
