package org.mdyk.netsim.mathModel.network;


import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class NetworkGraph  {

    private UndirectedGraph<ISensorModel, DefaultEdge> networkGraph;

    public NetworkGraph() {
        networkGraph = new ListenableUndirectedGraph<>( DefaultEdge.class );

        EventBusHolder.getEventBus().register(this);
    }

    public void addEdge(ISensorModel a , ISensorModel b) {
        networkGraph.addVertex(a);
        networkGraph.addVertex(b);

        networkGraph.addEdge(a,b);
        networkGraph.addEdge(b,a);
    }

    public void removeEdge(ISensorModel a , ISensorModel b) {

        networkGraph.removeEdge(a,b);
        networkGraph.removeEdge(b,a);
    }

    public boolean hasEdge(ISensorModel a , ISensorModel b) {
        return networkGraph.containsEdge(a,b) || networkGraph.containsEdge(b,a);
    }

    public List<ISensorModel> listNeighbors(ISensorModel a) {
        List<ISensorModel> neighbors = new LinkedList<>();

        networkGraph.containsVertex(a);

        for(DefaultEdge edge : networkGraph.edgesOf(a)){
            ISensorModel neighbor = networkGraph.getEdgeTarget(edge);

            // If edge target equals given sensor a, than we take edge source
            // as neighbor
            if (neighbor.equals(a)) {
                neighbor = networkGraph.getEdgeSource(edge);
            }

            neighbors.add(neighbor);
        }

        return neighbors;
    }

    public void addVertex(ISensorModel a) {
        networkGraph.addVertex(a);
    }
}
