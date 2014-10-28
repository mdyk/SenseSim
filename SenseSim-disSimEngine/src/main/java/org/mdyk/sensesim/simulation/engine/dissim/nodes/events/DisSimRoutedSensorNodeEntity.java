package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

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
            this.startMoveActivity = new StartMoveActivity(this , 0.1);
            this.startSenseActivity = new StartSenseActivity(this , 0.1);
        } catch (SimControlException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void reflect(BasicSimStateChange<?, ?> event) {
        /* EMPTY */
    }

    public DisSimRoutedSensorNode getWrapper() {
        return wrapper;
    }
}
