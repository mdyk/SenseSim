package org.mdyk.netsim.logic.communication.message;

/**
 * Represents message
 */
// TODO rozważyć przeniesienie do modelu matematycznego
public interface Message {

    /**
     * Returns size of the message in bytes
     * @return
     *      number of bytes
     */
    public int getSize();

}
