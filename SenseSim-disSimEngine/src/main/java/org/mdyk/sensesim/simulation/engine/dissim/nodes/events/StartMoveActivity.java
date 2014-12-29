package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;


public class StartMoveActivity extends BasicSimStateChange<DisSimRoutedSensorNodeEntity, Object> {

    private DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity;

    public StartMoveActivity(DisSimRoutedSensorNodeEntity disSimRoutedSensorNodeEntity) throws SimControlException {
        super(disSimRoutedSensorNodeEntity);
        this.disSimRoutedSensorNodeEntity = disSimRoutedSensorNodeEntity;
    }


    @Override
    protected void transition() throws SimControlException {
        System.out.println(">>>> StartMoveActivity.transition");
        System.out.println("-------- Początek ruchu [" + simTime() + "] -------");

        disSimRoutedSensorNodeEntity.endMoveActivity = new EndMoveActivity(1, disSimRoutedSensorNodeEntity, this);

        System.out.println("<<<< StartMoveActivity.transition");
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
