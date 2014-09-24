package util;


import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.util.RandomGenerator;

public class RandomGeneratorTest {

    @Test
    public void randomDoubleTest () throws Exception {

        double randomVal = RandomGenerator.randomDouble(-5,5);
        System.out.println(randomVal);
        TestCase.assertTrue(randomVal <= 5 && randomVal >=-5);

    }

    @Test
    public void randomIntTest () throws Exception {

        int randomVal = RandomGenerator.randomInt(-5,5);
        System.out.println(randomVal);
        TestCase.assertTrue(randomVal <= 5 && randomVal >=-5);

    }

}
