package org.mdyk.netsim.logic.node.simentity;


import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.program.Middleware;

public interface SensorSimEntity {

    public void startEntity();

    public void stopEntity();

    public double getSimTime();

    public void setSensorLogic(SensorLogic sensorLogic);

    public SensorLogic getSensorLogic();

    public void setMiddleware(Middleware middleware);

    public Middleware getMiddleware();

}
