/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mdyk.sensesim.simulation.engine.dissim.plugins;

import com.google.common.eventbus.Subscribe;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.Timer;
import org.mdyk.netsim.logic.communication.message.SimpleMessage;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.node.DevicesFactory;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Marcin Antczak <marcin@antczak.xyz>
 */
@PluginImplementation
public class RealDevicePlugin implements IRealDevicePlugin{

    private DevicesFactory devicesFactory;
    private List<Device> nodeList;
    private HashMap<Integer, Device> realDevices = new HashMap<>();
    
    public RealDevicePlugin(){
        EventBusHolder.getEventBus().register(this);
    }  
    
    @Override
    public void loadDevices(List<Device> nodeList) {
//        runServer();
//        this.nodeList = nodeList;
//        DeviceManager.addFakeDevices(nodeList);
        
    }

    @Override
    @Timer(period=1000)
    public void updateDevices() {
    }

    private void runServer(){
//        new Thread(new Runnable(){
//            @Override
//            public void run(){
////                try {
////                    Main.main(new String[]{});
////                } catch (Exception ex) {
////                    Logger.getLogger(RealDevicePlugin.class.getName()).log(Level.SEVERE, null, ex);
////                }
//            }
//        }).start();
    }
    
    @Override
    public void setDeviceFactory(DevicesFactory devicesFactory) {
        this.devicesFactory = devicesFactory;
    }
    
    @Subscribe
    public void processEvent(InternalEvent event) {
//        pl.edu.wat.integrator.Device device;
//        switch(event.getEventType()){
//            case NEW_REAL_DEVICE:
//                device = (pl.edu.wat.integrator.Device)event.getPayload();
//                addRealDevice(findDeviceById(device.getId()));
//                break;
//            case REAL_DEVICE_POSITION_CHANGED:
//                device = (pl.edu.wat.integrator.Device)event.getPayload();
//                updateDevice(device);
//                break;
//        }
    }
    
    private void addRealDevice(Device d){
//        if (d != null){
//            realDevices.put(d.getDeviceAPI().api_getMyID(), d);
//            Function<Message , Object> handler = h ->{
//                SimpleMessage msg = new SimpleMessage(h.getID(), h.getMessageSource(), h.getMessageDest(), h.getMessageContent(), h.getSize());
//                try {
//                    sendMessageToRealDevice(msg);
//                } catch (Exception ex) {
////                    Logger.getLogger(RealDevicePlugin.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                return null;
//            };
//            d.getDeviceAPI().api_setOnMessageHandler(handler);
//        }
    }
    
    private Device findDeviceById(int id){
//        for(Device device: nodeList){
//            if (device.getDeviceAPI().api_getMyID() == id)
//                return device;
//        }
        return null;
    }
    
    private boolean isRealDevice(Device d){
//        return realDevices.containsKey(d.getDeviceAPI().api_getMyID());
        return true;
    }
//    private void updateDevice(pl.edu.wat.integrator.Device device){
////        Device updateDevice = findDeviceById(device.getId());
////        if (updateDevice != null && isRealDevice(updateDevice)){
////            updateDevice.getDeviceLogic().setPosition(new GeoPosition(device.getLocation().getX(), device.getLocation().getY()));
////            EventBusHolder.post(EventType.NODE_POSITION_CHANGED, updateDevice);
////        }
//    }
//
    private void sendMessageToRealDevice(SimpleMessage msg) throws Exception{
//        if (isRealDevice(findDeviceById(msg.getMessageDest()))){
//            DeviceManager.sendMessageToDevice(msg.getMessageDest(), msg);
//        }
    }
    
}
