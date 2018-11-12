package org.mdyk.netsim.logic.node.program.owl.messages;


import org.json.JSONArray;
import org.json.JSONObject;
import org.mdyk.netsim.logic.infon.Infon;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;

public class InformationNeedRespMessage implements InformationNeedMessage {

    private final MessageParser.MessageType messageType = MessageParser.MessageType.INFORMATION_NEED_RESP;
    private final List<Infon> infons = new ArrayList<>();
    @Deprecated
    private Infon infon = null;
    private int sourceNode;
    private int id;
    private List<Integer> processedInNodes;

    public InformationNeedRespMessage(int sourceNode, int informationNeedId, Infon infon) {
        this.infon = infon;
        processedInNodes = new ArrayList<>();
        id = informationNeedId;
        this.sourceNode = sourceNode;
    }

    public InformationNeedRespMessage(String sourceNode, String needId, JSONArray processedInNodes) {
//        this(Integer.parseInt(sourceNode), Integer.parseInt(needId) , infon);
        this.processedInNodes = new ArrayList<>();
        id = Integer.parseInt(needId);
        this.sourceNode = Integer.parseInt(sourceNode);

        for(int i=0; i < processedInNodes.length(); i++){
            this.processedInNodes.add(Integer.parseInt(String.valueOf(processedInNodes.get(i))));
        }

    }

    public InformationNeedRespMessage(String sourceNode, String needId, Infon infon, JSONArray processedInNodes) {
        this(Integer.parseInt(sourceNode), Integer.parseInt(needId) , infon);

        for(int i=0; i < processedInNodes.length(); i++){
            this.processedInNodes.add(Integer.parseInt(String.valueOf(processedInNodes.get(i))));
        }

    }


    @Override
    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MessageParser.MESSAGE_ACTION_KEY, messageType.getJsonValue());

        if(infons.size()>0) {
            StringBuilder sb = new StringBuilder();
            for(Infon infon : infons) {
                sb.append(infon.toString());
                sb.append(";");
            }
            sb.deleteCharAt(sb.length()-1);
            jsonObject.put(MessageParser.INFON , sb.toString());
        } else {
            jsonObject.put(MessageParser.INFON , infon.toString());
        }

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

    @Deprecated
    public Infon getInfon() {
        return infon;
    }

    public void addInfon(Infon infon) {
        this.infons.add(infon);
    }

    public void addInfon(List<Infon> infons) {
        this.infons.addAll(infons);
    }

    public List<Infon> getInfons() {
        return infons;
    }

    public void procecessedInNode(int nodeId) {
        this.processedInNodes.add(nodeId);
    }

    public List<Integer> getProcessedInNodes() {
        return processedInNodes;
    }

}
