/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mdyk.sensesim.simulation.engine.dissim.plugins;

import java.util.List;
import net.xeoh.plugins.base.Plugin;
import org.mdyk.netsim.logic.network.NetworkManager;
import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.node.DevicesFactory;

/**
 *
 * @author Marcin Antczak <marcin@antczak.xyz>
 */
public interface IRealDevicePlugin extends Plugin {
    void loadDevices(List<Device> nodeList);
    void updateDevices();
    void setDeviceFactory(DevicesFactory devicesFactory);
}
