package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.broker.IEvent;
import dissim.broker.IEventPublisher;
import dissim.simspace.BasicSimContext;
import dissim.simspace.BasicSimEntity;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;


public class DisSimNodeEntity extends BasicSimEntity {

    private static final Logger LOG = Logger.getLogger(DisSimNodeEntity.class);

    protected DisSimRoutedNode routedNode;

    protected StartMoveActivity startMoveActivity;
    protected EndMoveActivity endMoveActivity;
    protected StartSenseActivity startSenseActivity;
    protected EndSenseActivity endSenseActivity;

    public DisSimNodeEntity(BasicSimContext context, DisSimRoutedNode routedNode) {
        super(context);
        this.routedNode = routedNode;
        try {
            this.startMoveActivity = new StartMoveActivity(this);
            this.startSenseActivity = new StartSenseActivity(this);
        } catch (SimControlException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public DisSimRoutedNode getRoutedNode() {
        return routedNode;
    }

    @Override
    public void reflect(IEvent iEvent, IEventPublisher iEventPublisher) {
    }

    @Override
    public void reflect(IEvent iEvent) {
    }
}
