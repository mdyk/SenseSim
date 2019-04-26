package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONObject;
import org.mdyk.netsim.logic.infon.Infon;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;

public class MessageParser {

    public static final String MESSAGE_ACTION_KEY = "a";
    public static final String NODE_ID_KEY = "id";
    public static final String SOURCE_NODE_KEY = "s";
    public static final String DEST_NODE_KEY = "d";
    public static final String POSITION_KEY = "p";
    public static final String INFON = "i";
    public static final String NEED_ID = "nid";
    public static final String PROCESSED_NODES = "psn";

    public static InformationNeedMessage parseJSON(String JSONString) {

        InformationNeedMessage inm = null;

        JSONObject jsonObject = new JSONObject(JSONString);
        String action  = String.valueOf(jsonObject.get(MESSAGE_ACTION_KEY));

        switch (MessageType.getMessageType(action)) {
            case TOPOLOGY_DISCOVERY_ASK:
                inm = new TopologyDiscoveryMessage(String.valueOf(jsonObject.get(SOURCE_NODE_KEY)), String.valueOf(jsonObject.get(NEED_ID)));
                break;

            case TOPOLOGY_DISCOVERY_RESP:
                inm = new TopologyDiscoveryResponseMessage(String.valueOf(jsonObject.get(NODE_ID_KEY)), String.valueOf(jsonObject.get(POSITION_KEY)), String.valueOf(jsonObject.get(NEED_ID)));
                break;

            case INFORMATION_NEED_ASK:
                inm = new InformationNeedAskMessage(String.valueOf(jsonObject.get(NEED_ID)), String.valueOf(jsonObject.get(SOURCE_NODE_KEY)), new Infon(String.valueOf(jsonObject.get(INFON))), jsonObject.getJSONArray(PROCESSED_NODES));
                break;

            case INFORMATION_NEED_RESP:

                String infonString = String.valueOf(jsonObject.get(INFON));

//                String[] infonsStringArr = infonsString.split(";");
//
//                List<Infon> infons = new ArrayList<>();
//                for(String infonString : infonsStringArr) {
                    Infon i = new Infon(infonString);
//                    infons.add(i);
//                }

                inm = new InformationNeedRespMessage(String.valueOf(jsonObject.get(SOURCE_NODE_KEY)), String.valueOf(jsonObject.get(NEED_ID)), jsonObject.getJSONArray(PROCESSED_NODES));

                ((InformationNeedRespMessage)inm).addInfon(i);

                break;
        }

        return inm;
    }

    public enum MessageType {
        TOPOLOGY_DISCOVERY_ASK("td"),
        TOPOLOGY_DISCOVERY_RESP("tdr"),
        INFORMATION_NEED_ASK("in"),
        INFORMATION_NEED_RESP("inr");


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
