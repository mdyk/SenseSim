package org.mdyk.netsim.logic.node.program.groovy;

import org.mdyk.netsim.logic.node.program.SensorProgram;


public class GroovyProgram implements SensorProgram {

    private String groovyScript;

    private int PID;

    public GroovyProgram(String groovyScript) {
        this.groovyScript = groovyScript;
    }

    @Override
    public void setPID(int PID) {
        this.PID = PID;
    }

    @Override
    public int getPID() {
        return PID;
    }

    @Override
    public void setParams(Object... params) {
        // Unused
    }

    @Override
    public Object getResult() {
        return null;
    }

    public String getGroovyScript() {
        return groovyScript;
    }

}
