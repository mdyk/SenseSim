package groovy
//package org.mdyk.netsim.logic.communication.groovyScripts

import org.mdyk.netsim.logic.node.api.DeviceAPI;

DeviceAPI sensorAPI = (DeviceAPI) api;

sensorAPI.api_stopMove();

List<Integer> neighbours = sensorAPI.api_scanForNeighbors();


(sensorAPI.api_getMyID() == 1) {

    sensorAPI.api_stopMove();
}

sensorAPI.api_setRoute()

String scriptContent = "api.api_stopMove();";

for (Integer neighbour : neighbours) {
    sensorAPI.api_sendMessage(sensorAPI.api_getMyID(), neighbour, scriptContent , null );
}


System.out.println(api.api_getMyID());
System.out.println(api.api_scanForNeighbors());
