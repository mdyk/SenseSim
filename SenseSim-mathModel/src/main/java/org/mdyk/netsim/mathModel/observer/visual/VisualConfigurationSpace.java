package org.mdyk.netsim.mathModel.observer.visual;

import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;

import java.awt.Image;


public class VisualConfigurationSpace extends ConfigurationSpace {

    private Image image;

    public VisualConfigurationSpace(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return this.image;
    }

    @Override
    public String getValue() {
        return null;
    }
}
