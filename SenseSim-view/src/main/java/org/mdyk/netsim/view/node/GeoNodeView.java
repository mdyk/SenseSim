package org.mdyk.netsim.view.node;


import org.mdyk.netsim.logic.node.geo.GeoSensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

public abstract class GeoNodeView<C> extends NodeView<C, GeoPosition> {

    public GeoNodeView(GeoSensorNode node, C nodeContainer) {
        super((ISensorModel) node, nodeContainer);
    }
}
