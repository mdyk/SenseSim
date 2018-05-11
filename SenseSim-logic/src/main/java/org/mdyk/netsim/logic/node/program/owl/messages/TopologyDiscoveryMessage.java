package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;

/**
 * Class which represtnes a topology discovery message send by OWL middleware
 */
public class TopologyDiscoveryMessage implements InformationNeedMessage {

    private MessageParser.MessageType messageType;
    private int sourceNode;

    private TopologyDiscoveryMessage() {
        this.messageType = MessageParser.MessageType.TOPOLOGY_DISCOVERY_ASK;
    }

    public TopologyDiscoveryMessage(String sourceNodeString) {
        this();
        this.sourceNode = Integer.parseInt(sourceNodeString);
    }

    public TopologyDiscoveryMessage(int sourceNode) {
        this();
        this.sourceNode = sourceNode;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public String toJSON() {
        JSONObject jsonQuery = new JSONObject();
        jsonQuery.put(MessageParser.MESSAGE_ACTION_KEY ,messageType.getJsonValue());
        jsonQuery.put(MessageParser.SOURCE_NODE_KEY , this.sourceNode);

        return jsonQuery.toString();
    }

    public int getSize() {
        return toJSON().getBytes().length;
    }

    @Override
    public MessageParser.MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "TopologyDiscoveryMessage{" +
                "messageType=" + messageType +
                ", sourceNode=" + sourceNode +
                '}';
    }
}
