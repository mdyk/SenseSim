package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;

public class EndSenseActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, Object> {

    private DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;

    public EndSenseActivity(double delay, DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity) throws SimControlException {
        super(disSimRoutedSensorNodeEntity, delay);
        this.disSimRoutedSensorNodeEntity = disSimRoutedSensorNodeEntity;
    }

    @Override
    protected void transition() throws SimControlException {
        System.out.println("Stopping sense " + simTime());

        disSimRoutedSensorNodeEntity.startSenseActivity = new StartSenseActivity(disSimRoutedSensorNodeEntity, 0.1);
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
