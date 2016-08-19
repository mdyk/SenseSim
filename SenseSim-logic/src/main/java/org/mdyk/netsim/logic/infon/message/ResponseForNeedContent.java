package org.mdyk.netsim.logic.infon.message;

/**
 * Created by michal on 30.07.2016.
 */
public class ResponseForNeedContent {

    private int needId;
    private int askingNodeId;
    private String content;

    public ResponseForNeedContent(int needId, int askingNodeId , String content) {
        this.needId = needId;
        this.askingNodeId = askingNodeId;
        this.content = content;
    }

    public int getNeedId() {
        return needId;
    }

    public int getAskingNodeId() {
        return askingNodeId;
    }

    public String getContent() {
        return content;
    }


}
