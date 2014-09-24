package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.BasicSimContext;
import dissim.simspace.SimCalendarInterface;
import dissim.simspace.SimContextInterface;
import net.xeoh.plugins.base.annotations.PluginImplementation;

//@PluginImplementation
public class SenseSimContext extends BasicSimContext implements SimContextInterface {

    private EventsRoutedSensorNode eventsRoutedSensorNode;

    public SenseSimContext() {
        super();
    }

    @Override
    public void initContext() {
        System.out.println("Jestem tutaj");
    }

    @Override
    public void stopContext() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isReadyToASAPtimeFlow() {
        return true;
    }

    @Override
    public SimCalendarInterface getSimCalendar() {
        return super.getSimCalendar();
    }

    @Override
    public void clearContext() {
        super.clearContext();
    }

}
