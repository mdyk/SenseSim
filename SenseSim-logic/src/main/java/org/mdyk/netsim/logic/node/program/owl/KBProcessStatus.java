package org.mdyk.netsim.logic.node.program.owl;



public class KBProcessStatus {


    private String name;
    private Status status;

    KBProcessStatus(Status status, String name) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        CLASS_UNKNOWN,
        RELATION_UNKNOWN
    }
}
