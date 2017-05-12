package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimAction;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.EventFactory;
import org.mdyk.netsim.mathModel.Functions;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import java.util.List;


public class SenseActivity extends BasicSimAction<DisSimNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(SenseActivity.class);

    private SensorModel sensorModel;
    private DisSimNodeEntity entity;
    private Environment environment;


    public SenseActivity(DisSimNodeEntity entity , SensorModel sensorModel , Environment environment, double period, double duration, double delayToRun) throws SimControlException {
        super(entity,delayToRun , period , duration);
        this.sensorModel = sensorModel;
        this.entity = entity;
        this.environment = environment;
    }


    @Override
    protected void transitionOnStart() throws SimControlException {
        LOG.trace(">> start SenseActivity; time=" + this.simTime());
    }

    @Override
    protected void transitionOnFinish() throws SimControlException {
        LOG.trace(">> stop SenseActivity; time=" + this.simTime());
        List<PhenomenonModel> observedPhenomena = environment.getPhenomenaByType(sensorModel.getConfigurationSpaceClass());

        // TODO rozważenie wielu zjawisk, które w danym momencie sensor może obserwować
        for(PhenomenonModel phenomenon : observedPhenomena) {
            // FIXME poprawne wyliczanie odległości
            if(!Functions.isPointInRegion(entity.deviceLogic.getPosition(), phenomenon.getPhenomenonRegionPoints())) {

                if(phenomenon.getAttachedDevice()!=null && phenomenon.getAttachedDevice().getID() == this.entity.getDeviceLogic().getID()) {
                    ConfigurationSpace observation = sensorModel.getObservation(phenomenon, simTime() , 0);
                    if(observation != null) {
                        entity.deviceLogic.addObservation(sensorModel.getConfigurationSpaceClass() , simTime() , observation);
                    }
                } else {
                    continue;
                }
            }

            ConfigurationSpace observation = sensorModel.getObservation(phenomenon, simTime() , 0);
            if(observation != null) {
                entity.deviceLogic.addObservation(sensorModel.getConfigurationSpaceClass() , simTime() , observation);
            }
        }

        EventBusHolder.getEventBus().post(EventFactory.endSenseEvent(entity.getDeviceLogic()));

    }
}
