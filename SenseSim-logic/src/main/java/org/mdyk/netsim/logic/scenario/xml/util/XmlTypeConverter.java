package org.mdyk.netsim.logic.scenario.xml.util;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpaceFactory;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.netsim.mathModel.phenomena.time.SimplePhenomenonTimeRange;
import org.mdyk.sensesim.schema.*;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Converts XML types into application specific types
 */
public class XmlTypeConverter {

    private static final Logger LOG = Logger.getLogger(XmlTypeConverter.class);

    private String scenarioFilePath;

    public XmlTypeConverter(String scenarioFilePath) {
        this.scenarioFilePath = scenarioFilePath;
    }

    public List<GeoPosition> convertRoute(RouteType routeType) {
        List<GeoPosition> route = new LinkedList<>();
        for (CheckpointType checkpointType : routeType.getCheckpoint()){
            route.add(covertCheckpointToPosiotion(checkpointType));
        }
        return route;
    }

    public GeoPosition covertCheckpointToPosiotion(CheckpointType checkpointType) {
        return new GeoPosition(Double.parseDouble(checkpointType.getLatitude()),Double.parseDouble(checkpointType.getLongitude()));
    }

    public PhenomenonModel convertPhenomenon(PhenomenonType phenomenonType) {
        // TODO
        return null;
    }

    public Map<IPhenomenonTimeRange, Object> discreteValueConverter(PhenomenonDiscreteValueType discreteValueType) {
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

    public Map<IPhenomenonTimeRange, ConfigurationSpace> observerValueConverter(PhenomenonObserverValueType observerValueType) {
        LOG.trace(">> observerValueConverter");
        Map<IPhenomenonTimeRange, ConfigurationSpace> phenomenonValue = new HashMap<>();
        IPhenomenonTimeRange phenomenonTime = new SimplePhenomenonTimeRange(Integer.parseInt(observerValueType.getFromTime()) , Integer.parseInt(observerValueType.getToTime()));


        String factoryClassName = observerValueType.getConfigurationSpaceFactory();
        LOG.debug("factoryClassName="+factoryClassName);
        try {
            Class factoryClass = Class.forName(factoryClassName);
            Constructor factoryConstructor = factoryClass.getConstructor();
            ConfigurationSpaceFactory factory = (ConfigurationSpaceFactory) factoryConstructor.newInstance();

            ConfigurationSpace confSpace = factory.buildConfigurationSpace(observerValueType.getValue());
            phenomenonValue.put(phenomenonTime , confSpace);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }

        LOG.trace("<< observerValueConverter");
        return phenomenonValue;
    }

    public Map<IPhenomenonTimeRange, Object> readPhenomenonValuesFromFile(String valuefilePath) {
        LOG.debug(">>> readPhenomenonValuesFromFile [filePath = " + valuefilePath + "]");

        Map<IPhenomenonTimeRange, Object> phenomenonValue = new HashMap<>();

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(valuefilePath),';');
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

    private Object valueConverter(String value , String format) throws Exception{
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
                    String photoFilePath = scenarioFilePath +'/'+ value;
                    File imageFile = new File(photoFilePath);

                    if(!imageFile.exists()) {
                        throw new RuntimeException("Cannot open file " + photoFilePath);
                    }

                    convertedValue = ImageIO.read(imageFile);
                    break;

                default:
                    throw new RuntimeException("Unknown phenomenon value type " + value);
            }
        return convertedValue;
    }

    public static List<CommunicationInterface> convertCommunicationInterfaces(CommunicationInterfacesType communicationInterfacesType) {
        List<CommunicationInterface> communicationInterfaces = new ArrayList<>();

        if(communicationInterfacesType != null) {
            if (communicationInterfacesType.getCommunicationInterface() == null || communicationInterfacesType.getCommunicationInterface().size() == 0) {
                throw new RuntimeException("No communication interface defined");
            }

            for (CommunicationInterfaceType interfaceType : communicationInterfacesType.getCommunicationInterface()) {
                CommunicationInterface.TopologyType topologyType;

                if (interfaceType.getTopologyType().getAdhocM2M() != null) {
                    topologyType = CommunicationInterface.TopologyType.ADHOC;
                } else if (interfaceType.getTopologyType().getFixedTopology() != null) {
                    topologyType = CommunicationInterface.TopologyType.FIXED;
                } else {
                    throw new RuntimeException("Topology type not set");
                }
                CommunicationInterface communicationInterface = null;
                if(topologyType == CommunicationInterface.TopologyType.ADHOC) {
                    communicationInterface = new CommunicationInterface(Integer.parseInt(interfaceType.getId()),
                            interfaceType.getName(), Double.parseDouble(interfaceType.getRadioInputBandwidth()),
                            Double.parseDouble(interfaceType.getRadioOutputBandwidth()), topologyType);
                }

                if(topologyType == CommunicationInterface.TopologyType.FIXED) {
                    List<Integer> connectedDevices = new ArrayList<>();
                    for (String devId : interfaceType.getTopologyType().getFixedTopology().getConnectedDeviceId()) {
                        connectedDevices.add(Integer.parseInt(devId));
                    }
                    communicationInterface = new CommunicationInterface(Integer.parseInt(interfaceType.getId()),
                            interfaceType.getName(), Double.parseDouble(interfaceType.getRadioInputBandwidth()),
                            Double.parseDouble(interfaceType.getRadioOutputBandwidth()), topologyType, connectedDevices);
                }

                communicationInterfaces.add(communicationInterface);

            }
        }

        return communicationInterfaces;
    }

    public static List<AbilityType> convertAbilities(AbilitiesType abilitiesType) {
        List<AbilityType> abilities = new LinkedList<>();

        for (String abilityString : abilitiesType.getAbility()) {
            abilities.add(AbilityType.valueOf(abilityString));
        }

        return abilities;
    }

}
