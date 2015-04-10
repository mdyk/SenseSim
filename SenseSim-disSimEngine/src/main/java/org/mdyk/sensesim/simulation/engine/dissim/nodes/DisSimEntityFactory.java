package org.mdyk.sensesim.simulation.engine.dissim.nodes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dissim.simspace.core.SimModel;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.node.SimEntityFactory;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;
import org.mdyk.sensesim.simulation.engine.dissim.nodes.events.DisSimNodeEntity;


@Singleton
public class DisSimEntityFactory implements SimEntityFactory {

    @Inject
    private Environment environment;

    @Override
    public SensorSimEntity buildSensorSimEntity(SensorLogic sensorLogic) {
        return new DisSimNodeEntity(SimModel.getInstance().getCommonSimContext(),  sensorLogic, environment);
    }


}
