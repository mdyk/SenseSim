package org.mdyk.netsim.view.node;

import org.mdyk.netsim.logic.node.geo.GeoSensorNode;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import java.awt.*;

/**
 * Node implementation as mark in Open Street Map (JMapViewer)
 */
public class OSMNodeView extends GeoNodeView<JMapViewer> {

    private MapMarkerDot mapMaker;

    public OSMNodeView(GeoSensorNode node, JMapViewer mapViewer) {
        super(node, mapViewer);
        mapMaker = new MapMarkerDot(node.getLatitude(), node.getLongitude());
        mapMaker.setBackColor(Color.YELLOW);
    }

    @Override
    protected void prepareView() {

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
    public void setEdge(ISensorModel secondEndNode) {

    }

    @Override
    public void removeEdge(ISensorModel secondEndNode) {

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

}
