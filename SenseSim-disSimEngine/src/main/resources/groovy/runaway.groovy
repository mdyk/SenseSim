package groovy

import org.mdyk.netsim.logic.node.api.SensorAPI
import org.mdyk.netsim.logic.util.GeoPosition
import org.mdyk.netsim.mathModel.ability.AbilityType
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue

SensorAPI sensorAPI = (SensorAPI) api;

def temp = 0;

while(temp < 100) {
    PhenomenonValue observation = sensorAPI.api_getCurrentObservation(AbilityType.TEMPERATURE);
    temp = (Integer) observation.getValue();
}

api.api_stopMove();

GeoPosition geoPosition = new GeoPosition(52.230684,21.004339);

List<GeoPosition> route = new ArrayList<>();
route.add(api.api_getPosition());
route.add(geoPosition);

api.api_setRoute(route);

api.api_startMove();

