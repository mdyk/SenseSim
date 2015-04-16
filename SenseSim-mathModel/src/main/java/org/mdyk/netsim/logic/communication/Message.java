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
    public int getID();

    /**
     * Return content of the message.
     * @return
     *      message content.
     */
    public Object getMessageContent();

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
    public int getMessageSource();

    /**
     * Returns destination sensor of the message. It is the origin sensor, which should not change during communication
     * process.
     * @return
     *      destination (sink) sensor
     */
    public int getMessageDest();

}
