package org.mdyk.netsim.logic.scenario.xml.util;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.event.IPhenomenonModel;
import org.mdyk.netsim.mathModel.event.time.IPhenomenonTime;
import org.mdyk.netsim.mathModel.event.time.SimplePhenomenonTime;
import org.mdyk.sensesim.schema.AbilitiesType;
import org.mdyk.sensesim.schema.CheckpointType;
import org.mdyk.sensesim.schema.PhenomenonType;
import org.mdyk.sensesim.schema.RouteType;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Converts XML types into application specific types
 */
public class XmlTypeConverter {

    private static final Logger logger = Logger.getLogger(XmlTypeConverter.class);

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

    public static Map<IPhenomenonTime , Object> readPhenomenonValuesFromFile(String filePath) {
        logger.debug(">>> readPhenomenonValuesFromFile [filePath = "+filePath+"]");

        Map<IPhenomenonTime, Object> phenomenonValue = new HashMap<>();

        CSVReader reader = null;
        try
        {
            reader = new CSVReader(new FileReader(filePath),';');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null)
            {
                IPhenomenonTime phenomenonTime = new SimplePhenomenonTime(Integer.parseInt(nextLine[0]),Integer.parseInt(nextLine[1]));
                Object value = null;
                switch(nextLine[3]) {
                    case "INTEGER":
                        value = Integer.parseInt(nextLine[2]);
                        break;

                    case "DOUBLE":
                        value = Double.parseDouble(nextLine[2]);
                        break;

                    case "BINARY":
                        // TODO
                        break;

                    default:
                        throw new RuntimeException("Unknown phenomenon value type " + nextLine[3]);
                }

                phenomenonValue.put(phenomenonTime, value);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage() , e);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage() , e);
            }
        }

        logger.debug("<<<t readPhenomenonValuesFromFile");
        return phenomenonValue;
    }

    public static List<AbilityType> convertAbilities(AbilitiesType abilitiesType) {
        List<AbilityType> abilities = new LinkedList<>();

        for (String abilityString : abilitiesType.getAbility()) {
            abilities.add(AbilityType.valueOf(abilityString));
        }

        return abilities;
    }

}
