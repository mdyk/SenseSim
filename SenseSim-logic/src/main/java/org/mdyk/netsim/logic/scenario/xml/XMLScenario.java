package org.mdyk.netsim.logic.scenario.xml;

import com.google.inject.assistedinject.Assisted;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.environment.phenomena.PhenomenaFactory;
import org.mdyk.netsim.logic.infon.Infon;
import org.mdyk.netsim.logic.node.Device;
import org.mdyk.netsim.logic.node.DevicesFactory;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.owl.KnowledgeBase;
import org.mdyk.netsim.logic.node.program.owl.OWLMiddleware;
import org.mdyk.netsim.logic.scenario.Scenario;
import org.mdyk.netsim.logic.scenario.xml.util.XmlTypeConverter;
import org.mdyk.netsim.logic.sensor.SensorFactory;
import org.mdyk.netsim.logic.util.GeoPosition;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.device.connectivity.CommunicationInterface;
import org.mdyk.netsim.mathModel.observer.ConfigurationSpace;
import org.mdyk.netsim.mathModel.phenomena.PhenomenonModel;
import org.mdyk.netsim.mathModel.phenomena.time.IPhenomenonTimeRange;
import org.mdyk.netsim.mathModel.sensor.SensorModel;
import org.mdyk.sensesim.schema.*;
import sensesim.integration.mcop.MCopPluginFactory;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scenario read from XML file
 */
public class XMLScenario implements Scenario {

