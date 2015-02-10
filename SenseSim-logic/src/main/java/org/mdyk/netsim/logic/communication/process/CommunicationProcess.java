package org.mdyk.netsim.logic.communication.process;

import org.mdyk.netsim.mathModel.communication.Message;

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
    public CommunicationStatus getCommunicationStatus(double time);

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

    /**
     * Returns message which is sent by communication process.
     * @return
     *      message
     */
    public Message getMessage();

    /**
     * Defines how many bits have been already sent.
     * @param bits
     *      sent bits since the beginning of the process.
     */
    public void bitsSent(int bits);

    /**
     * Defines how many bits have been sent in current step.
     * @param bits
     *      bits sent since last update.
     */
    public void addBitsSent(int bits);

    /**
     * Marks whole process as failed. Can be caused due to loss of connectivity.
     */
    public void processInterrupted();

}
