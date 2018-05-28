package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;
import org.mdyk.netsim.logic.infon.Infon;

public class InformationNeedAskMessage implements InformationNeedMessage {

    private final MessageParser.MessageType messageType = MessageParser.MessageType.INFORMATION_NEED_ASK;
    private Infon infon;
    private int sourceNode;


    public InformationNeedAskMessage(int sourceNode , Infon infon) {
        this.infon = infon;
        this.sourceNode = sourceNode;
    }

    public InformationNeedAskMessage(String sourceNode, Infon infon) {
        this(Integer.parseInt(sourceNode) , infon);
    }

    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageParser.MESSAGE_ACTION_KEY, messageType.getJsonValue());
        jsonObject.put(MessageParser.INFON , infon.toString());
        jsonObject.put(MessageParser.SOURCE_NODE_KEY , sourceNode);
        return jsonObject.toString();
    }

    @Override
    public MessageParser.MessageType getMessageType() {
        return messageType;
    }

    public Infon getInfon() {
        return infon;
    }

    public int getSourceNode() {
        return sourceNode;
    }
}