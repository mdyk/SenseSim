package org.mdyk.netsim.logic.communication.process;


import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.mathModel.device.IDeviceModel;

/**
 * Interface for communication process in SenseSim
 */
public interface CommunicationProcess {


    /**
     * Return id of the process. Should simulation wide unique.
     * @return
     *      id of the process.
     */
    int getID();

    /**
     * Returns communication status described by CommunicationStatus enum
     * @return
     *      communication status.
     */
    CommunicationStatus getCommunicationStatus(double time);

    /**
     * Returns current communication status described by CommunicationStatus enum
     * @return
     *      communication status.
     */
    CommunicationStatus getCommunicationStatus();

    /**
     * Returns value of simulation time when process has started
     * @return
     *      time when process has started.
     */
    double getStartTime();

    /**
     * Returns value of simulation time when process has finished
     * @return
     *      time when process has finished.
     */
    double getEndTime();

    /**
     * Returns expected simulation time when process should finish.
     * @return
     *      simulation time when process should finish successfully.
     */
    double getETA();

    /**
     * Returns message which is sent by communication process.
     * @return
     *      message
     */
    Message getMessage();

    /**
     * Defines how many bits have been already sent.
     * @param bits
     *      sent bits since the beginning of the process.
     */
    void bitsSent(int bits);

    /**
     * Defines how many bits have been sent in current step.
     * @param bits
     *      bits sent since last update.
     */
    void addBitsSent(int bits);

    /**
     * Marks whole process as failed. Can be caused due to loss of connectivity.
     */
    void processInterrupted();

    IDeviceModel<?> getSender();

    IDeviceModel<?> getReceiver();

    /**
     * Returns communication interface id used during communication (should be the same for sender and receiver).
     * @return
     *      numerical id of the communication interface
     */
    int getCommunicationIntterfaceId();

}
