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

    /**
     * Flag defines if program should be resent to other
     * nodes.
     * @return
     *      true if program should be resent, false in other case.
     */
    public boolean resend();

}
