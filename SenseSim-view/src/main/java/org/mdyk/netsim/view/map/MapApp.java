package org.mdyk.netsim.view.map;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.OsmTileLoader;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Main class for appliaction
 */
public class MapApp extends JPanel implements JMapViewerEventListener {

    private static final long serialVersionUID = 1L;

    private JMapViewerTree treeMap = null;
    private JLabel zoomValue = null;
    private JLabel mperpLabelValue = null;

    /**
     * Constructs the {@code Demo}.
     */
    public MapApp() {
        treeMap = new JMapViewerTree("Zones");
        map().addJMVListener(this);
        setLayout(new BorderLayout());

        try {
            map().setTileLoader(new OsmFileCacheTileLoader(map()));
        } catch (IOException e) {
            map().setTileLoader(new OsmTileLoader(map()));
        }
        
        add(treeMap, BorderLayout.CENTER);
    }

    private JMapViewer map(){
        return treeMap.getViewer();
    }


    private void updateZoomParameters() {
        if (mperpLabelValue!=null) {
            mperpLabelValue.setText(String.format("%s",map().getMeterPerPixel()));
        }
        if (zoomValue!=null) {
            zoomValue.setText(String.format("%s", map().getZoom()));
        }
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
            updateZoomParameters();
        }
    }

    public JMapViewer getMapContainer() {
        return map();
    }
}