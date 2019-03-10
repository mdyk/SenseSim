package org.mdyk.netsim.logic.node.program.owl;


public enum INProcessStatus  {

    // Zapytaj o nieznaną relację
    ASK_FOR_RELATION,

    // Zapytaj o nieznane obiekty
    ASK_FOR_OBJECTS,

    // Wyślij zapytanie o nieznane obiekty
    ASK_FOR_OBJECTS_AND_RELATION,

    LOCALIZATION_NOT_IMPORTANT,

    LOCALIZATION_IMPORTANT,

    RESEND,
    PROCESS_IN_NODE,
    UPDATE_TOPOLOGY;

}
