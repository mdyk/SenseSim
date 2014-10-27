package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;

public class StartSenseActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, Object> {

    private DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;

    public StartSenseActivity(DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity, double delay) throws SimControlException {
        super(disSimRoutedSensorNodeEntity, delay);
        this.disSimRoutedSensorNodeEntity = disSimRoutedSensorNodeEntity;
    }

    @Override
    protected void transition() throws SimControlException {
        System.out.println("Starting to sense " + simTime());

        disSimRoutedSensorNodeEntity.endSenseActivity = new EndSenseActivity(0.5 , disSimRoutedSensorNodeEntity);

        System.out.println("------- sense -------");

    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
