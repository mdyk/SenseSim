package org.mdyk.netsim.view.controller;

import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.view.node.OSMNodeView;

import javafx.scene.control.CheckBox;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;


public class ProgramConsoleController implements Initializable {

    private static final Logger LOG = Logger.getLogger(ProgramConsoleController.class);

    private HashMap<Integer, OSMNodeView> nodeViews;

    @FXML
    private TextArea codeArea;

    @FXML
    private TextArea outputArea;

    @FXML
    private ComboBox<Integer> nodesCombo;

    @FXML
    private CheckBox sendToAll;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBusHolder.getEventBus().register(this);
    }

    public void populateDevices(HashMap<Integer , OSMNodeView> nodeViews) {
        this.nodeViews = nodeViews;
        nodesCombo.setItems(FXCollections.observableArrayList(nodeViews.keySet()));
    }

    public void testProgram() {
        String programCode = codeArea.getText();

        if(nodesCombo.getValue() != null) {
            EventBusHolder.getEventBus().post(EventFactory.testProgram(nodesCombo.getValue(),programCode));
        }
    }

    public void installProgram() {

        String programCode = codeArea.getText();

        if(nodesCombo.getValue() != null) {
            EventBusHolder.getEventBus().post(EventFactory.loadProgram(nodesCombo.getValue(),programCode));
        }

    }

    @Subscribe
    public void processEvents(InternalEvent event) {
        try{
            switch (event.getEventType()) {
                case END_TEST_PROGRAM_EXECUTION:
                    ByteArrayOutputStream outputStream = (ByteArrayOutputStream) event.getPayload();

                    this.outputArea.setText(outputStream.toString());

                    break;
            }
        } catch (Exception exc) {
            LOG.error(exc.getMessage() , exc);
        }
    }

}
