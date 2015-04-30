package org.mdyk.netsim.logic.node.program.groovy;

import org.mdyk.netsim.logic.node.program.SensorProgram;


public class GroovyProgram implements SensorProgram {

    private String groovyScript;
    private boolean resend;

    private int PID;

    public GroovyProgram(String groovyScript, boolean resend) {
        this.groovyScript = groovyScript;
        this.resend = resend;
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

    @Override
    public boolean resend() {
        return this.resend;
    }

    public String getGroovyScript() {
        return groovyScript;
    }

}
