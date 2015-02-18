package org.mdyk.sensesim.simulation.engine.dissim.communication;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mdyk.netsim.logic.communication.CommunicationProcessFactory;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.communication.process.CommunicationProcess;
import org.mdyk.netsim.logic.network.WirelessChannel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.mdyk.sensesim.simulation.engine.dissim.communication.events.CommunicationProcessSimEntity;

@Singleton
public class DisSimCommunicationProcessFactory implements CommunicationProcessFactory {

    @Inject
    private WirelessChannel wirelessChannel;

    @Override
    public CommunicationProcess createCommunicationProcess(int id, ISensorModel<?> sender, ISensorModel<?> receiver, double startTime, Message message) {
        return new CommunicationProcessSimEntity(id , sender, receiver, startTime, message, wirelessChannel);
    }
}
