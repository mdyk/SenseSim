package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimProcess;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

/**
 * Created by Michal on 2016-05-11.
 */
public class SenseProcess extends BasicSimProcess<DisSimNodeEntity, Object> {

    private SensorModel sensorModel;
    private DisSimNodeEntity entity;
    private Environment environment;


    public SenseProcess(DisSimNodeEntity entity , SensorModel sensorModel , Environment environment) throws SimControlException {
        super(entity);
        this.sensorModel = sensorModel;
        this.entity = entity;
        this.environment = environment;
    }

    @Override
    public double controlStateTransitions() {

//        sensorModel.getObservation()

        return 0;
    }

}
