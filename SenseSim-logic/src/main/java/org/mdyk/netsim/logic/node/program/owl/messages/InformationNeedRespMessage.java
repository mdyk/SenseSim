package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONArray;
import org.json.JSONObject;
import org.mdyk.netsim.logic.infon.Infon;

import java.util.ArrayList;
import java.util.List;

public class InformationNeedRespMessage implements InformationNeedMessage {

    private final MessageParser.MessageType messageType = MessageParser.MessageType.INFORMATION_NEED_RESP;
    private final Infon infon;
    private int sourceNode;
    private int id;
    private List<Integer> processedInNodes;

    public InformationNeedRespMessage(int sourceNode, int informationNeedId, Infon infon) {
        this.infon = infon;
        processedInNodes = new ArrayList<>();
        id = informationNeedId;
        this.sourceNode = sourceNode;
    }

    public InformationNeedRespMessage(String sourceNode, String needId, Infon infon, JSONArray processedInNodes) {
        this(Integer.parseInt(sourceNode), Integer.parseInt(needId) , infon);

        for(int ii=0; ii < processedInNodes.length(); ii++){
//            System.out.println(processedInNodes.getJSONObject(ii);
            this.processedInNodes.add(Integer.parseInt(String.valueOf(processedInNodes.get(ii))));
        }

    }


    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageParser.MESSAGE_ACTION_KEY, messageType.getJsonValue());
        jsonObject.put(MessageParser.INFON , infon.toString());
        jsonObject.put(MessageParser.SOURCE_NODE_KEY , sourceNode);
        jsonObject.put(MessageParser.NEED_ID , getId());
        jsonObject.put(MessageParser.PROCESSED_NODES, processedInNodes);
        return jsonObject.toString();
    }

    @Override
    public MessageParser.MessageType getMessageType() {
        return messageType;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public Infon getInfon() {
        return infon;
    }
}
