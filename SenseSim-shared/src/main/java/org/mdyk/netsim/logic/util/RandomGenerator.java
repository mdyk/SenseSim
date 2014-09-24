package org.mdyk.netsim.logic.util;

import java.util.Random;


public class RandomGenerator {

    public static double randomDouble(double min , double max) {
        Random rand = new Random();

        double genratedValue = min + (max - min) * rand.nextDouble();

        return genratedValue;
    }

    public static int randomInt(int min , int max) {
        Random rand = new Random();

        int genratedValue = min + rand.nextInt(max - min + 1);

        return genratedValue;
    }

}
