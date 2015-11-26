package org.mdyk.netsim.logic.node.program;

/**
 * Interface for program executed by sensor
 */
public interface SensorProgram {

    public enum ProgramStatus {LOADED, FINISHED_OK, DURING_ECECUTION, FINISHED_ERROR}

    /**
     * Executes program
     */

    void setPID(int PID);

    int getPID();

    void setParams(Object... params);

    Object getResult();

    /**
     * Flag defines if program should be resent to other
     * nodes.
     * @return
     *      true if program should be resent, false in other case.
     */
    boolean resend();

    /**
     * Returns program's source code or other description.
     * @return
     *      string object which represents program's source code or
     *      other description.
     */
    String getProgram();

    /**
     * Returns program's execution status.
     * @return
     */
    ProgramStatus getStatus();

}
