package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;

public class StartSenseActivity extends BasicSimStateChange<EventsRoutedSensorNode, Object> {

    private EventsRoutedSensorNode eventsRoutedSensorNode;

    public StartSenseActivity(EventsRoutedSensorNode eventsRoutedSensorNode, double delay) throws SimControlException {
        super(eventsRoutedSensorNode, delay);
        this.eventsRoutedSensorNode = eventsRoutedSensorNode;
    }

    @Override
    protected void transition() throws SimControlException {
        System.out.println("Starting to sense " + simTime());

        eventsRoutedSensorNode.endSenseActivity = new EndSenseActivity(0.5 , eventsRoutedSensorNode);

        System.out.println("------- sense -------");

    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
