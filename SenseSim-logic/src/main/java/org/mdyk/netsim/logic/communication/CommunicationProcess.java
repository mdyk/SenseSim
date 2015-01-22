package org.mdyk.netsim.logic.communication;

/**
 * Interface for communication process in SenseSim
 */
public interface CommunicationProcess {


    /**
     * Return id of the process. Should simulation wide unique.
     * @return
     *      id of the process.
     */
    public int getID();

    /**
     * Returns communication status described by CommunicationStatus enum
     * @return
     *      communication status.
     */
    public CommunicationStatus getCommunicationStatus();

    /**
     * Returns value of simulation time when process has started
     * @return
     *      time when process has started.
     */
    public double getStartTime();

    /**
     * Returns expected simulation time when process should finish.
     * @return
     *      simulation time when process should finish successfully.
     */
    public double getETA();


}
