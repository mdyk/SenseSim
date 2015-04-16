package org.mdyk.netsim.logic.event;


public enum EventType {

    NODE_START_SENSE,
    NODE_END_SENSE,
    START_PROGRAM_EXECUTION,
    END_PROGRAM_EXECUTION,

    PHENOMENON_CREATED,
    SCENARIO_LOADED,

    SIM_PAUSE_NODES,
    SIM_RESUME_NODES,
    SIM_STOP_NODES,
    SIM_START_NODES,

    NEW_NODE,
    EDGE_ADDED,
    EDGE_REMOVED,
    NODE_POSITION_CHANGED
}
