package org.mdyk.netsim.view.controller;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.view.node.OSMNodeView;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class InformationNeedConsoleController implements Initializable {

    private static final Logger LOG = Logger.getLogger(InformationNeedConsoleController.class);

    @FXML
    private TextArea informationNeed;

    @FXML
    private TextArea response;

    @FXML
    private ComboBox sendingDevice;

    private HashMap<Integer , OSMNodeView> nodes;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBusHolder.getEventBus().register(this);
    }

    public void populateDevices(HashMap<Integer , OSMNodeView> nodes) {
        this.nodes = nodes;
        sendingDevice.setItems(FXCollections.observableArrayList(nodes.keySet()));
    }

    public void sendInformationNeed() {
        LOG.trace(">> sendInformationNeed");
        Integer selectedNode = (Integer) sendingDevice.getValue();

        EventBusHolder.post(EventFactory.sendInformationNeed(selectedNode , informationNeed.getText()));

        LOG.trace("<< sendInformationNeed");
    }

    public void updateResponse() {

    }

}
