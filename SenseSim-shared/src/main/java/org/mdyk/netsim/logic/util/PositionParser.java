package org.mdyk.netsim.logic.util;


import java.util.ArrayList;
import java.util.List;

public class PositionParser {

    private static final String LatLonDelimiter = "#";
    private static final String PositionsDelimiter = ";";

    // TODO obsługa wyjątków
    public static GeoPosition parsePosition(String positionString) {
        String[] stringArr = positionString.split(LatLonDelimiter);
        return new GeoPosition(Double.parseDouble(stringArr[0]), Double.parseDouble(stringArr[1]));
    }

    // TODO obsługa wyjątków
    public static String encodePosition(GeoPosition position) {
        return position.getLatitude() + LatLonDelimiter + position.getLongitude();
    }

    // TODO obsługa wyjątków
    public static List<GeoPosition> parsePositionsList(String positionPointsString) {
        positionPointsString = positionPointsString.replace("(","").replace(")","");
        String[] positions = positionPointsString.split(PositionsDelimiter);
        List<GeoPosition> positionsList = new ArrayList<>();
        for (String s : positions) {
            positionsList.add(parsePosition(s));
        }
        return positionsList;
    }

    // TODO obsługa wyjątków
    public static String encodePositionsList(List<GeoPosition> positions) {
        StringBuilder sb = new StringBuilder();

        for (GeoPosition gp : positions) {
            StringBuilder positionString = new StringBuilder();
            positionString.append(";(").append(gp.getLatitude()).append(LatLonDelimiter).append(gp.getLongitude()).append(")");
            sb.append(positionString);
        }

        return sb.toString().replaceFirst(";","");
    }

}
