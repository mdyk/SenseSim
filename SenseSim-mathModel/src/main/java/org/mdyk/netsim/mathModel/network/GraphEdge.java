package org.mdyk.netsim.mathModel.network;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.device.IDeviceModel;

public class GraphEdge<P extends Position> {

    public IDeviceModel<P> idA;
    public IDeviceModel<P> idB;

    public Class<P> positionType;

    public GraphEdge(IDeviceModel<P> idA, IDeviceModel<P> idB) {
        this.idA = idA;
        this.idB = idB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphEdge graphEdge = (GraphEdge) o;

        return idA.getID() == graphEdge.idA.getID() || idA.getID() == graphEdge.idB.getID()
                && idB.getID() == graphEdge.idA.getID() || idB.getID() == graphEdge.idB.getID();

    }

    @Override
    public String toString() {
        return "GraphEdge{" +
                "idA=" + idA.getID() +
                ", idB=" + idB.getID() +
                ", positionType="+
                '}';
    }

    @Override
    public int hashCode() {
        int result = idA.getID();
        result = 31 * (result + idB.getID());
        return result;
    }
}
