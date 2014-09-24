package org.mdyk.netsim.logic.scenario.xml;


public class XMLScenarioLoadException extends Exception {

    public XMLScenarioLoadException(String message){
        super(message);
    }

    public XMLScenarioLoadException(String message, Exception exc){
        super(message, exc);
    }
}
