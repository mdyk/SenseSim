package groovy

import groovy.transform.Field
import org.mdyk.netsim.logic.node.api.DeviceAPI
import groovy.json.*
import org.mdyk.netsim.mathModel.sensor.SensorModel

DeviceAPI device = (DeviceAPI) api;

@Field int equipmentIdCount = 1


class Soldier {

    String name;
    Long unitId;
    double latitude;
    double longitude;
    List<SoldierEquipment> equipment = new ArrayList<>();

}

class SoldierEquipment {
    Long equipmentId
    String equipmentName
    String rfidTag
    int quantity
}


def updateSoldier(Soldier soldier, DeviceAPI device) {
    soldier.latitude = device.api_getPosition().getPositionX()
    soldier.longitude = device.api_getPosition().getPositionY()
    
    String[] rfidTags;
    for(SensorModel sensorModel: device.api_getSensorsList()) {
        if (sensorModel.getName().contains("RFID")) {
            rfidTags = device.api_getSensorCurrentObservation(sensorModel).getStringValue().split("\n")

            for (String tag : rfidTags) {
                def equipment = new SoldierEquipment();
                equipment.equipmentId = equipmentIdCount
                equipmentIdCount++
                equipment.equipmentName = tag
                equipment.rfidTag = tag
                equipment.quantity = 1

                soldier.equipment.add(equipment)

            }

        }
    }
}

def sendMessageWithUpdate(Soldier soldier, DeviceAPI device) {
    Map<Integer , String> commInterfaces =  device.api_listCommunicationInterfaces();

    def interfaceId = 0
    for (Integer commId : commInterfaces.keySet()) {
        println(commId + " " + commInterfaces.get(commId) )
        if(commInterfaces.get(commId).contains("WiFi")) {
            interfaceId = commId
        }
    }

    if (interfaceId != 0) {
        List<Integer> neighbours = device.api_scanForNeighbors(interfaceId)
        println(neighbours)

        for (Integer nodeId : neighbours) {
            def message = new JsonBuilder( soldier )
            device.api_sendMessage((int)System.currentTimeMillis(), device.api_getMyID() , nodeId, interfaceId , message.toPrettyString() , message.toPrettyString().getBytes().length);
        }

    }
}


def thisSoldier = new Soldier()
thisSoldier.unitId = device.api_getMyID()
thisSoldier.name = device.api_getName()
thisSoldier.latitude = device.api_getPosition().getPositionX()
thisSoldier.longitude = device.api_getPosition().getPositionY()

def count = 1

while(count < 100) {

    updateSoldier(thisSoldier , device)
    sendMessageWithUpdate(thisSoldier, device)

    sleep(10000)
    count ++

}
