package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;

public class MessageParser {

    public static final String MESSAGE_ACTION_KEY = "a";
    public static final String NODE_ID_KEY = "id";
    public static final String SOURCE_NODE_KEY = "s";
    public static final String DEST_NODE_KEY = "d";
    public static final String POSITION_KEY = "p";

    public static InformationNeedMessage parseJSON(String JSONString) {

        InformationNeedMessage inm = null;

        JSONObject jsonObject = new JSONObject(JSONString);
        String action  = String.valueOf(jsonObject.get(MESSAGE_ACTION_KEY));

        switch (MessageType.getMessageType(action)) {
            case TOPOLOGY_DISCOVERY_ASK:
                inm = new TopologyDiscoveryMessage(String.valueOf(jsonObject.get(SOURCE_NODE_KEY)));
                break;

            case TOPOLOGY_DISCOVERY_RESP:
                inm = new TopologyDiscoveryResponseMessage(String.valueOf(jsonObject.get(NODE_ID_KEY)), String.valueOf(jsonObject.get(POSITION_KEY)));
                break;
        }

        return inm;
    }

    public enum MessageType {
        TOPOLOGY_DISCOVERY_ASK("td"),
        TOPOLOGY_DISCOVERY_RESP("tdr");


        private final String jsonKey;

        MessageType(String jsonKey) {
            this.jsonKey = jsonKey;
        }

        public static MessageType getMessageType(String actionType) {
            for(MessageType e:MessageType.values()){
                if(e.getJsonValue().equals(actionType)){
                    return e;
                }
            }
            throw new RuntimeException("Enum not found");
        }

        public final String getJsonValue() {
            return jsonKey;
        }

    }


}
