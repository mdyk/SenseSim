package org.mdyk.netsim.logic.scenario;


import org.mdyk.netsim.logic.scenario.xml.XMLScenario;

import java.io.File;

public interface ScenarioFactory {

    XMLScenario createXMLScenario(File fileName);

}
