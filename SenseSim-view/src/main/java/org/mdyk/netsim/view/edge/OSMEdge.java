package org.mdyk.netsim.view.edge;

import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Edge based on MapPolygonImpl
 */
public class OSMEdge extends MapPolygonImpl {

    public OSMEdge(ICoordinate... points) {
        super(points);
    }

    public OSMEdge(java.util.List<? extends ICoordinate> points) {
        super(points);
    }

    public OSMEdge(String name, List<? extends ICoordinate> points) {
        super(name, points);
    }

    public OSMEdge(String name, ICoordinate... points) {
        super(name, points);
        this.setStyle(getDefaultStyle());
    }

    public OSMEdge(Layer layer, List<? extends ICoordinate> points) {
        super(layer, points);
    }

    public OSMEdge(Layer layer, String name, List<? extends ICoordinate> points) {
        super(layer, name, points);
    }

    public OSMEdge(Layer layer, String name, ICoordinate... points) {
        super(layer, name, points);
    }

    public OSMEdge(Layer layer, String name, List<? extends ICoordinate> points, Style style) {
        super(layer, name, points, getDefaultStyle());
    }

    public static Style getDefaultStyle(){
        return new Style(Color.RED, new Color(100,100,100,50), new BasicStroke(1.5f), getDefaultFont());
    }
}
