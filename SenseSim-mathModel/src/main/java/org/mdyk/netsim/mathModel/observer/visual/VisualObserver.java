package org.mdyk.netsim.mathModel.observer.visual;

import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ObserverModel;
import org.mdyk.netsim.mathModel.observer.PresmisesSpace;


public class VisualObserver implements ObserverModel {
    @Override
    public ConfigurationSpace getConfigurationSpace() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PresmisesSpace getPremises(ConfigurationSpace event, Object... parameters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ConfigurationSpace getConclusion(PresmisesSpace premises) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
