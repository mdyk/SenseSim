package org.mdyk.netsim.logic.scenario.xml;

import com.google.inject.assistedinject.Assisted;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.node.Sensor;
import org.mdyk.netsim.logic.node.SensorsFactory;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.xml.util.XmlTypeConverter;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.sensesim.schema.*;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

/**
 * Scenario read from XML file
 */
public class XMLScenario implements Scenario {

    private static final Logger LOG = Logger.getLogger(XMLScenario.class);
    private org.mdyk.sensesim.schema.Scenario scenario;
    private File scenarioFile;

    private SensorsFactory sensorsFactory;
    private PhenomenaFactory phenomenaFactory;

    @Inject
    public XMLScenario(@Assisted File file, SensorsFactory sensorsFactory, PhenomenaFactory phenomenaFactory) throws XMLScenarioLoadException {
        JAXBContext jaxbContext;
        try {
            scenarioFile = file;
            jaxbContext = JAXBContext.newInstance(org.mdyk.sensesim.schema.Scenario.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            scenario = (org.mdyk.sensesim.schema.Scenario) jaxbUnmarshaller.unmarshal(scenarioFile);
            this.sensorsFactory = sensorsFactory;
            this.phenomenaFactory = phenomenaFactory;
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
    public Map<Class, List<Sensor>> scenarioSensors() {

        Map<Class, List<Sensor>> nodesMap = new HashMap<>();

        for (NodeType nodeType : scenario.getNodes().getNode()){
            try {
                switch (nodeType.getSesnorImplType()) {
                    case "GeoSensorNode":
                        List<Sensor> geoSensorNodes;
                        if (!nodesMap.containsKey(Sensor.class)) {
                            geoSensorNodes = new LinkedList<>();
                            nodesMap.put(Sensor.class, geoSensorNodes);
                        }
                        geoSensorNodes = nodesMap.get(Sensor.class);

                        GeoPosition position = new GeoPosition(Double.parseDouble(nodeType.getStartPosition().getLatitude()),
                                Double.parseDouble(nodeType.getStartPosition().getLongitude()));
                        List<GeoPosition> route = XmlTypeConverter.convertRoute(nodeType.getRoute());

                        List<AbilityType> abilities = XmlTypeConverter.convertAbilities(nodeType.getSensorAbilities());

                        Sensor node = sensorsFactory.buildSensor(Integer.parseInt(nodeType.getId()),
                                position, Integer.parseInt(nodeType.getRadioRange()), Integer.parseInt(nodeType.getRadioBandwidth()),
                                Double.parseDouble(nodeType.getSpeed()), abilities);
                        node.getSensorLogic().setRoute(route);
//                    node.getSensorLogic().setRoutingAlgorithm(nodeType , node);
                        geoSensorNodes.add(node);
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
    public List<IPhenomenonModel<GeoPosition>> getPhenomena() {
        LOG.debug(">> getPhenomena()");

        PhenomenaType phenomenaType = scenario.getPhenomena();
        LOG.debug("Size of phenomena: " + phenomenaType.getPhenomenon().size());
        List<IPhenomenonModel<GeoPosition>> phenomenaList = new ArrayList<>(phenomenaType.getPhenomenon().size());

        for(PhenomenonType phenomenonType : phenomenaType.getPhenomenon()) {
            List<GeoPosition> phenomenonArea = XmlTypeConverter.convertRoute(phenomenonType.getPhenomenonArea());

            Map<AbilityType , Map<IPhenomenonTimeRange, Object>> phenomenonValuesMap = new HashMap<>();

            for(PhenomenonValueConfigType phenomenonValueType : phenomenonType.getPhenomenonValueSet()) {
                AbilityType abilityName =  AbilityType.valueOf(phenomenonValueType.getAbilityName());
                Map<IPhenomenonTimeRange, Object> phenomenonValues = new HashMap<>();

                if(phenomenonValueType.getCsvFile() != null) {
                    String filePath = scenarioFile.getParent() + "/" + phenomenonValueType.getCsvFile().getCsvFile();
                    phenomenonValues.putAll(XmlTypeConverter.readPhenomenonValuesFromFile(filePath));
                }
                else {
                    for(PhenomenonValueType valueType : phenomenonValueType.getPhenomenonValue()) {
                        for(PhenomenonDiscreteValueType discreteValueType : valueType.getDiscreteValue()) {
                            Map<IPhenomenonTimeRange, Object> values = XmlTypeConverter.discreteValueConverter(discreteValueType);
                            phenomenonValues.putAll(values);
                        }
                    }
                }
                phenomenonValuesMap.put(abilityName , phenomenonValues);
            }
            IPhenomenonModel phenomenon = phenomenaFactory.createPhenomenon(phenomenonValuesMap,phenomenonArea);
            phenomenaList.add(phenomenon);
        }

        LOG.debug("<< getPhenomena()");
        return phenomenaList;
    }

}
