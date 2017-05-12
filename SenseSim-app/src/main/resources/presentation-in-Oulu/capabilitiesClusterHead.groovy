package groovy

import groovy.transform.Field
import org.mdyk.netsim.logic.communication.Message
import org.mdyk.netsim.logic.node.api.DeviceAPI
import groovy.json.*
import org.mdyk.netsim.logic.node.program.groovy.GroovyProgram
import org.mdyk.netsim.mathModel.sensor.SensorModel

import java.util.function.Function

DeviceAPI device = (DeviceAPI) api;

@Field int HQDeviceId = 1

@Field List<Message> messagesToSend = new ArrayList<>()


device.api_setOnMessageHandler(new Function<Message, Object>() {
    @Override
    Object apply(Message message) {

        println("recieved message " + message.ID)

        Object messageContent = message.getMessageContent();

//        println("Received message " + message.getID() + ". Adding to queue.")
//        messagesToSend.add(message)
        sendMessageToHQ(message , device)
        
        return null;
    }
});

def sendMessageToHQ(Message message , DeviceAPI device) {
    Map<Integer , String> commInterfaces =  device.api_listCommunicationInterfaces();

    def interfaceId = 0
    for (Integer commId : commInterfaces.keySet()) {
        if(commInterfaces.get(commId).contains("Tactical")) {
            interfaceId = commId
        }
    }



    if(interfaceId !=0 ) {
        println("send to HQ message: " + message.getID())
        device.api_sendMessage(message.getID() , device.api_getMyID() , HQDeviceId , interfaceId , message.getMessageContent() , message.getSize())
    }

}