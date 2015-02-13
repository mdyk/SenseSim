package org.mdyk.netsim.logic.communication.message;


import org.mdyk.netsim.mathModel.communication.Message;
import org.mdyk.netsim.mathModel.sensor.SensorNode;

public class SimpleMessage implements Message<Object> {

    private SensorNode<?> originSource;
    private SensorNode<?> originDest;
    private Object content;
    private int size;

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
    public SimpleMessage(SensorNode<?> originSource, SensorNode<?> originDest, Object content, int size) {
        this.originSource = originSource;
        this.originDest = originDest;
        this.content = content;
        this.size = size;
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
    public SensorNode<?> getMessageSource() {
        return originSource;
    }

    @Override
    public SensorNode<?> getMessageDest() {
        return originDest;
    }
}
