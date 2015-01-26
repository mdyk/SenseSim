package org.mdyk.netsim.logic.node;

import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

/**
 * Uniwersalny interfejs dostępowy do węzła sieci
 */
public interface SensorNode<P extends Position> extends ISensorModel<P> {

    public void startNode();

    public void stopNode();

    public void pauseNode();

    public void resumeNode();

    @Deprecated
    public void work();

    public void move();

    // TODO wsparcie dla wielu procesów komunikacji równocześnie
    public void startCommunication(Object message, ISensorModel<P> ... receivers);

    public CommunicationStatus getCommunicationStatus();

    // TODO instalacja programu i pobieranie danych z sensorów

}
