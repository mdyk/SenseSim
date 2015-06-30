package org.mdyk.netsim.logic.scenario.xml;

import com.google.inject.assistedinject.Assisted;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.routing.FloodingRouting;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.node.Sensor;
import org.mdyk.netsim.logic.node.SensorsFactory;
import org.mdyk.netsim.logic.node.geo.SensorLogic;
import org.mdyk.netsim.logic.simEngine.thread.GeoSensorNodeThread;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.xml.util.XmlTypeConverter;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.phenomena.IPhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;
import org.mdyk.sensesim.schema.NodeType;
import org.mdyk.sensesim.schema.PhenomenaType;
import org.mdyk.sensesim.schema.PhenomenonType;

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
            switch(nodeType.getSesnorImplType()){
                case "GeoSensorNode":
                    List<Sensor> geoSensorNodes;
                    if(!nodesMap.containsKey(GeoSensorNodeThread.class)){
                        geoSensorNodes = new LinkedList<>();
                        nodesMap.put(GeoSensorNodeThread.class,geoSensorNodes);
                    }
                    geoSensorNodes = nodesMap.get(GeoSensorNodeThread.class);

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
        }

        return nodesMap;
    }

    private void setRoutingAlgorithm(NodeType nodeType , ISensorModel<?> sensorModel) {
        switch (nodeType.getRoutingAlgType()) {
            case "FloodingRouting":
                sensorModel.setRoutingAlgorithm(new FloodingRouting());
                break;

            default:
                throw new RuntimeException("Invalid type of routing algorithm");
        }
    }

    @Override
    public List<IPhenomenonModel<GeoPosition>> getPhenomena() {
        LOG.debug(">>> getPhenomena()");

        PhenomenaType phenomenaType = scenario.getPhenomena();
        LOG.debug("Size of phenomena: " + phenomenaType.getPhenomenon().size());

        List<IPhenomenonModel<GeoPosition>> phenomenaList = new ArrayList<>(phenomenaType.getPhenomenon().size());

        for(PhenomenonType phenomenonType : phenomenaType.getPhenomenon()) {
            List<GeoPosition> phenomenonArea = XmlTypeConverter.convertRoute(phenomenonType.getPhenomenonArea());
            AbilityType abilityName =  AbilityType.valueOf(phenomenonType.getAbilityName());

            // TODO obsługa przypadku kiedy nie ma pliku
            Map<IPhenomenonTimeRange, Object> phenomenonValues = new HashMap<>();
            if(phenomenonType.getCsvFile() != null) {
                String filePath = scenarioFile.getParent() + "/" + phenomenonType.getCsvFile().getCsvFile();
                phenomenonValues.putAll(XmlTypeConverter.readPhenomenonValuesFromFile(filePath));
            }

            IPhenomenonModel phenomenon = phenomenaFactory.createPhenomenon(phenomenonValues, abilityName,phenomenonArea);
            phenomenaList.add(phenomenon);
        }

        LOG.debug("<<< getPhenomena()");
        return phenomenaList;
    }

}
