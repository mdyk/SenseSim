package org.mdyk.netsim.logic.infon;


import java.util.ArrayList;

/**
 * Represents atomic item of information
 */
public class Infon {

    private String relation;
    private ArrayList<String> objects;
    private String temporalLocation;
    private String spatialLocation;
    private String polarity;

    public Infon(String infonDesc) {
        infonDesc = infonDesc.replace("<<","").replace(">>","").replaceAll("\\s","");

        String[] split = infonDesc.split(",");

        this.relation = split[0];
        this.polarity = split[split.length-1];
        this.temporalLocation = split[split.length-2];
        this.spatialLocation = split[split.length-3];

        objects = new ArrayList<>();

        for(int i = 1 ; i <= split.length-4 ; i++) {
            objects.add(split[i]);
        }

    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public ArrayList<String> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<String> objects) {
        this.objects = objects;
    }

    public String getTemporalLocation() {
        return temporalLocation;
    }

    public void setTemporalLocation(String temporalLocation) {
        this.temporalLocation = temporalLocation;
    }

    public String getPolarity() {
        return polarity;
    }

    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    public String getSpatialLocation() {
        return spatialLocation;
    }

    public void setSpatialLocation(String spatialLocation) {
        this.spatialLocation = spatialLocation;
    }
}
