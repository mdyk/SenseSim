package org.mdyk.netsim.view.node;


import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import java.util.Map;

/**
 * Graphical representation of a node
 *
 * @author Michał Dyk (mdyk@wat.edu.pl)
 */
public class JFXNodeView /*extends NodeView*/ {

    public enum LineEnding {START, END}

    private static final Logger logger = Logger.getLogger(JFXNodeView.class);

    private StackPane pane;
    private Map<ISensorModel, Edge> nodeEdges;
    private ISensorModel node;
    private Position position;

    public JFXNodeView(ISensorModel node) {
//        super(node);
    }

    protected void prepareView() {
        pane = new StackPane();

        Text label = new Text(String.valueOf(node.getID()));
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        label.setFill(Color.WHITE);
        label.setStroke(Color.web("#7080A0"));

        Circle circle = new Circle(10, Color.BLUE);
        circle.relocate(node.getPosition().getPositionX(), node.getPosition().getPositionY());

        Circle radioRange = new Circle(node.getRadioRange(), Color.GREEN);
        radioRange.setOpacity(0.2);
        pane.getChildren().addAll(circle, label, radioRange);
    }

//    @Override
    public void renderNode(Object container) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Pane getNode() {
        return pane;
    }

    public void relocate(Position newPosition) {
        this.position = newPosition;
        pane.relocate(newPosition.getPositionX(), newPosition.getPositionY());
        relocateEdges(newPosition);
    }

    protected void relocateEdges(Position newPosition) {
        for(Edge edge : nodeEdges.values()) {
            switch(edge.getLineEnding()) {
                case START:
                    edge.getLine().setStartX(newPosition.getPositionX());
                    edge.getLine().setStartX(newPosition.getPositionY());
                    break;

                case END:
                    edge.getLine().setEndX(newPosition.getPositionX());
                    edge.getLine().setEndX(newPosition.getPositionY());
                    break;
            }
        }
    }

    public int getID() {
        return node.getID();
    }

//    @Override
    public void setEdge(ISensorModel secondEndNode) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Override
//    public void setEdge(ISensorModel secondEndNode, Line line, NodeView.Edge.LineEnding lineEnding) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    public void setEdge(ISensorModel secondEndNode, Line line, LineEnding lineEnding) {
        logger.debug(">> setEdge");
        logger.debug("secondEndNode id: " + secondEndNode.getID() + " lineEnding:  " + lineEnding.name() );
        Edge edge = new Edge(line, lineEnding);
        nodeEdges.put(secondEndNode, edge);
        relocateEdges(position);
        logger.debug("<< setEdge");
    }

    public void removeEdge(ISensorModel secondEndNode) {
        logger.debug(">> removeEdge");
        // TODO zweryfikować czy jest inny sposób usuwania
        nodeEdges.get(secondEndNode).getLine().setVisible(false);
        nodeEdges.remove(secondEndNode);
        logger.debug("<< removeEdge");
    }

    private static class Edge {

        private Line line;
        private LineEnding lineEnding;

        public Edge(Line line , LineEnding lineEnding) {
            this.line = line;
            this.lineEnding = lineEnding;
        }

        private Line getLine() {
            return line;
        }

        private LineEnding getLineEnding() {
            return lineEnding;
        }
    }

}
