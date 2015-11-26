package org.mdyk.netsim.logic.node.statistics;

import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.node.Sensor;
import org.mdyk.netsim.logic.node.program.SensorProgram;

import java.util.List;

/**
 * Holds and calculates all important statistics about sensor.
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
     * Returns all communication processes which are incoming for the given sensor
     * @return
     *      List of incoming communication processes
     */
    List<CommunicationProcess> getIncomingCommunication();

    /**
     * Returns all communication processes which are outgoing for the given sensor
     * @return
     *      List of incoming communication processes
     */
    List<CommunicationProcess> getOutgoingCommunication();

    void addProgram(SensorProgram program);

    List<SensorProgram> getSensorPrograms();

    void setSensor(Sensor sensor);

    int getSensorId();

}
