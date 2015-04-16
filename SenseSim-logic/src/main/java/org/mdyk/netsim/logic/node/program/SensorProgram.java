package org.mdyk.netsim.logic.node.program;

/**
 * Interface for program executed by sensor
 */
public interface SensorProgram {

    /**
     * Executes program
     */

    public void setPID(int PID);

    public int getPID();

    public void setParams(Object ... params);

    public Object getResult();

}
