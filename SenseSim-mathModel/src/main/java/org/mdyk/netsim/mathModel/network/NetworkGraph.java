package org.mdyk.netsim.mathModel.network;


import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.mathModel.device.IDeviceModel;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class NetworkGraph  {

    private UndirectedGraph<IDeviceModel, DefaultEdge> networkGraph;

    public NetworkGraph() {
        networkGraph = new ListenableUndirectedGraph<>( DefaultEdge.class );

        EventBusHolder.getEventBus().register(this);
    }

    public void addEdge(IDeviceModel a , IDeviceModel b) {
        networkGraph.addVertex(a);
        networkGraph.addVertex(b);

        networkGraph.addEdge(a,b);
        networkGraph.addEdge(b,a);
    }

    public void removeEdge(IDeviceModel a , IDeviceModel b) {

        networkGraph.removeEdge(a,b);
        networkGraph.removeEdge(b,a);
    }

    public boolean hasEdge(IDeviceModel a , IDeviceModel b) {
        return networkGraph.containsEdge(a,b) || networkGraph.containsEdge(b,a);
    }

    public List<IDeviceModel> listNeighbors(IDeviceModel a) {
        List<IDeviceModel> neighbors = new LinkedList<>();

        networkGraph.containsVertex(a);

        for(DefaultEdge edge : networkGraph.edgesOf(a)){
            IDeviceModel neighbor = networkGraph.getEdgeTarget(edge);

            // If edge target equals given device a, than we take edge source
            // as neighbor
            if (neighbor.equals(a)) {
                neighbor = networkGraph.getEdgeSource(edge);
            }

            neighbors.add(neighbor);
        }

        return neighbors;
    }

    public void clear() {
        networkGraph= new ListenableUndirectedGraph<>( DefaultEdge.class );
    }

    public void addVertex(IDeviceModel a) {
        networkGraph.addVertex(a);
    }
}
