package org.mdyk.netsim.mathModel.device;

import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.util.Position;

import java.util.HashMap;
import java.util.List;

/**
 * Uniwersalny interfejs dostępowy do węzła sieci
 */
public interface DeviceNode<P extends Position> extends IDeviceModel<P> {

    void startNode();

    void stopNode();

    void pauseNode();

    void resumeNode();

    void work();

    void move();

    // TODO wsparcie dla wielu procesów komunikacji równocześnie
    @Deprecated
    void startCommunication(Message message, DeviceNode<P>... receivers);

    /**
     * Starts communication between device and other, connected devices
     * @param message
     *      message to be sent
     * @param receivers
     *      map which defines receivers and communication interfaces which should be used to send the message.
     *      Keys are communication interfaces identifiers and values are lists with receivers.
     */
    void startCommunication(Message message, HashMap<Integer, List<DeviceNode<P>>> receivers);

    // TODO instalacja programu i pobieranie danych z sensorów

}
