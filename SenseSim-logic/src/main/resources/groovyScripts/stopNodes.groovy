//package groovyScripts

import org.mdyk.netsim.logic.node.api.SensorAPI;

SensorAPI sensorAPI = (SensorAPI) api;

sensorAPI.api_stopMove();

List<Integer> neighbours = sensorAPI.api_scanForNeighbors();


String scriptContent = "api.api_stopMove();";

for (Integer neighbour : neighbours) {
    sensorAPI.api_sendMessage(sensorAPI.api_getMyID(), neighbour, scriptContent , null );
}


System.out.println(api.api_getMyID());
System.out.println(api.api_scanForNeighbors());
