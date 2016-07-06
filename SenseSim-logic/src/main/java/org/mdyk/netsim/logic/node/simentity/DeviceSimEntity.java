package org.mdyk.netsim.logic.node.simentity;


import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.program.Middleware;

public interface DeviceSimEntity {

    void startProgramExecution(int PID);

    void endProgramExecution(int PID);

    void startEntity();

    void stopEntity();

    double getSimTime();

    void setDeviceLogic(DeviceLogic deviceLogic);

    DeviceLogic getDeviceLogic();

    void setMiddleware(Middleware middleware);

    Middleware getMiddleware();

}
