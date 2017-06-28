package groovy_ch

import groovy.transform.Field
import org.mdyk.netsim.logic.node.api.DeviceAPI
import groovy.json.*
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace
import org.mdyk.netsim.mathModel.observer.ecg.EcgConfigurationSpace
import org.mdyk.netsim.mathModel.observer.so2.SaturationConfigurationSpace
import org.mdyk.netsim.mathModel.observer.temperature.TemperatureConfigurationSpace
import org.mdyk.netsim.mathModel.sensor.SensorModel

DeviceAPI device = (DeviceAPI) api;

@Field int equipmentIdCount = 1


@Field String HEALTHY = "HEALTHY"
@Field String NEEDS_TREATMENT = "NEEDS_TREATMENT"
@Field String EXHAUSTED = "EXHAUSTED"
@Field String CRITICAL_CONDITION = "CRITICAL_CONDITION"
@Field String UNKNOWN = "UNKNOWN"

@Field Map<String, String> rfidMapping = new HashMap<>();
rfidMapping.put("urn:epc:id:sgtin:0714141.912345.6789" , "MSBS 5,56x45 mm rifle");
rfidMapping.put("urn:epc:id:sgtin:0714141.897345.6081" , "5,56x45 mm 30 rnds magazine");
rfidMapping.put("urn:epc:id:sgtin:1214752.907970.2143" , "RGZ-89 HE hand grenade");
rfidMapping.put("urn:epc:id:sgtin:2060037.907719.2345" , "Tactical knife");
rfidMapping.put("urn:epc:id:sgtin:7233818.917981.7009" , "Ragun 9x19 mm pistol");
rfidMapping.put("urn:epc:id:sgtin:9225686.701917.6886" , "MRE Z1");
rfidMapping.put("urn:epc:id:sgtin:7778077.784361.5695" , "MRE W1");
rfidMapping.put("urn:epc:id:sgtin:7910443.798972.7695" , "MRE R2");
rfidMapping.put("urn:epc:id:sgtin:7384921.251397.1935" , "Water camelbag");
rfidMapping.put("urn:epc:id:sgtin:3172857.166353.5259" , "UKM 2000 7,62x51 mm machine gun");
rfidMapping.put("urn:epc:id:sgtin:5882302.174710.5542" , "Sako TRG 7,62x51 mm sniper rifle");
rfidMapping.put("urn:epc:id:sgtin:8924707.483581.9973" , "RGP-40 40x46 mm automatic grenade launcher");
rfidMapping.put("urn:epc:id:sgtin:9971254.755813.9976" , "40x46 mm HE grenade");
rfidMapping.put("urn:epc:id:sgtin:6590553.796988.3183" , "40x46 mm sponge grenade");
rfidMapping.put("urn:epc:id:sgtin:4095162.333026.3913" , "40x46 mm CS gas grenade");
rfidMapping.put("urn:epc:id:sgtin:6935615.310213.1991" , "40x46 mm smoke grenade");
rfidMapping.put("urn:epc:id:sgtin:7982950.860339.8660" , "Riot shield");
rfidMapping.put("urn:epc:id:sgtin:2549703.875699.8004" , "Telescopic baton");
rfidMapping.put("urn:epc:id:sgtin:3342616.987419.4509" , "Compact Metal Detector");
rfidMapping.put("urn:epc:id:sgtin:3127438.626491.4427" , "Tactical vest");
rfidMapping.put("urn:epc:id:sgtin:0168755.636426.4745" , "Kevlar helmet");






class Soldier {

    String name
    Long unitId
    double latitude
    double longitude
    List<SoldierEquipment> equipment = new ArrayList<>()
    String healthStatus

    def getEquipmentByTag(String tag) {

        SoldierEquipment foundEq = null

        for (SoldierEquipment equipment : this.equipment) {
            if(equipment.rfidTag == tag) {
                foundEq = equipment
            }
        }

        return foundEq
    }

}

class SoldierEquipment {
    Long equipmentId
    String equipmentName
    String rfidTag
    int quantity
}


def updateSoldierEquipment(Soldier soldier, DeviceAPI device) {
    soldier.latitude = device.api_getPosition().getPositionX()
    soldier.longitude = device.api_getPosition().getPositionY()

    // Wyzerowanie stanów
    for (SoldierEquipment soldierEquipment : soldier.equipment) {
        soldierEquipment.quantity = 0
    }

    String[] rfidTags;
    for(SensorModel sensorModel: device.api_getSensorsList()) {
        if (sensorModel.getName().contains("RFID")) {

            if(device.api_getSensorCurrentObservation(sensorModel) != null) {

                rfidTags = device.api_getSensorCurrentObservation(sensorModel).getStringValue().split("\n")

                for (String tag : rfidTags) {

                    SoldierEquipment equipment = soldier.getEquipmentByTag(tag)

                    if(equipment != null) {
                        equipment.quantity += 1
                    } else {
                        def newEquipment = new SoldierEquipment();
                        newEquipment.equipmentId = equipmentIdCount
                        equipmentIdCount++

                        if(rfidMapping.get(tag) != null) {
                            newEquipment.equipmentName = rfidMapping.get(tag)
                        } else {
                            newEquipment.equipmentName = "Unknown"
                        }

                        newEquipment.rfidTag = tag
                        newEquipment.quantity = 1
                        soldier.equipment.add(newEquipment)
                    }
                }
            }
        }
    }
}


