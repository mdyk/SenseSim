package org.mdyk.netsim.view.controlls.table;


import org.mdyk.netsim.logic.node.program.SensorProgram;

public class ProgramStatistics {

    private String PID;
    private String status;
    private String output;

    public ProgramStatistics(String PID, String status) {
        this.PID = PID;
        this.status = status;
    }

    public ProgramStatistics(SensorProgram sensorProgram) {
        this.PID = String.valueOf(sensorProgram.getPID());
        this.status = sensorProgram.getStatus().name();
        this.output = sensorProgram.getOutput().toString();
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
