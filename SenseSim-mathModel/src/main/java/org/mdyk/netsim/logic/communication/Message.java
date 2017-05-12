package org.mdyk.netsim.logic.communication;

/**
 * Represents message
 */
// TODO rozważyć przeniesienie do modelu matematycznego
public interface Message {

    /**
     * Returns ID number of the message. Should be unique.
     * Implementation may not guarantee that.
     * @return
     *      ID of the message.
     */
    long getID();

    /**
     * Return content of the message.
     * @return
     *      message content.
     */
    Object getMessageContent();

    /**
     * Returns size of the message in bytes
     * @return
     *      number of bytes
     */
    int getSize();

    /**
     * Returns source device of the message. It is the origin device, which should not change during communication
     * process.
     * @return
     *      source device
     */
    int getMessageSource();

    /**
     * Returns destination device of the message. It is the origin device, which should not change during communication
     * process.
     * @return
     *      destination (sink) device
     */
    int getMessageDest();

}
