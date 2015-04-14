package org.mdyk.netsim.logic.node.program;

/**
 * Interface for program executed by sensor
 */
public interface SensorProgram {

    /**
     * Executes program
     */
    public void execute();

    public void setParams(Object ... params);

    public void getResult();

}
