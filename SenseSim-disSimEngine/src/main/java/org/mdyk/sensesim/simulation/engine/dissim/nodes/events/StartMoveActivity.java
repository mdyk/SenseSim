package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.random.SimGenerator;
import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;


public class StartMoveActivity extends BasicSimStateChange<EventsRoutedSensorNode, Object> {

    private EventsRoutedSensorNode eventsRoutedSensorNode;
    private SimGenerator generator;

    public StartMoveActivity(EventsRoutedSensorNode eventsRoutedSensorNode, double delay) throws SimControlException {
        super(eventsRoutedSensorNode);
        this.eventsRoutedSensorNode = eventsRoutedSensorNode;
        generator = new SimGenerator();
    }


    @Override
    protected void transition() throws SimControlException {
        System.out.println(">>>> StartMoveActivity.transition");

        double ruch = generator.normal(9.0, 1.0);
        System.out.println("-------- Początek ruchu [" + simTime() + "] -------");

        eventsRoutedSensorNode.endMoveActivity = new EndMoveActivity(1, eventsRoutedSensorNode, this);

        System.out.println("<<<< StartMoveActivity.transition");
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
