package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.BasicSimContext;
import dissim.simspace.BasicSimEntity;
import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;
import org.apache.log4j.Logger;


public class DisSimRoutedSensorNodeEntity extends BasicSimEntity {

    private static final Logger logger = Logger.getLogger(DisSimRoutedSensorNodeEntity.class);

    protected EventsRoutedSensorNodeWrapper wrapper;

    protected StartMoveActivity startMoveActivity;
    protected EndMoveActivity endMoveActivity;
    protected StartSenseActivity startSenseActivity;
    protected EndSenseActivity endSenseActivity;

    public DisSimRoutedSensorNodeEntity(BasicSimContext context, EventsRoutedSensorNodeWrapper wrapper) {
        super(context);
        this.wrapper = wrapper;
        try {
            this.startMoveActivity = new StartMoveActivity(this , 0.1);
            this.startSenseActivity = new StartSenseActivity(this , 0.1);
        } catch (SimControlException e) {
            logger.error(e.getMessage() , e);
        }
    }

    @Override
    public void reflect(BasicSimStateChange<?, ?> event) {
        /* EMPTY */
    }
}
