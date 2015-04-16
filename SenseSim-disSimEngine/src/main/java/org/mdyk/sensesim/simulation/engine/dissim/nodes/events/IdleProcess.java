package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimSimpleProcess;
import org.apache.log4j.Logger;

public class IdleProcess extends BasicSimSimpleProcess<DisSimNodeEntity, Object> {

    private static final Logger LOG = Logger.getLogger(IdleProcess.class);

    public IdleProcess(DisSimNodeEntity entity) throws SimControlException {
        super(entity, null);
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
