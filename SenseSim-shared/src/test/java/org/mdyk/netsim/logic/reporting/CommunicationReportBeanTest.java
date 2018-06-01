package org.mdyk.netsim.logic.reporting;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.junit.Test;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;



public class CommunicationReportBeanTest {

    @Test
    public void storeCommunication() throws Exception {
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

        Writer writer = new FileWriter("yourfile.csv");


        StatefulBeanToCsvBuilder beanToCsv = new StatefulBeanToCsvBuilder(writer);
        StatefulBeanToCsv statefulBeanToCsv = beanToCsv.build();

        statefulBeanToCsv.write(crb);

        writer.close();

        List<CommunicationReportBean> beans = new CsvToBeanBuilder(new FileReader("yourfile.csv"))
                .withType(CommunicationReportBean.class).build().parse();




//        CSVReader

        CommunicationReportBean crb2 = new CommunicationReportBean();

        crb2.setCommProcId(33333);
        crb2.setSimTimeStart(2.0);
        crb2.setSimTimeEnd(3.0);
        crb2.setCommStatus("DURING_COMM");
        crb2.setMessageId(222);
        crb2.setMessageContent("message content");
        crb2.setSender(45);
        crb2.setReceiver(46);
        crb2.setMessageType("td");
        crb2.setMessageSize(5000);

        beans.add(crb2);


        Writer writer2 = new FileWriter("yourfile.csv");


        StatefulBeanToCsvBuilder beanToCsv2 = new StatefulBeanToCsvBuilder(writer2);
        StatefulBeanToCsv statefulBeanToCsv2 = beanToCsv2.build();

        statefulBeanToCsv2.write(beans);

        writer2.close();
//
//        crb.setCommStatus("SUCCESS");
//
//        Writer writer2 = new FileWriter("yourfile.csv");
//        StatefulBeanToCsvBuilder beanToCsv2 = new StatefulBeanToCsvBuilder(writer2);
//        StatefulBeanToCsv statefulBeanToCsv2 = beanToCsv.build();
//
//        statefulBeanToCsv2.write(crb);
//        statefulBeanToCsv2.write(crb2);
//        writer2.close();

        //        StatefulBeanToCsvBuilder beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
//        beanToCsv.write(beans);
//        writer.close();


    }

}