    private static final Logger LOG = Logger.getLogger(XMLScenario.class);
    List<Device> devices;
    List<PhenomenonModel<GeoPosition>> phenomena;
    private org.mdyk.sensesim.schema.Scenario scenario;
    private File scenarioFile;
    private DevicesFactory devicesFactory;
    private PhenomenaFactory phenomenaFactory;
    private SensorFactory sensorFactory;
    private XmlTypeConverter xmlTypeConverter;
    private MCopPluginFactory mCopPluginFactory;

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
//            this.mCopPluginFactory = mCopPluginFactory;
            this.xmlTypeConverter = new XmlTypeConverter(scenarioFile.getParent());
        } catch (JAXBException e) {
            LOG.error(e.getMessage(), e);
            throw new XMLScenarioLoadException("Error loading scenario from file: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public void initialize() {
        this.devices = parseDevices();
        this.phenomena = parsePhenomena();
    }

    @Override
    public String scenarioName() {
        return scenario.getId();
    }

    @Override
    public List<Device> scenarioDevices() {
        return devices;
    }

    @Override
    public List<PhenomenonModel<GeoPosition>> getPhenomena() {
        return this.phenomena;
    }

    @Override
    public List<GeoPosition> getScenarioRegionPoints() {
        List<GeoPosition> scenarioRegion = new ArrayList<>();
        for (CheckpointType checkpointType : scenario.getScenarioBoundaries().getCheckpoint()) {
            GeoPosition position = xmlTypeConverter.covertCheckpointToPosiotion(checkpointType);
            scenarioRegion.add(position);
        }

        if(scenarioRegion.size() > 0) {
//            mCopPluginFactory.getMCopPlugin().setCenter(scenarioRegion.get(0).getLatitude(), scenarioRegion.get(0).getLongitude());
        }


        return scenarioRegion;
    }

    private List<Device> parseDevices() {

        List<Device> nodesList = new ArrayList<>();

        for (NodeType nodeType : scenario.getNodes().getNode()){
            try {
                switch (nodeType.getSesnorImplType()) {
                    case "GeoSensorNode":
                        GeoPosition position = new GeoPosition(Double.parseDouble(nodeType.getStartPosition().getLatitude()),
                                Double.parseDouble(nodeType.getStartPosition().getLongitude()));
                        List<GeoPosition> route = xmlTypeConverter.convertRoute(nodeType.getRoute());

                        List<AbilityType> abilities = XmlTypeConverter.convertAbilities(nodeType.getSensorAbilities());

                        List<SensorModel<?, ?>> sensorModels = new ArrayList<>();
                        if(nodeType.getSensors() != null) {
                            for (SensorClassType sensorsType : nodeType.getSensors().getSensorClass()) {
                                sensorModels.add(sensorFactory.buildSensor(sensorsType.getValue()));
                            }
                        }

                        List<CommunicationInterface> communicationInterfaces = XmlTypeConverter.convertCommunicationInterfaces(nodeType.getCommunicationInterfaces());

                        Device node = devicesFactory.buildSensor(Integer.parseInt(nodeType.getId()), nodeType.getName(),
                                position, Integer.parseInt(nodeType.getRadioRange()), Integer.parseInt(nodeType.getRadioBandwidth()),
                                Double.parseDouble(nodeType.getSpeed()), abilities, sensorModels, communicationInterfaces);
                        node.getDeviceLogic().setRoute(route);

                        Middleware middleware = node.getMiddleware();
                        // FIXME refactor w taki sposób ze nie bedzie porzebny instanceof
                        if(middleware instanceof OWLMiddleware) {
                            OWLMiddleware owlMiddleware = (OWLMiddleware) middleware;

                            String ontologyFileName;
                            String ontologyIRI;

                            if(nodeType.getNodeOntology() != null) {
                                ontologyFileName = nodeType.getNodeOntology().getOntologyFile();
                                ontologyIRI = nodeType.getNodeOntology().getOntologyIRI();
                            } else {
                                ontologyFileName = scenario.getScenarioOntology().getOntologyFile();
                                ontologyIRI = scenario.getScenarioOntology().getOntologyIRI();
                            }

                            String filePath = scenarioFile.getParent() + "/" + ontologyFileName;
                            File ontologyFile = new File(filePath);
                            owlMiddleware.loadOntology(ontologyFile, ontologyIRI);


                            // ładowanie bazy wiedzy

                            KnowledgeBaseType kbt = nodeType.getInitialKB();
                            if(kbt != null) {
                                for (String infonString : kbt.getInfon()) {

                                    infonString = infonString.replace(" ","");

                                    if (infonString.contains("=")) {
                                        // dodanie relacji

                                        String[] relationArr = infonString.split("=");
                                        String relationName = relationArr[0];
                                        KnowledgeBase.LogicOperator operator = KnowledgeBase.LogicOperator.AND;

                                        String[] infonsString = new String[0];

                                        if (relationArr[1].contains("^")) {
                                            operator = KnowledgeBase.LogicOperator.AND;
                                            infonsString = relationArr[1].split("^");
                                        } else if (relationArr[1].contains("v")) {
                                            operator = KnowledgeBase.LogicOperator.OR;
                                            infonsString = relationArr[1].split("v");
                                        } else {
                                            infonsString = new String[1];
                                            infonsString[0] = relationArr[1];
                                        }

                                        Infon[] infonsArr = new Infon[infonsString.length];
                                        for (int i = 0; i < infonsArr.length; i++) {
                                            Infon infon = new Infon(infonsString[i]);
                                            infonsArr[i] = infon;
                                        }

                                        owlMiddleware.getKb().addRelation(relationName, operator, infonsArr);

                                    } else {
                                        Infon infon = new Infon(infonString);
                                        owlMiddleware.getKb().populateKB(infon);
                                        owlMiddleware.getKb().saveKBSnapshot(0.0);
                                    }
                                }
                            }


                        }

                        if(nodeType.getMiddleware() != null && nodeType.getMiddleware().getProgramFile() != null ) {
                            for(String programFilePath : nodeType.getMiddleware().getProgramFile()) {
                                String fullPath = scenarioFile.getParent() + "/" + programFilePath;
                                String code = FileUtils.readFileToString(new File(fullPath));
                                middleware.loadProgram(code);
                            }
                        }



                        nodesList.add(node);
                        break;

                    default:
                        throw new RuntimeException("Invalid type of node");
                }
            } catch (Exception exc) {
                LOG.error(exc.getMessage() , exc);
                throw new RuntimeException("Error loading scenario sensors", exc);
            }
        }

        return nodesList;
    }


    private List<PhenomenonModel<GeoPosition>> parsePhenomena() {
        LOG.debug(">> parsePhenomena()");

        PhenomenaType phenomenaType = scenario.getPhenomena();
        LOG.debug("Size of phenomena: " + phenomenaType.getPhenomenon().size());
        List<PhenomenonModel<GeoPosition>> phenomenaList = new ArrayList<>(phenomenaType.getPhenomenon().size());

        for(PhenomenonType phenomenonType : phenomenaType.getPhenomenon()) {
            List<GeoPosition> phenomenonArea = null;
            Device attachedTo = null;
            if(phenomenonType.getPhenomenonArea() != null) {
                phenomenonArea = xmlTypeConverter.convertRoute(phenomenonType.getPhenomenonArea());
            } else {
                for(Device device : this.devices) {
                    if(device.getDeviceLogic().getID() == Integer.parseInt(phenomenonType.getAttachedTo().getNodeId())) {
                        attachedTo = device;
                    }
                }
            }

            boolean infinite = false;

            Map<AbilityType , Map<IPhenomenonTimeRange, Object>> phenomenonValuesMap = new HashMap<>();

            Map<Class , Map<IPhenomenonTimeRange, ConfigurationSpace>> phenomenonObserverValues = new HashMap<>();

            for(PhenomenonValueConfigType phenomenonValueType : phenomenonType.getPhenomenonValueSet()) {
                AbilityType abilityName =  AbilityType.valueOf(phenomenonValueType.getAbilityName());
                Map<IPhenomenonTimeRange, Object> phenomenonValues = new HashMap<>();

                if ( phenomenonValueType.getEndTime().equalsIgnoreCase("INFINITE") ) {
                    infinite = true;
                }

                String configurationClassName = phenomenonValueType.getConfigurationClass();
                String configFactoryClassName = phenomenonValueType.getConfigurationSpaceFactory();

                Class<?> configurationClass = null;
                Class<?> factoryClass = null;
                try {
                    if (configFactoryClassName != null && !configFactoryClassName.isEmpty()) {
                        factoryClass = Class.forName(configFactoryClassName);
                    }

                    if (configurationClassName != null && !configurationClassName.isEmpty()) {
                        configurationClass = Class.forName(configurationClassName);
                    }

                } catch (ClassNotFoundException e) {
                    LOG.error(e.getMessage() , e);
                    throw new RuntimeException("Error while determinig configuration class for phenomenon: " + phenomenonType.getName(),e);
                }

                if(phenomenonValueType.getCsvFile() != null) {
                    String filePath = scenarioFile.getParent() + "/" + phenomenonValueType.getCsvFile().getCsvFile();
                    phenomenonObserverValues.put(configurationClass, xmlTypeConverter.readPhenomenonValuesFromFile(filePath, configurationClass , factoryClass));
                }
                else {
                    // FIXME rezygnacja z wartości wpisywanych wprost, z pominięciem mechaniki obserwatora
                    for(PhenomenonValueType valueType : phenomenonValueType.getPhenomenonValue()) {
                        for(PhenomenonDiscreteValueType discreteValueType : valueType.getDiscreteValue()) {
                            Map<IPhenomenonTimeRange, Object> values = xmlTypeConverter.discreteValueConverter(discreteValueType);
                            phenomenonValues.putAll(values);
                        }
                        for(PhenomenonObserverValueType observerValueType : valueType.getObserverValue()) {
                            Map<IPhenomenonTimeRange, ConfigurationSpace> values = xmlTypeConverter.observerValueConverter(observerValueType, configFactoryClassName);
//                                Class<?> configurationClass = Class.forName(observerValueType.getConfigurationClass());
                            phenomenonObserverValues.put(configurationClass , values);
                        }
                    }
                }
                phenomenonValuesMap.put(abilityName , phenomenonValues);
            }

            //FIXME do poprawy po pełnym przejściu na zdarzenia budowane w oparciu o teorię percepcji

            switch(phenomenonType.getPhenomenonType()) {
                case "observer":
                    PhenomenonModel phenomenonObserver;
                    if(phenomenonArea != null) {
                        phenomenonObserver = phenomenaFactory.createPhenomenon(phenomenonType.getName(), phenomenonObserverValues, phenomenonArea, infinite);
                    }
                    else if (attachedTo != null) {
                        phenomenonObserver = phenomenaFactory.createPhenomenon(phenomenonType.getName(), phenomenonObserverValues, attachedTo, infinite);
                    }
                    else {
                        throw  new RuntimeException();
                    }
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

        LOG.debug("<< parsePhenomena()");
        return phenomenaList;
    }

    public File getScenarioFile() {
        return scenarioFile;
    }
}
