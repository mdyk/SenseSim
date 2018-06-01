package org.mdyk.netsim.logic.reporting;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reporting of the communication between devices
 */
public class CommunicationReport {

    private static final Logger LOG = Logger.getLogger(CommunicationReport.class);
    private static String commReportFile = "communicationReport.csv";


    public static void updateCommReport(CommunicationReportBean updatedCrb) {
        try {
            List<CommunicationReportBean> records =  getRecords();

            CommunicationReportBean recordToUpdate = findRecord(updatedCrb.getCommProcId() , records);

            if(recordToUpdate == null) {
                records.add(updatedCrb);
            } else {
                records.remove(recordToUpdate);
                records.add(updatedCrb);
            }

            saveRecords(records);

        } catch (CsvRequiredFieldEmptyException | IOException | CsvDataTypeMismatchException e) {
            LOG.error(e);
        }
    }

    public static CommunicationReportBean findRecord(int commId, List<CommunicationReportBean> records) {
        CommunicationReportBean foundCrb = null;
        for(CommunicationReportBean crb : records) {
            if(crb.getCommProcId() == commId) {
                foundCrb = crb;
            }
        }
        return foundCrb;
    }

    public static List<CommunicationReportBean> getRecords() throws FileNotFoundException {

        List<CommunicationReportBean> records = new ArrayList<>();

        File reportFile = new File(commReportFile);

        if(reportFile.exists()) {
            records = new CsvToBeanBuilder(new FileReader(commReportFile)).withType(CommunicationReportBean.class).build().parse();
        }

        return records;
    }

    public static void saveRecords(List<CommunicationReportBean> records) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = new FileWriter(commReportFile);

        StatefulBeanToCsvBuilder beanToCsv = new StatefulBeanToCsvBuilder(writer);
        StatefulBeanToCsv statefulBeanToCsv = beanToCsv.build();

        statefulBeanToCsv.write(records);
        writer.close();
    }

    public static void deleteReport() {
        File reportFile = new File(commReportFile);

        reportFile.delete();

    }

}
