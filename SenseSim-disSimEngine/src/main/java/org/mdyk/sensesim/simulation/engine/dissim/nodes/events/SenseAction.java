package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimAction;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.mathModel.Functions;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import java.util.List;


public class SenseAction extends BasicSimAction<DisSimNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(SenseAction.class);

    private SensorModel sensorModel;
    private DisSimNodeEntity entity;
    private Environment environment;


    public SenseAction(DisSimNodeEntity entity , SensorModel sensorModel , Environment environment, double period, double duration) throws SimControlException {
        super(entity,0 , period , duration);
        this.sensorModel = sensorModel;
        this.entity = entity;
        this.environment = environment;
    }


    @Override
    protected void transitionOnStart() throws SimControlException {
        LOG.trace(">> start SenseAction; time=" + this.simTime());
    }

    @Override
    protected void transitionOnFinish() throws SimControlException {
        LOG.trace(">> stop SenseAction; time=" + this.simTime());
        List<PhenomenonModel> observedPhenomena = environment.getPhenomenaByType(sensorModel.getConfigurationSpaceClass());

        // TODO rozważenie wielu zjawisk, które w danym momencie sensor może obserwować
        for(PhenomenonModel phenomenon : observedPhenomena) {
            // FIXME poprawne wyliczanie odległości
            if(!Functions.isPointInRegion(entity.deviceLogic.getPosition(), phenomenon.getPhenomenonRegionPoints())) {
                continue;
            }

            ConfigurationSpace observation = sensorModel.getObservation(phenomenon, simTime() , 0);
            if(observation != null) {
                entity.deviceLogic.addObservation(sensorModel.getConfigurationSpaceClass() , simTime() , observation);
            }
        }

    }
}
