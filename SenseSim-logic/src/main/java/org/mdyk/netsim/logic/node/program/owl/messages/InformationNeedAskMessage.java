package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;
import org.mdyk.netsim.logic.infon.Infon;

public class InformationNeedAskMessage implements InformationNeedMessage {

    private Infon infon;
    private final MessageParser.MessageType messageType = MessageParser.MessageType.INFORMATION_NEED_ASK;

    public InformationNeedAskMessage(Infon infon) {
        this.infon = infon;
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageParser.MESSAGE_ACTION_KEY, messageType.getJsonValue());
        jsonObject.put(MessageParser.INFON , infon.toString());
        return jsonObject.toString();
    }

    @Override
    public MessageParser.MessageType getMessageType() {
        return messageType;
    }

    public Infon getInfon() {
        return infon;
    }
}
