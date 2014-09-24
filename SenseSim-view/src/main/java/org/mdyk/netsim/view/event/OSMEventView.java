package org.mdyk.netsim.view.event;


import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.event.IPhenomenonModel;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import java.util.LinkedList;
import java.util.List;

// TODO: generic + dziedziczenie
public class OSMEventView {

    private MapPolygon polygon;
    private JMapViewer mapViewer;

    public OSMEventView(IPhenomenonModel<GeoPosition> eventModel, JMapViewer mapViewer, String name) {

        List<Coordinate> coordinates = new LinkedList<>();

        for(GeoPosition position : eventModel.getPhenomenonRegionPoints()) {
            coordinates.add(new Coordinate(position.getLatitude(),position.getLongitude()));
        }

        polygon = new MapPolygonImpl(name,coordinates);
        this.mapViewer = mapViewer;
    }

    public void renderEvent() {
        mapViewer.addMapPolygon(polygon);
    }

}
