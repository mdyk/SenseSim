package org.mdyk.netsim.logic.reporting;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Test;

import java.io.FileNotFoundException;



public class CommunicationReportTest {

    @After
    public void clear() {
        CommunicationReport.deleteReport();
    }

    @Test
    public void addNewEntry() throws FileNotFoundException {

        CommunicationReportBean crb = new CommunicationReportBean();

        crb.setCommProcId(1);
        crb.setSimTimeStart(2.0);
        crb.setSimTimeEnd(3.0);
        crb.setCommStatus("DURING_COMM");
        crb.setMessageId(222);
        crb.setMessageContent("message content");
        crb.setSender(45);
        crb.setReceiver(46);
        crb.setMessageType("td");
        crb.setMessageSize(5000);

        CommunicationReport.updateCommReport(crb);

        TestCase.assertTrue(CommunicationReport.findRecord(1 , CommunicationReport.getRecords())!= null);

    }


    @Test
    public void updateEntry() throws FileNotFoundException {

        CommunicationReportBean crb = new CommunicationReportBean();

        crb.setCommProcId(1);
        crb.setSimTimeStart(2.0);
        crb.setSimTimeEnd(3.0);
        crb.setCommStatus("DURING_COMM");
        crb.setMessageId(222);
        crb.setMessageContent("message content");
        crb.setSender(45);
        crb.setReceiver(46);
        crb.setMessageType("td");
        crb.setMessageSize(5000);

        CommunicationReport.updateCommReport(crb);

        TestCase.assertTrue(CommunicationReport.findRecord(1 , CommunicationReport.getRecords())!= null);

        CommunicationReportBean crb2 = new CommunicationReportBean();

        crb2.setCommProcId(1);
        crb2.setSimTimeStart(2.0);
        crb2.setSimTimeEnd(3.0);
        crb2.setCommStatus("SUCCESS");
        crb2.setMessageId(222);
        crb2.setMessageContent("message content");
        crb2.setSender(45);
        crb2.setReceiver(46);
        crb2.setMessageType("td");
        crb2.setMessageSize(5000);

        CommunicationReport.updateCommReport(crb2);

        CommunicationReportBean foundRecord = CommunicationReport.findRecord(1 , CommunicationReport.getRecords());
        TestCase.assertTrue(foundRecord != null);
        TestCase.assertEquals("SUCCESS" , foundRecord.getCommStatus());

    }

}