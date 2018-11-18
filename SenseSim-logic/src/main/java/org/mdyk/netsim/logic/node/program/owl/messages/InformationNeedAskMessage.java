package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONArray;
import org.json.JSONObject;
import org.mdyk.netsim.logic.infon.Infon;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InformationNeedAskMessage implements InformationNeedMessage {

    private final MessageParser.MessageType messageType = MessageParser.MessageType.INFORMATION_NEED_ASK;
    private final Infon infon;
    private int sourceNode;
    private List<Integer> processedInNodes;
    private UUID id;


    public InformationNeedAskMessage(int sourceNode , Infon infon) {
        this.infon = infon;
        this.sourceNode = sourceNode;
        processedInNodes = new ArrayList<>();
        this.id = UUID.randomUUID();
    }

    public InformationNeedAskMessage(String sourceNode, Infon infon, JSONArray processedInNodes) {
        this(Integer.parseInt(sourceNode) , infon);

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

    public Infon getInfon() {
        return infon;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public void processedInNode(int nodeId) {
        if(!processedInNodes.contains(nodeId)){
            processedInNodes.add(nodeId);
        }
    }

    public List<Integer> getProcessedInNodes() {
        return processedInNodes;
    }

    public boolean wasProcessedBy(int nodeId) {
        return processedInNodes.contains(nodeId);
    }

    public int getId() {
//        id.hashCode();
//        int result = messageType.hashCode();
//        result = 31 * result + infon.hashCode();
//        result = 31 * result + sourceNode;
        return id.hashCode();
    }

}
