package org.mdyk.netsim.mathModel.device.connectivity;


import java.util.ArrayList;
import java.util.List;

public class CommunicationInterface {

    public enum TopologyType {
        ADHOC,
        FIXED,
    }
    
    private int id;
    private String name;
    private double inputBandwidth;
    private double outputBandwidth;
    private TopologyType topologyType;

    private List<Integer> connectedDevices;

    public CommunicationInterface(int id, String name, double inputBandwidth, double outputBandwidth, TopologyType topologyType) {
        this.id = id;
        this.name = name;
        this.inputBandwidth = inputBandwidth;
        this.outputBandwidth = outputBandwidth;
        this.topologyType = topologyType;
        this.connectedDevices = new ArrayList<>();
    }

    public CommunicationInterface(int id, String name, double inputBandwidth, double outputBandwidth, TopologyType topologyType, List<Integer> connectedDevices) {
        this.id = id;
        this.name = name;
        this.inputBandwidth = inputBandwidth;
        this.outputBandwidth = outputBandwidth;
        this.topologyType = topologyType;
        this.connectedDevices = connectedDevices;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getInputBandwidth() {
        return inputBandwidth;
    }

    public double getOutputBandwidth() {
        return outputBandwidth;
    }

    public TopologyType getTopologyType() {
        return topologyType;
    }

    public List<Integer> getConnectedDevices() {
        return connectedDevices;
    }
}
