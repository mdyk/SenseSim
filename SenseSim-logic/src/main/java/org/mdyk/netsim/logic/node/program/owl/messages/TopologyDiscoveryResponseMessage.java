package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;
import org.mdyk.netsim.logic.util.GeoPosition;

public class TopologyDiscoveryResponseMessage implements InformationNeedMessage {

    private static final String positionDelimiter = "#";
    private MessageParser.MessageType messageType;
    private int nodeId;
    private GeoPosition position;

    private TopologyDiscoveryResponseMessage() {
        messageType = MessageParser.MessageType.TOPOLOGY_DISCOVERY_RESP;
    }

    public TopologyDiscoveryResponseMessage(int nodeId, GeoPosition position) {
        this();
        this.nodeId = nodeId;
        this.position = position;
    }

    public TopologyDiscoveryResponseMessage(String nodeId, String positionString) {
        this();
        this.nodeId = Integer.parseInt(nodeId);
        String[] latlonArray = positionString.split(positionDelimiter);
        this.position = new GeoPosition(Double.parseDouble(latlonArray[0]) , Double.parseDouble(latlonArray[1]));

    }

    @Override
    public String toJSON() {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put(MessageParser.MESSAGE_ACTION_KEY ,messageType.getJsonValue());
        jsonResponse.put(MessageParser.NODE_ID_KEY , this.nodeId);
        jsonResponse.put(MessageParser.POSITION_KEY, this.encodePosition());

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

    @Override
    public String toString() {
        return "TopologyDiscoveryResponseMessage{" +
                "messageType=" + messageType +
                ", nodeId=" + nodeId +
                ", position=" + position +
                '}';
    }
}
