package org.mdyk.netsim.logic.node.geo;

import org.mdyk.netsim.logic.util.Position;

import java.util.List;

/**
 * Interface for nodes with defined route.
 */
public interface RoutedNode<P extends Position> {

    /**
     * Returns list containing positions of node's route points.
     * @return
     *      list of route checkpoint positions.
     */
    public List<P> getRoute();

    public void setRoute(List<P> route);

}
