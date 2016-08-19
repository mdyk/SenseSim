package org.mdyk.netsim.logic.infon.message;

/**
 * Created by michal on 30.07.2016.
 */
public class ResponseForNeedContent {

    private int askingNodeId;
    private String content;

    public ResponseForNeedContent(int askingNodeId , String content) {
        this.askingNodeId = askingNodeId;
        this.content = content;
    }

    public int getAskingNodeId() {
        return askingNodeId;
    }

    public String getContent() {
        return content;
    }
}
