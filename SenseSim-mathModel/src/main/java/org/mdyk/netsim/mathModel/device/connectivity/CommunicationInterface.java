package org.mdyk.netsim.mathModel.device.connectivity;


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

    public CommunicationInterface(int id, String name, double inputBandwidth, double outputBandwidth, TopologyType topologyType) {
        this.id = id;
        this.name = name;
        this.inputBandwidth = inputBandwidth;
        this.outputBandwidth = outputBandwidth;
        this.topologyType = topologyType;
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
}
