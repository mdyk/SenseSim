package org.mdyk.sensesim.simulation.engine.dissim.nodes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dissim.simspace.core.SimModel;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.node.SimEntityFactory;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimNodeEntity;


@Singleton
public class DisSimEntityFactory implements SimEntityFactory {

    @Inject
    private Environment environment;

    @Override
    public DeviceSimEntity buildSensorSimEntity(DeviceLogic deviceLogic) {
        return new DisSimNodeEntity(SimModel.getInstance().getCommonSimContext(), deviceLogic, environment);
    }


}
