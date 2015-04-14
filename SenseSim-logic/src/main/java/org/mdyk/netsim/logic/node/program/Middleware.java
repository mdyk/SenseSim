package org.mdyk.netsim.logic.node.program;

import org.mdyk.netsim.logic.node.api.SensorAPI;

import java.util.List;

/**
 * Middleware for sensors
 */
public interface Middleware {

    /**
     * Initializes middleware when sensor is created.
     */
    public void initialize();

    public void setSensorAPI(SensorAPI api);

    public void loadProgram(SensorProgram program);

    public List<SensorProgram> getPrograms();

    /**
     * Method executed during sensors work
     */
    public void execute();

}
