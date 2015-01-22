package org.mdyk.netsim.logic.communication;

import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.message.Message;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.LinkedList;


public class DefaultCommunicationProcessTest {
    @Test
    public void testGetETA() throws Exception {
        ISensorModel sender = new DefaultSensorModel(1 , new Position(0,0,0), 10 , 1 , new LinkedList<>()) {
            @Override
            public void sense() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        } ;

        ISensorModel receiver = new DefaultSensorModel(1 , new Position(0,0,0), 10 , 1 , new LinkedList<>()) {
            @Override
            public void sense() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        } ;

        DefaultCommunicationProcess process = new DefaultCommunicationProcess(1 , sender, receiver , 0 ,new Message() {
            @Override
            public int getSize() {
                return 625;
            }
        });

        TestCase.assertEquals(1.0 , process.getETA());

        DefaultCommunicationProcess process2 = new DefaultCommunicationProcess(1 , sender, receiver , 0 ,new Message() {
            @Override
            public int getSize() {
                return 1250;
            }
        });

        TestCase.assertEquals(2.0 , process2.getETA());
    }
}
