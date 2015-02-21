package org.mdyk.netsim.logic.communication.message;


import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

public class SimpleMessage implements Message<Object> {

    private SensorNode<?> originSource;
    private SensorNode<?> originDest;
    private Object content;
    private int size;
    private int id;

    /**
     *
     * @param originSource
     *      sender of the message. It is the origin sensor, which should not change during communication
     *      process.
     * @param originDest
     *      receiver of the message. sender of the message. It is the origin sensor, which should not change during communication
     *      process.
     * @param content
     *      content of the message.
     * @param size
     *      size of the message in bytes.
     */
    public SimpleMessage(int id, SensorNode<?> originSource, SensorNode<?> originDest, Object content, int size) {
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
        return size;
    }

    @Override
    public int getMessageSource() {
        return originSource.getID();
    }

    @Override
    public int getMessageDest() {
        return originDest.getID();
    }
}
