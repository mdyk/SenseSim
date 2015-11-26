package org.mdyk.netsim.logic.node.program.groovy;

import org.mdyk.netsim.logic.node.program.SensorProgram;


public class GroovyProgram implements SensorProgram {

    private String groovyScript;
    private boolean resend;
    private ProgramStatus programStatus;
    private Object result;

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
        return result;
    }

    @Override
    public boolean resend() {
        return this.resend;
    }

    @Override
    public String getProgram() {
        return groovyScript;
    }

    @Override
    public ProgramStatus getStatus() {
        return programStatus;
    }

    public String getGroovyScript() {
        return groovyScript;
    }

    public void setProgramStatus(ProgramStatus status) {
        this.programStatus = status;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
