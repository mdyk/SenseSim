package org.mdyk.netsim.view.node;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.node.geo.GeoDeviceNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.device.IDeviceModel;
import org.mdyk.netsim.view.controller.SensorConsoleController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import java.awt.*;
import java.io.IOException;

/**
 * Node implementation as mark in Open Street Map (JMapViewer)
 */
public class OSMNodeView extends GeoNodeView<JMapViewer> {

    private static final Logger LOG = Logger.getLogger(OSMNodeView.class);

    private MapMarkerDot mapMaker;
    private Stage sensorConsole;

    public OSMNodeView(GeoDeviceNode node, JMapViewer mapViewer) {
        super(node, mapViewer);
        mapMaker = new MapMarkerDot(node.getLatitude(), node.getLongitude());
        mapMaker.setBackColor(Color.YELLOW);
        mapMaker.setName(String.valueOf(node.getID()));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/SensorConsoleUI.fxml"));

        try {
            Parent parent = fxmlLoader.load();
            sensorConsole = new Stage();
            sensorConsole.initModality(Modality.NONE);
            sensorConsole.initStyle(StageStyle.DECORATED);
            sensorConsole.setTitle("Device " + this.getNode().getID());
            sensorConsole.setScene(new Scene(parent));
            SensorConsoleController scc = fxmlLoader.getController();
            scc.setNodeView(this);
            scc.fillGui();
        } catch (IOException e) {
            LOG.error(e.getMessage() , e);
        }

    }

    @Override
    protected void prepareView() {
        // EMPTY
    }

    @Override
    public void renderNode() {
        nodeContainer.addMapMarker(mapMaker);
    }

    @Override
    public void relocate(GeoPosition newPosition) {
        mapMaker.setLat(newPosition.getLatitude());
        mapMaker.setLon(newPosition.getLongitude());
        nodeContainer.repaint();
    }

    @Override
    protected void relocateEdges(GeoPosition newPosition) {
        // TODO
    }


    @Override
    public void setEdge(IDeviceModel secondEndNode) {
        // EMPTY
    }

    @Override
    public void removeEdge(IDeviceModel secondEndNode) {
        // EMPTY
    }

    @Override
    public GeoPosition getNodePosition() {
        return new GeoPosition(mapMaker.getLat(), mapMaker.getLon());
    }

    public void setLat(double lat) {
        mapMaker.setLat(lat);
    }

    public void setLon(double lon) {
        mapMaker.setLon(lon);
    }

    public double getLat() {
        return mapMaker.getLat();
    }

    public double getLon() {
        return mapMaker.getLon();
    }

    public MapMarkerDot getMapMaker(){
        return this.mapMaker;
    }

    public void startSense() {
        mapMaker.setBackColor(Color.GREEN);
    }

    public void stopSense() {
        mapMaker.setBackColor(Color.YELLOW);
    }

    public void showConsole() {
        this.sensorConsole.show();
    }

}
