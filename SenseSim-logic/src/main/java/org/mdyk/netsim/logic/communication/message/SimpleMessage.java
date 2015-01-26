package org.mdyk.netsim.logic.communication.message;


import org.mdyk.netsim.mathModel.sensor.ISensorModel;

public class SimpleMessage implements Message<Object> {

    private ISensorModel<?> originSource;
    private ISensorModel<?> originDest;
    private Object content;
    private int size;

    public SimpleMessage(ISensorModel<?> originSource, ISensorModel<?> originDest, Object content, int size) {
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
    public ISensorModel<?> getMessageSource() {
        return originSource;
    }

    @Override
    public ISensorModel<?> getMessageDest() {
        return originDest;
    }
}
