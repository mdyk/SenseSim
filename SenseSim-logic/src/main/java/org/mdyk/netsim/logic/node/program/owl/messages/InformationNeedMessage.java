package org.mdyk.netsim.logic.node.program.owl.messages;


public interface InformationNeedMessage {

    String toJSON();

    MessageParser.MessageType getMessageType();

    default int getSize() {
        return toJSON().getBytes().length;
    }

}
