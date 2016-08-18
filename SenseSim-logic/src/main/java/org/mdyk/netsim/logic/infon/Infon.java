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

    private boolean isRelationParam = false;
    private RelationParam relationParam;

    private boolean areObjectsParam = false;
    private boolean isTemporalLocationParam = false;
    private boolean isSpatialLocationParam = false;
    private boolean isPolarityParam = false;

    public Infon(String infonDesc) {
        infonDesc = infonDesc.replace("<<","").replace(">>","").replaceAll("\\s","");

        String[] split = infonDesc.split(",");

        this.relation = split[0];
        // TODO do poprawy sposob rozpoznawania parametrow
        if(relation.contains("?")) {
            isPolarityParam = true;

            relation = relation.replace("?","");
            relation = relation.replace(" ","");

            String[] splittedRelation = relation.split(":");

            relationParam = new RelationParam(splittedRelation[0] , splittedRelation[1]);

        }

        this.polarity = split[split.length-1];
        if(polarity.contains("?")) {
            isPolarityParam = true;
        }

        this.temporalLocation = split[split.length-2];
        if(temporalLocation.contains("?")) {
            isTemporalLocationParam = true;
        }

        this.spatialLocation = split[split.length-3];
        if(spatialLocation.contains("?")) {
            isSpatialLocationParam = true;
        }

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


    public boolean isRelationParam() {
        return isRelationParam;
    }

    public RelationParam getRelationParam() {
        return relationParam;
    }

    public boolean isAreObjectsParam() {
        return areObjectsParam;
    }

    public boolean isTemporalLocationParam() {
        return isTemporalLocationParam;
    }

    public boolean isSpatialLocationParam() {
        return isSpatialLocationParam;
    }

    public boolean isPolarityParam() {
        return isPolarityParam;
    }


    public class RelationParam {
        private String relationValue;
        private String relationType;

        public RelationParam(String relationValue, String relationType) {
            this.relationValue = relationValue;
            this.relationType = relationType;
        }

        public String getRelationValue() {
            return relationValue;
        }

        public void setRelationValue(String relationValue) {
            this.relationValue = relationValue;
        }

        public String getRelationType() {
            return relationType;
        }

        public void setRelationType(String relationType) {
            this.relationType = relationType;
        }
    }

}
