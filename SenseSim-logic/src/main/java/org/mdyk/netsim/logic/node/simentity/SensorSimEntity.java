package org.mdyk.netsim.logic.node.simentity;


import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.program.Middleware;

public interface SensorSimEntity {

    public void startProgramExecution(int PID);

    public void endProgramExecution(int PID);

    public void startEntity();

    public void stopEntity();

    public double getSimTime();

    public void setDeviceLogic(DeviceLogic deviceLogic);

    public DeviceLogic getDeviceLogic();

    public void setMiddleware(Middleware middleware);

    public Middleware getMiddleware();

}
