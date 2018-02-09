package groovy

import org.mdyk.netsim.logic.communication.Message
import org.mdyk.netsim.logic.node.api.DeviceAPI
import groovy.json.*
import sensesim.integration.mcop.MCopPlugin

import java.util.function.Function


DeviceAPI device = (DeviceAPI) api
MCopPlugin plugin = (MCopPlugin) mCopPlugin

device.api_setOnMessageHandler(new Function<Message, Object>() {
    @Override
    Object apply(Message message) {

        String messageJson = (String) message.getMessageContent()

        updateSoldierState(messageJson , plugin)

        return null
    }
})

static def updateSoldierState(String json, MCopPlugin plugin) {
    def slurper = new JsonSlurper()
    def soldierUpdate = slurper.parseText(json)

    Long unitId = soldierUpdate.unitId
    Double latitude = soldierUpdate.latitude
    Double longitude = soldierUpdate.longitude
    String healthState = soldierUpdate.healthStatus

    plugin.updateUnitPosition(unitId, latitude , longitude)

    plugin.updateUnitHealthStatus(unitId , healthState)

    ArrayList equipmentList = (ArrayList) soldierUpdate.equipment

    for (int i = 0 ; i < equipmentList.size() ; i++) {

        Long equipmentId = equipmentList.get(i).equipmentId
        Integer qty = equipmentList.get(i).quantity
        if(plugin.hasEquipment(unitId , equipmentId)) {
            plugin.updateUnitEquipment(unitId, equipmentId , qty)
        } else {
            plugin.addEquipment(unitId, equipmentId , (String) equipmentList.get(i).equipmentName , qty)
        }

    }
}
