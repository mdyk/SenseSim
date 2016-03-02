package org.mdyk.netsim.logic.communication.message;


import org.mdyk.netsim.logic.communication.Message;

public class SimpleMessage implements Message {

    private int originSource;
    private int originDest;
    private Object content;
    private Integer size;
    private int id;

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
    public SimpleMessage(int id, int originSource, int originDest, Object content, Integer size) {
        this.id = id;
        this.originSource = originSource;
        this.originDest = originDest;
        this.content = content;
        this.size = size;
    }

    @Override
    public int getID() {
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

        return size;
    }

    @Override
    public int getMessageSource() {
        return originSource;
    }

    @Override
    public int getMessageDest() {
        return originDest;
    }
}
