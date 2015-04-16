package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimContext;
import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;


public class DisSimNodeEntity extends BasicSimEntity implements SensorSimEntity {

    private static final Logger LOG = Logger.getLogger(DisSimNodeEntity.class);

    //FIXME
    public StartMoveActivity startMoveActivity;
    //FIXME
    public EndMoveActivity endMoveActivity;
    //FIXME
    public StartSenseActivity startSenseActivity;
    //FIXME
    public EndSenseActivity endSenseActivity;

    private IdleProcess idleProcess;

    protected SensorLogic sensorLogic;

    protected Middleware middleware;

    protected Environment environment;

    public DisSimNodeEntity(BasicSimContext context, SensorLogic sensorLogic, Environment environment) {
        super(context);
        this.setSensorLogic(sensorLogic);
        this.environment = environment;
        try {
            idleProcess = new IdleProcess(this);
            idleProcess.start();
        } catch (SimControlException e) {
            LOG.error(e.getMessage() , e);
            throw new RuntimeException(e.getMessage() ,e);
        }
    }

    protected void startNode() {
        try {
            this.startMoveActivity = new StartMoveActivity(this);
            this.startSenseActivity = new StartSenseActivity(this);
        } catch (SimControlException e) {
           LOG.error(e.getMessage(),e);
        }

    }

    public DisSimSensorLogic getProgrammableNode() {
        return (DisSimSensorLogic) sensorLogic;
    }

    @Override
    public void startEntity() {
        startNode();
    }

    @Override
    public void stopEntity() {
        // TODO implementacja
    }

    @Override
    public double getSimTime() {
        return simTime();
    }

    @Override
    public void setSensorLogic(SensorLogic sensorLogic) {
        this.sensorLogic = sensorLogic;
    }

    @Override
    public SensorLogic getSensorLogic() {
        return sensorLogic;
    }

    @Override
    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public Middleware getMiddleware() {
        return middleware;
    }
}
