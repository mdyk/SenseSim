package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;
import org.mdyk.netsim.logic.util.GeoPosition;

public class TopologyDiscoveryResponseMessage implements InformationNeedMessage {

    private static final String positionDelimiter = "#";
    private MessageParser.MessageType messageType;
    private int nodeId;
    private GeoPosition position;
    private int needId;

    private TopologyDiscoveryResponseMessage() {
        messageType = MessageParser.MessageType.TOPOLOGY_DISCOVERY_RESP;
    }

    public TopologyDiscoveryResponseMessage(int nodeId, GeoPosition position, int needId) {
        this();
        this.nodeId = nodeId;
        this.position = position;
        this.needId = needId;
    }

    public TopologyDiscoveryResponseMessage(String nodeId, String positionString, String needId) {
        this();
        this.nodeId = Integer.parseInt(nodeId);
        String[] latlonArray = positionString.split(positionDelimiter);
        this.position = new GeoPosition(Double.parseDouble(latlonArray[0]) , Double.parseDouble(latlonArray[1]));
        this.needId = Integer.parseInt(needId);
    }

    @Override
    public String toJSON() {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put(MessageParser.MESSAGE_ACTION_KEY ,messageType.getJsonValue());
        jsonResponse.put(MessageParser.NODE_ID_KEY , this.nodeId);
        jsonResponse.put(MessageParser.POSITION_KEY, this.encodePosition());
        jsonResponse.put(MessageParser.NEED_ID, this.needId);
        return jsonResponse.toString();
    }

    public int getNodeId() {
        return nodeId;
    }

    public GeoPosition getPosition() {
        return position;
    }

    private String encodePosition() {
        return position.getLatitude()+positionDelimiter+position.getLongitude();
    }

    @Override
    public MessageParser.MessageType getMessageType() {
        return messageType;
    }

    public int getNeedId() {
        return needId;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "TopologyDiscoveryResponseMessage{" +
                "messageType=" + messageType +
                ", nodeId=" + nodeId +
                ", position=" + position +
                ", needId=" + needId +
                '}';
    }
}
