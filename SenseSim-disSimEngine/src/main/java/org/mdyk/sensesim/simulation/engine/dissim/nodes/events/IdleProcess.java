package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimProcess;
import org.apache.log4j.Logger;

public class IdleProcess extends BasicSimProcess<DisSimNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(IdleProcess.class);

    public IdleProcess(DisSimNodeEntity entity) throws SimControlException {
        super(entity);
    }

    @Override
    public double controlStateTransitions() {
        LOG.trace(">> controlStateTransitions");
        if (getSimEntity().middleware.getPrograms().size() != 0){
            LOG.debug(">> getSimEntity().middleware.getPrograms().size() != 0");
            // TODO start programów
            this.stop();
            return 0.0;
        } else {
            LOG.debug(">> getSimEntity().middleware.getPrograms().size() == 0");
            return 0.01;
        }

    }
}
