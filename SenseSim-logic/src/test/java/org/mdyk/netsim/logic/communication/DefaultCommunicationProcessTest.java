package org.mdyk.netsim.logic.communication;

import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.communication.process.CommunicationStatus;
import org.mdyk.netsim.logic.communication.process.DefaultCommunicationProcess;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.device.DefaultDeviceModel;
import org.mdyk.netsim.mathModel.device.IDeviceModel;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;

import java.util.ArrayList;
import java.util.List;


public class DefaultCommunicationProcessTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testGetETA() throws Exception {
        List<CommunicationInterface> communicationInterfaces = new ArrayList<>();
        CommunicationInterface commInt1 = new CommunicationInterface(1, "int1",5000,5000,10, CommunicationInterface.TopologyType.ADHOC);
        communicationInterfaces.add(commInt1);
        
        IDeviceModel sender = new DefaultDeviceModel(1, "", new Position(0,0,0), 0 , 0 , 1, new ArrayList<>() , new ArrayList<>(),  communicationInterfaces) {

            @Override
            protected void onMessage(double time, Message message) {
                // unused
            }

            @Override
            protected void onMessage(double time, int communicationInterfaceId, Message message) {
                
            }
        } ;

        IDeviceModel receiver = new DefaultDeviceModel(2, "", new Position(0,0,0), 0 , 0 , 1, new ArrayList<>() , new ArrayList<>(),  communicationInterfaces) {
            @Override
            protected void onMessage(double time, Message message) {
                // unused
            }

            @Override
            protected void onMessage(double time, int communicationInterfaceId, Message message) {
                
            }
        } ;

        DefaultCommunicationProcess process = new DefaultCommunicationProcess(1 , sender, receiver , 1, 0 ,new TestMessage() {
            @Override
            public int getSize() {
                return 625;
            }
        });

        TestCase.assertEquals(1.0 , process.getETA());

        DefaultCommunicationProcess process2 = new DefaultCommunicationProcess(1 , sender, receiver , 1, 0 ,new TestMessage() {
            @Override
            public int getSize() {
                return 1250;
            }
        });

        TestCase.assertEquals(2.0 , process2.getETA());

        DefaultCommunicationProcess process3 = new DefaultCommunicationProcess(1 , sender, receiver , 1, 0 ,new TestMessage() {
            @Override
            public int getSize() {
                return 312;
            }
        });

        TestCase.assertEquals(0.4992 , process3.getETA());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getCommunicationStatusTest() {

        List<CommunicationInterface> communicationInterfaces = new ArrayList<>();
        CommunicationInterface commInt1 = new CommunicationInterface(1, "int1",5000,5000,10, CommunicationInterface.TopologyType.ADHOC);
        communicationInterfaces.add(commInt1);

        IDeviceModel sender = new DefaultDeviceModel(1, "", new Position(0,0,0), 0 , 0 , 1, new ArrayList<>() , new ArrayList<>(),  communicationInterfaces) {
            @Override
            protected void onMessage(double time, Message message) {
                // unused
            }

            @Override
            protected void onMessage(double time, int communicationInterfaceId, Message message) {
                
            }
        } ;

        IDeviceModel receiver = new DefaultDeviceModel(1, "", new Position(0,0,0), 0 , 0 , 1, new ArrayList<>() , new ArrayList<>(),  communicationInterfaces) {
            @Override
            protected void onMessage(double time, Message message) {
                // unused
            }

            @Override
            protected void onMessage(double time, int communicationInterfaceId, Message message) {
                
            }
        } ;

        DefaultCommunicationProcess process = new DefaultCommunicationProcess(1 , sender, receiver , 1, 0 ,new TestMessage() {
            @Override
            public int getSize() {
                return 6250;
            }
        });

        // 0 bits sent, 0 time, eta 10
        TestCase.assertEquals(CommunicationStatus.DURING_COMM, process.getCommunicationStatus(0));

        // 0 bits sent, 3 time, eta 10
        TestCase.assertEquals(CommunicationStatus.DURING_COMM, process.getCommunicationStatus(3));

        process.addBitsSent(1024);
        // 1024 bits sent, 10 time, eta 10
        TestCase.assertEquals(CommunicationStatus.DURING_COMM, process.getCommunicationStatus(10));

        process.bitsSent(50000);
        // 50000 (all) bits sent, 11 time, eta 10
        TestCase.assertEquals(CommunicationStatus.SUCCESS, process.getCommunicationStatus(11));

//        process.bitsSent(50);
        // 1024 bits sent, 10 time, eta 10
//        TestCase.assertEquals(CommunicationStatus.FAILURE, process.getCommunicationStatus(11));

        DefaultCommunicationProcess process2 = new DefaultCommunicationProcess(1 , sender, receiver , 1, 0 ,new TestMessage() {
            @Override
            public int getSize() {
                return 6250;
            }
        });

        process2.processInterrupted();
        TestCase.assertEquals(CommunicationStatus.FAILURE, process2.getCommunicationStatus(9));

    }


    private static abstract class TestMessage implements Message {

        @Override
        public long getID(){
            return 1;
        }

        @Override
        public Object getMessageContent() {
            return null;
        }

        @Override
        public int getMessageSource() {
            return -1;
        }

        @Override
        public int getMessageDest() {
            return -1;
        }

        public int getCommunicationInterface() {return 1;}
    }

}
