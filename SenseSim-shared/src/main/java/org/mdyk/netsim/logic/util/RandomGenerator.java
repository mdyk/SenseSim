package org.mdyk.netsim.logic.util;

import java.util.Random;


public class RandomGenerator {

    public static double randomDouble(double min , double max) {
        Random rand = new Random();

        return min + (max - min) * rand.nextDouble();
    }

    public static int randomInt(int min , int max) {
        Random rand = new Random();

        return min + rand.nextInt(max - min + 1);
    }

}
