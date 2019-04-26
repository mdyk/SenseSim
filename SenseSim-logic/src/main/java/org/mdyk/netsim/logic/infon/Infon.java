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

    public Infon(String relation, ArrayList<String> objects, String temporalLocation, String spatialLocation, String polarity) {
        this.relation = relation;
        this.objects = objects;
        this.temporalLocation = temporalLocation;
        this.spatialLocation = spatialLocation;
        this.polarity = polarity;
    }

    public Infon(Infon infon) {
        this(infon.toString());
    }

    public Infon(String infonDesc) {
        infonDesc = infonDesc.replace("<<","").replace(">>","").replaceAll("\\s","");

        String[] split = infonDesc.split(",");

        this.relation = split[0];

        // TODO do poprawy sposob rozpoznawania parametrow
        if(relation.contains("?")) {
            isRelationParam = true;
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

        if(objects.size() == 1 && objects.get(0).contains("?")) {
            areObjectsParam = true;
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

    public void setPolarity(boolean polarity) {
        if (polarity) {
            this.polarity = "1";
        } else {
            this.polarity = "0";
        }
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

    public boolean areObjectsParam() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Infon infon = (Infon) o;

        if (isRelationParam != infon.isRelationParam) return false;
        if (areObjectsParam != infon.areObjectsParam) return false;
        if (isTemporalLocationParam != infon.isTemporalLocationParam) return false;
        if (isSpatialLocationParam != infon.isSpatialLocationParam) return false;
        if (isPolarityParam != infon.isPolarityParam) return false;
        if (relation != null ? !relation.equals(infon.relation) : infon.relation != null) return false;
        if (objects != null ? !objects.equals(infon.objects) : infon.objects != null) return false;
        if (temporalLocation != null ? !temporalLocation.equals(infon.temporalLocation) : infon.temporalLocation != null)
            return false;
        if (spatialLocation != null ? !spatialLocation.equals(infon.spatialLocation) : infon.spatialLocation != null)
            return false;
        if (polarity != null ? !polarity.equals(infon.polarity) : infon.polarity != null) return false;
        return relationParam != null ? relationParam.equals(infon.relationParam) : infon.relationParam == null;
    }

    @Override
    public int hashCode() {
        int result = relation != null ? relation.hashCode() : 0;
        result = 31 * result + (objects != null ? objects.hashCode() : 0);
        result = 31 * result + (temporalLocation != null ? temporalLocation.hashCode() : 0);
        result = 31 * result + (spatialLocation != null ? spatialLocation.hashCode() : 0);
        result = 31 * result + (polarity != null ? polarity.hashCode() : 0);
        result = 31 * result + (isRelationParam ? 1 : 0);
        result = 31 * result + (relationParam != null ? relationParam.hashCode() : 0);
        result = 31 * result + (areObjectsParam ? 1 : 0);
        result = 31 * result + (isTemporalLocationParam ? 1 : 0);
        result = 31 * result + (isSpatialLocationParam ? 1 : 0);
        result = 31 * result + (isPolarityParam ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();

        builder.append("<<");
        builder.append(relation).append(",");

        for(String object : objects) {
            builder.append(object).append(",");
        }

        builder.append(spatialLocation).append(",");
        builder.append(temporalLocation).append(",");
        builder.append(polarity).append(",");
        builder.append(">>");

        return builder.toString();
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
