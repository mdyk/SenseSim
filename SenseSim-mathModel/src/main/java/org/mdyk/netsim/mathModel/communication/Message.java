package org.mdyk.netsim.mathModel.communication;

import org.mdyk.netsim.mathModel.sensor.SensorNode;

/**
 * Represents message
 */
// TODO rozważyć przeniesienie do modelu matematycznego
public interface Message<M> {

    /**
     * Return content of the message.
     * @return
     *      message content.
     */
    public M getMessageContent();

    /**
     * Returns size of the message in bytes
     * @return
     *      number of bytes
     */
    public int getSize();

    /**
     * Returns source sensor of the message. It is the origin sensor, which should not change during communication
     * process.
     * @return
     *      source sensor
     */
    public SensorNode<?> getMessageSource();

    /**
     * Returns destination sensor of the message. It is the origin sensor, which should not change during communication
     * process.
     * @return
     *      destination (sink) sensor
     */
    public SensorNode<?> getMessageDest();

}