def updateSoldierHealth(Soldier soldier, DeviceAPI device) {
    soldier.healthStatus = this.HEALTHY


    for (SensorModel sensorModel :device.api_getSensorsList()) {
        print(sensorModel.getName())

        if(sensorModel.getName().contains("Temperature")) {
            println("Temperature")
            List<ConfigurationSpace> observations = device.api_getObservations(sensorModel.getConfigurationSpaceClass() , 100);

            double temp = 0

            for(ConfigurationSpace observation : observations) {
                TemperatureConfigurationSpace temperatureObservation = observation as TemperatureConfigurationSpace
                temp += temperatureObservation.getTemperature()
            }

            double avgTemp = temp / observations.size()
            println("avgTemp = " + avgTemp)

            if(avgTemp < 36) {
                soldier.healthStatus = this.CRITICAL_CONDITION
            } else if ((soldier.healthStatus == this.HEALTHY || soldier.healthStatus == this.EXHAUSTED) && avgTemp > 37) {
                soldier.healthStatus = this.NEEDS_TREATMENT
            }

        }

        if(sensorModel.getName().contains("Ecg")) {
            List<ConfigurationSpace> observations = device.api_getObservations(sensorModel.getConfigurationSpaceClass() , 200);
            println("ECG")
            double milivolts = 0

            for(ConfigurationSpace observation : observations) {
                EcgConfigurationSpace ecgObservation = observation as EcgConfigurationSpace
                milivolts += Math.abs(ecgObservation.getMilivolts())
            }

            double avgMilivots = milivolts / observations.size()

            println("avgTemp = " + avgMilivots)

            if(avgMilivots == 0) {
                println("avgMilivots == 0 " + this.CRITICAL_CONDITION)
                soldier.healthStatus = this.CRITICAL_CONDITION
            }

        }

        if(sensorModel.getName().contains("Pulse")) {
            println("Pulse")
            List<ConfigurationSpace> observations = device.api_getObservations(sensorModel.getConfigurationSpaceClass() , 100);

            double saO2 = 0

            for(ConfigurationSpace observation : observations) {
                SaturationConfigurationSpace saturation = observation as SaturationConfigurationSpace
                saO2 += saturation.getSaturation()
            }

            double avgSaO2 = saO2 / observations.size()

            println("avgSaO2 = " + avgSaO2)

            if(soldier.healthStatus == this.HEALTHY && avgSaO2 < 93 && avgSaO2 > 92) {
                soldier.healthStatus = this.EXHAUSTED
            } else if ((soldier.healthStatus == this.HEALTHY || soldier.healthStatus == this.EXHAUSTED) && (avgSaO2 > 98 || avgSaO2 < 94)) {
                soldier.healthStatus = this.NEEDS_TREATMENT

            }
        }
    }

    println(soldier.healthStatus)

}


def  sendMessageWithUpdate(Soldier soldier, DeviceAPI device) {
    Map<Integer , String> commInterfaces =  device.api_listCommunicationInterfaces();

    def interfaceId = 0
    for (Integer commId : commInterfaces.keySet()) {
        if(commInterfaces.get(commId).contains("Tactical")) {
            interfaceId = commId
        }
    }

    if (interfaceId != 0) {
        List<Integer> neighbours = device.api_scanForNeighbors(interfaceId)

        if(neighbours.contains(1)) {
            def message = new JsonBuilder(soldier)
            device.api_sendMessage(System.currentTimeMillis() + (device.api_getMyID() * 100000), device.api_getMyID(), 1, interfaceId, message.toPrettyString(), message.toPrettyString().getBytes().length);
            println("sending message to commander ")
            println(message.toPrettyString())
        }

    }
}


def thisSoldier = new Soldier()
thisSoldier.unitId = device.api_getMyID()
thisSoldier.name = device.api_getName()
thisSoldier.latitude = device.api_getPosition().getPositionX()
thisSoldier.longitude = device.api_getPosition().getPositionY()
thisSoldier.healthStatus = this.HEALTHY


while(true) {
    println("---------------------------------------------------------------------------------")
    this.updateSoldierEquipment(thisSoldier , device)
    this.updateSoldierHealth(thisSoldier,device)
    this.sendMessageWithUpdate(thisSoldier, device)
    println("---------------------------------------------------------------------------------")
    sleep(5000 + ( (int )(Math.random() + 10) * 10))

}

