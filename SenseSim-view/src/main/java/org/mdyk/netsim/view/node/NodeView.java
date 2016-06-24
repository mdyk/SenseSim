package org.mdyk.netsim.view.node;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.IDeviceModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.ArrayList;
import java.util.List;

/**
  Abstract class for node view

 */
public abstract class NodeView<C, P extends Position> {

    protected IDeviceModel<P> node;
    protected C nodeContainer;

    public NodeView(IDeviceModel node, C nodeContainer) {
        this.node = node;
        this.nodeContainer = nodeContainer;
        prepareView();
    }

    protected abstract void prepareView();

    /**
     * Renders node in provided nodeContainer
     */
    public abstract void renderNode();

    public abstract void relocate(P newPosition);

    protected abstract void relocateEdges(P newPosition);

    public int getID() {
        return node.getID();
    }

    @Deprecated
    public List<AbilityType> getAbilities() {
        return node.getAbilities();
    }

    public List<String> getSensorsNames() {
        List<String> sensorsNames = new ArrayList<>();

        for(SensorModel sensorModel : node.getSensors()) {
            sensorsNames.add(sensorModel.getName());
        }

        return sensorsNames;
    }

    public abstract void setEdge(IDeviceModel secondEndNode);

    public abstract void removeEdge(IDeviceModel secondEndNode);

    public abstract P getNodePosition();

    public IDeviceModel<P> getNode() {
        return node;
    }

//    public DeviceStatistics getStatistics() {
//
//    }

}
