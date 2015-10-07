package org.mdyk.netsim.view.controller;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.view.node.NodeView;
import org.mdyk.netsim.view.node.OSMNodeView;

import java.net.URL;
import java.util.ResourceBundle;


public class SensorConsoleController implements Initializable {

    private static final Logger LOG = Logger.getLogger(SensorConsoleController.class);

    private NodeView nodeView;

    @FXML
    private TextField nodeBandwidth;

    @FXML
    private TextField latitude;

    @FXML
    private TextField longitude;

    @FXML
    private TextField velocity;

    @FXML
    private Label consoleLabel;

    @FXML
    private TextField radioRange;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBusHolder.getEventBus().register(this);
    }


    public void fillGui() {
        consoleLabel.setText("Sensor " + nodeView.getID());
        nodeBandwidth.setText(Double.toString(nodeView.getNode().getWirelessBandwith()));
        velocity.setText(Double.toString(nodeView.getNode().getVelocity()));
        radioRange.setText(Double.toString(nodeView.getNode().getRadioRange()));
        actualizePositionLabel();
    }

    public void setNodeView(NodeView nodeView) {
        this.nodeView = nodeView;
    }


    @Subscribe
    public synchronized void processEvent(InternalEvent event) {
        LOG.debug(">> processEvent");
        switch(event.getEventType()){
            case NODE_POSITION_CHANGED:
                actualizePositionLabel();
                break;
        }
        LOG.debug("<< processEvent");
    }

    private void actualizePositionLabel() {
        OSMNodeView osmNodeView = (OSMNodeView) nodeView;
        Platform.runLater(() -> {
            latitude.setText(Double.toString(osmNodeView.getLat()));
            longitude.setText(Double.toString(osmNodeView.getLon()));
        });
    }

}
