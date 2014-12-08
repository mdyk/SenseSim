package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.broker.IEvent;
import dissim.broker.IEventPublisher;
import dissim.simspace.BasicSimContext;
import dissim.simspace.BasicSimEntity;
import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;


public class DisSimRoutedSensorNodeEntity extends BasicSimEntity {

    private static final Logger LOG = Logger.getLogger(DisSimRoutedSensorNodeEntity.class);

    protected DisSimRoutedSensorNode wrapper;

    protected StartMoveActivity startMoveActivity;
    protected EndMoveActivity endMoveActivity;
    protected StartSenseActivity startSenseActivity;
    protected EndSenseActivity endSenseActivity;

    public DisSimRoutedSensorNodeEntity(BasicSimContext context, DisSimRoutedSensorNode wrapper) {
        super(context);
        this.wrapper = wrapper;
        try {
            this.startMoveActivity = new StartMoveActivity(this);
            this.startSenseActivity = new StartSenseActivity(this);
        } catch (SimControlException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public DisSimRoutedSensorNode getWrapper() {
        return wrapper;
    }

    @Override
    public void reflect(IEvent iEvent, IEventPublisher iEventPublisher) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reflect(IEvent iEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
