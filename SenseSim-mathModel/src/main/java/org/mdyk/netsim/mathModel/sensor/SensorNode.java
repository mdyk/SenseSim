package org.mdyk.netsim.mathModel.sensor;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.util.Position;

/**
 * Uniwersalny interfejs dostępowy do węzła sieci
 */
public interface SensorNode<P extends Position> extends ISensorModel<P> {

    public void startNode();

    public void stopNode();

    public void pauseNode();

    public void resumeNode();

    public void work();

    public void move();

    // TODO wsparcie dla wielu procesów komunikacji równocześnie
    public void startCommunication(Message message, SensorNode<P> ... receivers);

    // TODO instalacja programu i pobieranie danych z sensorów

}
