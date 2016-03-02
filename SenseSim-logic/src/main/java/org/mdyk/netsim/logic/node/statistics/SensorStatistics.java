package org.mdyk.netsim.logic.node.statistics;

import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.node.program.SensorProgram;

import java.util.HashMap;
import java.util.List;

/**
 * Holds and calculates all important statistics about device.
 */
public interface SensorStatistics {


    /**
     * Adds communication process to the statistics it should recognize itself if that is incoming
     * or outgoing communication.
     * @param communicationProcess
     *      Started communication process.
     */
    void addCommunication(CommunicationProcess communicationProcess);

    /**
     * Returns all communication processes which are incoming for the given device
     * @return
     *      List of incoming communication processes
     */
    List<CommunicationProcess> getIncomingCommunication();

    /**
     * Returns all communication processes which are outgoing for the given device
     * @return
     *      List of incoming communication processes
     */
    List<CommunicationProcess> getOutgoingCommunication();

    /**
     * Adds loaded program to device's statistics.
     * @param sensorProgramMap
     *      map which key is device's Id and value is SensorProgram. Map should
     *      contain only one element.
     */
    void addProgram(HashMap<Integer , SensorProgram>sensorProgramMap);

    List<SensorProgram> getSensorPrograms();

    void setDevice(Device device);

    int getSensorId();

}
