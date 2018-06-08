package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;

/**
 * Class which represtnes a topology discovery message send by OWL middleware
 */
public class TopologyDiscoveryMessage implements InformationNeedMessage {

    private MessageParser.MessageType messageType;
    private int sourceNode;
    private int needId;

    private TopologyDiscoveryMessage() {
        this.messageType = MessageParser.MessageType.TOPOLOGY_DISCOVERY_ASK;
    }

    public TopologyDiscoveryMessage(String sourceNodeString, String needId) {
        this();
        this.sourceNode = Integer.parseInt(sourceNodeString);
        this.needId = Integer.parseInt(needId);
    }

    public TopologyDiscoveryMessage(int sourceNode, int needId) {
        this();
        this.sourceNode = sourceNode;
        this.needId = needId;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public String toJSON() {
        JSONObject jsonQuery = new JSONObject();
        jsonQuery.put(MessageParser.MESSAGE_ACTION_KEY ,messageType.getJsonValue());
        jsonQuery.put(MessageParser.SOURCE_NODE_KEY , this.sourceNode);
        jsonQuery.put(MessageParser.NEED_ID, this.needId);
        return jsonQuery.toString();
    }

    public int getSize() {
        return toJSON().getBytes().length;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public MessageParser.MessageType getMessageType() {
        return messageType;
    }

    public int getNeedId() {
        return needId;
    }

    @Override
    public String toString() {
        return "TopologyDiscoveryMessage{" +
                "messageType=" + messageType +
                ", sourceNode=" + sourceNode +
                '}';
    }
}
