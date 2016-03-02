package org.mdyk.netsim.view.node;


import org.mdyk.netsim.logic.node.geo.GeoDeviceNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.device.IDeviceModel;

public abstract class GeoNodeView<C> extends NodeView<C, GeoPosition> {

    public GeoNodeView(GeoDeviceNode node, C nodeContainer) {
        super((IDeviceModel) node, nodeContainer);
    }
}
