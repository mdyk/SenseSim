package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonValue;

public class StartSenseActivity extends BasicSimStateChange<DisSimNodeEntity, Object> {

    private DisSimNodeEntity disSimNodeEntity;

    private static final Logger LOG = Logger.getLogger(StartSenseActivity.class);

    private Environment environment;

    private DisSimRoutedNode sensorNode;

    public StartSenseActivity(DisSimNodeEntity disSimNodeEntity) throws SimControlException {
        super(disSimNodeEntity);
        this.disSimNodeEntity = disSimNodeEntity;
        this.environment = disSimNodeEntity.routedNode.environment;
        this.sensorNode = disSimNodeEntity.routedNode;
    }

    @Override
    protected void transition() throws SimControlException {
        LOG.trace(">> StartSenseActivity time=" + simTime());

        disSimNodeEntity.endSenseActivity = new EndSenseActivity(0.5, disSimNodeEntity);

        for(AbilityType ability : sensorNode.getAbilities()) {
            PhenomenonValue phenomenonValue = environment.getEventValue(sensorNode.getPosition(),simTime(), ability);
            sensorNode.addObservation(ability, simTime(), phenomenonValue);
        }

        EventBusHolder.getEventBus().post(EventFactory.startSenseEvent(sensorNode));

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
