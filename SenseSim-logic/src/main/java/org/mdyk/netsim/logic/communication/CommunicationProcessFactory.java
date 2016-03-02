package org.mdyk.netsim.logic.communication;

import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.mathModel.device.IDeviceModel;

/**
 * Factory for communication processes
 */
public interface CommunicationProcessFactory {

    /**
     * Creates communication process in hop style (one hop - one process)
     * @param id
     *      id of the process
     * @param sender
     *      reference to the sender in current hop
     * @param receiver
     *      reference to the receiver in current hop
     * @param startTime
     *      start time of the process
     * @param message
     *      message to sent
     * @return
     *      instance of the communication process
     */
    public CommunicationProcess createCommunicationProcess(int id, IDeviceModel<?> sender, IDeviceModel<?> receiver, double startTime, Message message);

    /**
     * Creates communication process in hop style (one hop - one process)
     * @param sender
     *      reference to the sender in current hop
     * @param receiver
     *      reference to the receiver in current hop
     * @param startTime
     *      start time of the process
     * @param message
     *      message to sent
     * @return
     *      instance of the communication process
     */
    public CommunicationProcess createCommunicationProcess(IDeviceModel<?> sender, IDeviceModel<?> receiver, double startTime, Message message);

}
