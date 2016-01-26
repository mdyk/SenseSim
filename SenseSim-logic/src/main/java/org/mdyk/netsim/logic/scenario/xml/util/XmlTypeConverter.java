package org.mdyk.netsim.logic.scenario.xml.util;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.netsim.mathModel.phenomena.time.SimplePhenomenonTimeRange;
import org.mdyk.sensesim.schema.*;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Converts XML types into application specific types
 */
public class XmlTypeConverter {

    private static final Logger LOG = Logger.getLogger(XmlTypeConverter.class);

    private XmlTypeConverter() {
        // Empty constructor, just to hide the default one.
    }

    public static List<GeoPosition> convertRoute(RouteType routeType) {
        List<GeoPosition> route = new LinkedList<>();
        for (CheckpointType checkpointType : routeType.getCheckpoint()){
            route.add(covertCheckpointToPosiotion(checkpointType));
        }
        return route;
    }

    public static GeoPosition covertCheckpointToPosiotion(CheckpointType checkpointType) {
        return new GeoPosition(Double.parseDouble(checkpointType.getLatitude()),Double.parseDouble(checkpointType.getLongitude()));
    }

    public static IPhenomenonModel convertPhenomenon(PhenomenonType phenomenonType) {
        // TODO
        return null;
    }

    public static Map<IPhenomenonTimeRange, Object> discreteValueConverter(PhenomenonDiscreteValueType discreteValueType) {
        LOG.trace(">> discreteValueConverter");
        Map<IPhenomenonTimeRange, Object> phenomenonValue = new HashMap<>();

        IPhenomenonTimeRange phenomenonTime = new SimplePhenomenonTimeRange(Integer.parseInt(discreteValueType.getFromTime()) , Integer.parseInt(discreteValueType.getToTime()));
        try {
            Object value = valueConverter(discreteValueType.getValue() , discreteValueType.getFormat());
            phenomenonValue.put(phenomenonTime , value);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }

        LOG.trace("<< discreteValueConverter");
        return phenomenonValue;
    }

    public static Map<IPhenomenonTimeRange, Object> readPhenomenonValuesFromFile(String filePath) {
        LOG.debug(">>> readPhenomenonValuesFromFile [filePath = " + filePath + "]");

        Map<IPhenomenonTimeRange, Object> phenomenonValue = new HashMap<>();

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filePath),';');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                IPhenomenonTimeRange phenomenonTime = new SimplePhenomenonTimeRange(Integer.parseInt(nextLine[0]),Integer.parseInt(nextLine[1]));
                Object value = valueConverter(nextLine[2] , nextLine[3]);
                phenomenonValue.put(phenomenonTime, value);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        LOG.debug("<<< readPhenomenonValuesFromFile");
        return phenomenonValue;
    }

    private static Object valueConverter(String value , String format) throws Exception{
        Object convertedValue;
            switch(format) {
                case "INTEGER":
                    convertedValue = Integer.parseInt(value);
                    break;

                case "DOUBLE":
                    convertedValue = Double.parseDouble(value);
                    break;

                case "PHOTO_B64":
                    InputStream stream = new ByteArrayInputStream(Base64.decodeBase64(value.getBytes()));
                    convertedValue = ImageIO.read(stream);
                    break;

                case "PHOTO_FILE":
                    convertedValue = ImageIO.read(new File(value));
                    break;

                default:
                    throw new RuntimeException("Unknown phenomenon value type " + value);
            }
        return convertedValue;
    }

    public static List<AbilityType> convertAbilities(AbilitiesType abilitiesType) {
        List<AbilityType> abilities = new LinkedList<>();

        for (String abilityString : abilitiesType.getAbility()) {
            abilities.add(AbilityType.valueOf(abilityString));
        }

        return abilities;
    }

}
