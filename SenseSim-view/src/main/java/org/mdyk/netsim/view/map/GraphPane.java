package org.mdyk.netsim.view.map;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.mathModel.network.GraphEdge;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.mdyk.netsim.view.node.JFXNodeView;

import java.util.HashMap;
import java.util.Map;


/**
 * View component which shows network nodes.
 */
public class GraphPane extends Pane {

    private static final Logger logger = Logger.getLogger(GraphPane.class)     ;

    /**
     * Keeps node views. Keys are node ids.
     */
    private Map<Integer, JFXNodeView> nodeViews;

    public GraphPane() {
        super();
        setPrefSize(600, 200);
        nodeViews = new HashMap<>();
        EventBusHolder.getEventBus().register(this);
    }

    public void addNode(JFXNodeView node) {
        logger.debug(">> addNode");
        nodeViews.put(node.getID(),node);
        this.getChildren().add(node.getNode());
        logger.debug("<< addNode");
    }

    public Line addEdge() {

        final Line line = new Line();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                getChildren().add(line);
            }
        });
        return line;
    }

    @Subscribe
    public void processEvent(InternalEvent event) {
        logger.debug(">> processEvent");
        switch(event.getEventType()){
            case NEW_NODE:
                logger.debug("NEW_NODE event");
                addNode(new JFXNodeView((ISensorModel) event.getPayload()));
                break;
            case NODE_POSITION_CHANGED:
                logger.debug("NODE_POSITION_CHANGED event");
                ISensorModel node = (ISensorModel) event.getPayload();
                nodeViews.get(node.getID()).relocate(node.getPosition());
                break;
            case EDGE_ADDED:
                logger.debug("EDGE_ADDED event");
                GraphEdge newEdge = (GraphEdge) event.getPayload();

                Line line = addEdge();

                nodeViews.get(newEdge.idA.getID()).setEdge(newEdge.idA, line, JFXNodeView.LineEnding.START);
                nodeViews.get(newEdge.idB.getID()).setEdge(newEdge.idB, line, JFXNodeView.LineEnding.END);

                break;
        }
        logger.debug("<< processEvent");
    }
}
