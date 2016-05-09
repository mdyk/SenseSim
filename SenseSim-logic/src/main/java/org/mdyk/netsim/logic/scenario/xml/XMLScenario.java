package org.mdyk.netsim.logic.scenario.xml;

import com.google.inject.assistedinject.Assisted;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.node.DevicesFactory;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.xml.util.XmlTypeConverter;
import org.mdyk.netsim.logic.sensor.SensorFactory;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpaceFactory;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.mdyk.sensesim.schema.*;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Scenario read from XML file
 */
public class XMLScenario implements Scenario {

    private static final Logger LOG = Logger.getLogger(XMLScenario.class);
    private org.mdyk.sensesim.schema.Scenario scenario;
    private File scenarioFile;

    private DevicesFactory devicesFactory;
    private PhenomenaFactory phenomenaFactory;
    private SensorFactory sensorFactory;
    private XmlTypeConverter xmlTypeConverter;

    @Inject
    public XMLScenario(@Assisted File file, DevicesFactory devicesFactory, PhenomenaFactory phenomenaFactory, SensorFactory sensorFactory) throws XMLScenarioLoadException {
        JAXBContext jaxbContext;
        try {
            scenarioFile = file;
            jaxbContext = JAXBContext.newInstance(org.mdyk.sensesim.schema.Scenario.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            scenario = (org.mdyk.sensesim.schema.Scenario) jaxbUnmarshaller.unmarshal(scenarioFile);
            this.devicesFactory = devicesFactory;
            this.phenomenaFactory = phenomenaFactory;
            this.sensorFactory = sensorFactory;
            this.xmlTypeConverter = new XmlTypeConverter(scenarioFile.getParent());
        } catch (JAXBException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLScenarioLoadException("Error loading scenario from file: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public String scenarioName() {
        return scenario.getId();
    }

    @Override
    public Map<Class, List<Device>> scenarioSensors() {

        Map<Class, List<Device>> nodesMap = new HashMap<>();

        for (NodeType nodeType : scenario.getNodes().getNode()){
            try {
                switch (nodeType.getSesnorImplType()) {
                    case "GeoSensorNode":
                        List<Device> geoDeviceNodes;
                        if (!nodesMap.containsKey(Device.class)) {
                            geoDeviceNodes = new LinkedList<>();
                            nodesMap.put(Device.class, geoDeviceNodes);
                        }
                        geoDeviceNodes = nodesMap.get(Device.class);

                        GeoPosition position = new GeoPosition(Double.parseDouble(nodeType.getStartPosition().getLatitude()),
                                Double.parseDouble(nodeType.getStartPosition().getLongitude()));
                        List<GeoPosition> route = xmlTypeConverter.convertRoute(nodeType.getRoute());

                        List<AbilityType> abilities = XmlTypeConverter.convertAbilities(nodeType.getSensorAbilities());

                        List<SensorModel<?, ?>> sensorModels = new ArrayList<>();
                        if(nodeType.getSensors() != null) {
                            for (String sensorClassName : nodeType.getSensors().getSensorClass()) {
                                sensorModels.add(sensorFactory.buildSensor(sensorClassName));
                            }
                        }

                        Device node = devicesFactory.buildSensor(Integer.parseInt(nodeType.getId()),
                                position, Integer.parseInt(nodeType.getRadioRange()), Integer.parseInt(nodeType.getRadioBandwidth()),
                                Double.parseDouble(nodeType.getSpeed()), abilities, sensorModels);
                        node.getDeviceLogic().setRoute(route);
                        geoDeviceNodes.add(node);
                        break;

                    default:
                        throw new RuntimeException("Invalid type of node");
                }
            } catch (Exception exc) {
                LOG.error(exc.getMessage() , exc);
                throw new RuntimeException("Error loading scenario sensors", exc);
            }
        }

        return nodesMap;
    }

    @Override
    public List<PhenomenonModel<GeoPosition>> getPhenomena() {
        LOG.debug(">> getPhenomena()");

        PhenomenaType phenomenaType = scenario.getPhenomena();
        LOG.debug("Size of phenomena: " + phenomenaType.getPhenomenon().size());
        List<PhenomenonModel<GeoPosition>> phenomenaList = new ArrayList<>(phenomenaType.getPhenomenon().size());

        for(PhenomenonType phenomenonType : phenomenaType.getPhenomenon()) {
            List<GeoPosition> phenomenonArea = xmlTypeConverter.convertRoute(phenomenonType.getPhenomenonArea());

            Map<AbilityType , Map<IPhenomenonTimeRange, Object>> phenomenonValuesMap = new HashMap<>();

            Map<Class , Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonObserverValues = new HashMap<>();

            for(PhenomenonValueConfigType phenomenonValueType : phenomenonType.getPhenomenonValueSet()) {
                AbilityType abilityName =  AbilityType.valueOf(phenomenonValueType.getAbilityName());
                Map<IPhenomenonTimeRange, Object> phenomenonValues = new HashMap<>();

                if(phenomenonValueType.getCsvFile() != null) {
                    String filePath = scenarioFile.getParent() + "/" + phenomenonValueType.getCsvFile().getCsvFile();
                    phenomenonValues.putAll(xmlTypeConverter.readPhenomenonValuesFromFile(filePath));
                }
                else {
                    for(PhenomenonValueType valueType : phenomenonValueType.getPhenomenonValue()) {
                        for(PhenomenonDiscreteValueType discreteValueType : valueType.getDiscreteValue()) {
                            Map<IPhenomenonTimeRange, Object> values = xmlTypeConverter.discreteValueConverter(discreteValueType);
                            phenomenonValues.putAll(values);
                        }

                        for(PhenomenonObserverValueType observerValueType : valueType.getObserverValue()) {
                            Map<IPhenomenonTimeRange, ConfigurationSpace> values = xmlTypeConverter.observerValueConverter(observerValueType);
                            try {
                                Class<?> configurationClass = Class.forName(observerValueType.getConfigurationClass());
                                phenomenonObserverValues.put(configurationClass , values);
                            } catch (ClassNotFoundException e) {
                                LOG.error(e.getMessage() , e);
                                throw new RuntimeException("Error while determinig configuration class for phenomenon: " + phenomenonType.getName(),e);
                            }
                        }
                    }
                }
                phenomenonValuesMap.put(abilityName , phenomenonValues);
            }

            //FIXME do poprawy po pełnym przejściu na zdarzenia budowane w oparciu o teorię percepcji

            switch(phenomenonType.getPhenomenonType()) {
                case "observer":
                    PhenomenonModel phenomenonObserver = phenomenaFactory.createPhenomenon(phenomenonType.getName(), phenomenonObserverValues, phenomenonArea);
                    phenomenaList.add(phenomenonObserver);
                    break;

                case "discrete":
                    PhenomenonModel phenomenon = phenomenaFactory.createPhenomenon(phenomenonValuesMap,phenomenonArea);
                    phenomenaList.add(phenomenon);
                    break;

                default:
                    throw new RuntimeException("Phenomenon type should be 'observer' or 'discrete'");
            }
        }

        LOG.debug("<< getPhenomena()");
        return phenomenaList;
    }

}
