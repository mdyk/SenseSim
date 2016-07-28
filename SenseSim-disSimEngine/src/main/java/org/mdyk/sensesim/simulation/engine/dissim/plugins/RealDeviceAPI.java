/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mdyk.sensesim.simulation.engine.dissim.plugins;

import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.api.DisSimDeviceAPI;

/**
 *
 * @author Marcin Antczak <marcin@antczak.xyz>
 */
public class RealDeviceAPI extends DisSimDeviceAPI {
    
    public RealDeviceAPI(DeviceSimEntity deviceSimEntity) {
        super(deviceSimEntity);
    }

    @Override
    public void api_sendMessage(int messageId, int originSource, int originDest, Object content, Integer size) {
        System.out.println("---------------------------------------------------"+size);
        super.api_sendMessage(messageId, originSource, originDest, content, size);
    }
    
    
    
}
