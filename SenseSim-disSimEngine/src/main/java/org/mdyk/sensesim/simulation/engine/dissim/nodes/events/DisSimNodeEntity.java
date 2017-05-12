package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.BasicSimContext;
import dissim.simspace.core.BasicSimEntity;
import dissim.simspace.core.SimControlException;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.Environment;
import org.mdyk.netsim.logic.node.geo.DeviceLogic;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.mathModel.sensor.SensorModel;

import java.util.HashMap;
import java.util.Map;


public class DisSimNodeEntity extends BasicSimEntity implements DeviceSimEntity {

    private static final Logger LOG = Logger.getLogger(DisSimNodeEntity.class);

    //FIXME
    // public StartMoveActivity startMoveActivity;
    public MoveActivity startMoveActivity;

    //FIXME
    public EndMoveActivity endMoveActivity;
    //FIXME
//    public StartSenseActivity startSenseActivity;
//    //FIXME
//    public EndSenseActivity endSenseActivity;

    private Map<SensorModel , SenseActivity> senseActions;

//    private IdleProcess idleProcess;

    protected DeviceLogic deviceLogic;

    protected Middleware middleware;

    protected Environment environment;

    public DisSimNodeEntity(BasicSimContext context, DeviceLogic deviceLogic, Environment environment) {
        super(context);
        this.setDeviceLogic(deviceLogic);
        this.environment = environment;
        this.senseActions = new HashMap<>();
    }

    protected void startNode() {
        LOG.debug(">> Starting node " + this.deviceLogic.getID());
        try {

            double delay = Math.random();

            this.startMoveActivity = new MoveActivity(this);
//            this.startSenseActivity = new StartSenseActivity(this);

            for(SensorModel sensorModel : this.deviceLogic.getSensors()) {
                delay = Math.random();
                SenseActivity senseActivity = new SenseActivity(this, sensorModel, environment , sensorModel.samplingFrequency() , sensorModel.sensingTime(), delay);
                senseActions.put(sensorModel , senseActivity);
            }

        } catch (SimControlException e) {
           LOG.error(e.getMessage(),e);
        }
    }

    public DisSimDeviceLogic getProgrammableNode() {
        return (DisSimDeviceLogic) deviceLogic;
    }

    @Override
    public void startProgramExecution(int PID) {
        LOG.trace(">> startProgramExecution PID="+PID);
        try {
            new StartProgramExecution(this , PID);
        } catch (SimControlException e) {
            LOG.error(e.getMessage() ,e);
        }
        LOG.trace("<< startProgramExecution");
    }

    @Override
    public void endProgramExecution(int PID) {
        LOG.trace(">> endProgramExecution");
        try {
            new EndProgramExecution(this , PID);
        } catch (SimControlException e) {
            LOG.error(e.getMessage() ,e);
        }
        LOG.trace("<< endProgramExecution");
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

    public void setDeviceLogic(DeviceLogic deviceLogic) {
        this.deviceLogic = deviceLogic;
    }

    public DeviceLogic getDeviceLogic() {
        return deviceLogic;
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
