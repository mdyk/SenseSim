package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;


import dissim.simspace.BasicSimStateChange;
import dissim.simspace.SimControlException;

public class EndSenseActivity extends BasicSimStateChange<EventsRoutedSensorNode, Object> {

    private EventsRoutedSensorNode eventsRoutedSensorNode;

    public EndSenseActivity(double delay, EventsRoutedSensorNode eventsRoutedSensorNode) throws SimControlException {
        super(eventsRoutedSensorNode, delay);
        this.eventsRoutedSensorNode = eventsRoutedSensorNode;
    }

    @Override
    protected void transition() throws SimControlException {
        System.out.println("Stopping sense " + simTime());

        eventsRoutedSensorNode.startSenseActivity = new StartSenseActivity(eventsRoutedSensorNode, 0.1);
    }

    @Override
    protected void onTermination() throws SimControlException {

    }

    @Override
    protected void onInterruption() throws SimControlException {

    }
}
