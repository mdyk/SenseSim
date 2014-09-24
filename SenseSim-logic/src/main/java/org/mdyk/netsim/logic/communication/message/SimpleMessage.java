package org.mdyk.netsim.logic.communication.message;


public class SimpleMessage implements Message {

    private Object content;
    private int size;

    public SimpleMessage(Object content, int size) {
        this.content = content;
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
