package org.mdyk.netsim.logic.scenario.xml.util;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.netsim.mathModel.phenomena.time.SimplePhenomenonTimeRange;

import java.io.File;
import java.util.Map;


public class XmlTypeConverterTest {


    @Test
    public void testReadPhenomenonValuesFromFile() throws Exception {
        File csv = FileUtils.toFile(getClass().getResource("/phenomenaValueTest.csv"));
        String scenario1Path = FileUtils.toFile(getClass().getResource("/scenario-1.xml")).getAbsolutePath();
        XmlTypeConverter xmlTypeConverter = new XmlTypeConverter(scenario1Path);
        Map<IPhenomenonTimeRange, Object> map = xmlTypeConverter.readPhenomenonValuesFromFile(csv.getAbsolutePath());

        IPhenomenonTimeRange time = new SimplePhenomenonTimeRange(1,1000);
        Object value = map.get(time);

        TestCase.assertEquals(1 , map.size());
        TestCase.assertEquals(110 , value);
    }


}
