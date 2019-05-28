package org.mdyk.netsim.logic.communication.message;


import org.mdyk.netsim.logic.communication.Message;

public class SimpleMessage implements Message {

    // FIXME dla ramki TCP
    final private Integer sizeOffset = 100;
    private int originSource;
    private int originDest;
    private Object content;
    private Integer size;
    private long id;
    private int communicationInterfaceId;

    /**
     *
     * @param originSource
     *      sender of the message. It is the origin device, which should not change during communication
     *      process.
     * @param originDest
     *      receiver of the message. sender of the message. It is the origin device, which should not change during communication
     *      process.
     * @param content
     *      content of the message.
     * @param size
     *      size of the message in bytes.
     */
    @Deprecated
    public SimpleMessage(long id, int originSource, int originDest, Object content, Integer size) {
        this.id = id;
        this.originSource = originSource;
        this.originDest = originDest;
        this.content = content;
        this.size = size;
    }

    public SimpleMessage(long id, int originSource, int originDest , int communicationInterfaceId, Object content, Integer size) {
        this.originSource = originSource;
        this.originDest = originDest;
        this.content = content;
        this.size = size;
        this.id = id;
        this.communicationInterfaceId = communicationInterfaceId;
    }

    @Override
    public long getID() {
        return this.id;
    }

    @Override
    public Object getMessageContent() {
        return content;
    }

    @Override
    public int getSize() {

        if(content instanceof  String && size == null) {
            String contentString = (String) content;
            size = contentString.getBytes().length;
        }

        return size + sizeOffset;
    }

    @Override
    public int getMessageSource() {
        return originSource;
    }

    @Override
    public int getMessageDest() {
        return originDest;
    }

    @Override
    public int getCommunicationInterface() {
        return this.communicationInterfaceId;
    }

    @Override
    public String getMessageString() {
        return content.toString();
    }
}
