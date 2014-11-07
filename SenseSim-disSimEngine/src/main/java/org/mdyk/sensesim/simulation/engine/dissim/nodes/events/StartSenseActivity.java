package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;

public class StartSenseActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, Object> {

    private DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;

    private static final Logger LOG = Logger.getLogger(StartSenseActivity.class);

    private Environment environment;

    private DisSimRoutedSensorNode sensorNode;

    public StartSenseActivity(DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity) throws SimControlException {
        super(disSimRoutedSensorNodeEntity);
        this.disSimRoutedSensorNodeEntity = disSimRoutedSensorNodeEntity;
        this.environment = disSimRoutedSensorNodeEntity.wrapper.environment;
        this.sensorNode = disSimRoutedSensorNodeEntity.wrapper;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> StartSenseActivity time=" + simTime());

        disSimRoutedSensorNodeEntity.endSenseActivity = new EndSenseActivity(0.1, disSimRoutedSensorNodeEntity);

        for(AbilityType ability : sensorNode.getAbilities()) {
            PhenomenonValue phenomenonValue = environment.getEventValue(sensorNode.getPosition(),simTime(), ability);
            sensorNode.addObservation(simTime(), phenomenonValue);
        }

        LOG.trace("<< StartSenseActivity");
    }

    @Override
    protected void onTermination() throws SimControlException {
        // EMPTY  //
    }

    @Override
    protected void onInterruption() throws SimControlException {
        // EMPTY //
    }
}
