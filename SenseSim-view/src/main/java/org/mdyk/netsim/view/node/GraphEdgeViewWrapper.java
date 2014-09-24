package org.mdyk.netsim.view.node;

/**
 * Generic implementation of wrapper which represents
 * edge between two nodes
 */
public class GraphEdgeViewWrapper<V extends Object> {

    protected int nodeA;
    protected int nodeB;
    protected V view;

    /**
     *
     * @param nodeA
     *      one side of the edge.
     * @param nodeB
     *      second side of the edge.
     * @param view
     *      reference to view which represents the edge.
     */
    public GraphEdgeViewWrapper(int nodeA, int nodeB, V view) {
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.view = view;
    }

    public int getNodeA() {
        return nodeA;
    }

    public int getNodeB() {
        return nodeB;
    }

    /**
     * Returns reference to view implementation of the edge
     * @return
     *      reference to view implementation
     */
    public V getView() {
        return view;
    }
}
