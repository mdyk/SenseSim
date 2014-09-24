package org.mdyk.netsim.logic.movement;


import junit.framework.TestCase;
import org.junit.Test;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.sensesim.schema.NodeType;
import org.mdyk.sensesim.schema.Scenario;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class LineMovementAlgorithmTest {

    @Test
    public void configTest() throws Exception {
//        File file = new File("d:\\config.xml");
//        JAXBContext jaxbContext = JAXBContext.newInstance(Scenario.class);
//
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//        Scenario scenario = (Scenario) jaxbUnmarshaller.unmarshal(file);
////        System.out.println(customer);
//        System.out.println(scenario.getId());
    }

    @Test
    public void calculateNewPositionTest() throws Exception {

        LineMovementAlgorithm lineAlg = new LineMovementAlgorithm();

        lineAlg.setINCREMENT_X(1);
        lineAlg.setINCREMENT_Y(1);

        Position pos = new Position(2,3,1);

        Position newPos = lineAlg.calculateNewPosition(pos,0);

        TestCase.assertEquals(3d, newPos.getPositionX());
        TestCase.assertEquals(4d, newPos.getPositionY());
        System.out.println(newPos.getPositionX());
        System.out.println(newPos.getPositionY());

    }


    @Test
     public void  calculateNewPosition_XoutOfBounds_Test() throws Exception {
        LineMovementAlgorithm lineAlg = new LineMovementAlgorithm();

        lineAlg.setINCREMENT_X(1);
        lineAlg.setINCREMENT_Y(1);

        Position pos = new Position(401,400,1);

        Position newPos = lineAlg.calculateNewPosition(pos,0);
        TestCase.assertEquals(400d, newPos.getPositionX());
        TestCase.assertEquals(399d, newPos.getPositionY());

        newPos = lineAlg.calculateNewPosition(newPos,0);

        TestCase.assertEquals(399d, newPos.getPositionX());
        TestCase.assertEquals(398d, newPos.getPositionY());

        pos = new Position(1,1,1);

        newPos = lineAlg.calculateNewPosition(pos,0);

        TestCase.assertEquals(2d, newPos.getPositionX());
        TestCase.assertEquals(2d, newPos.getPositionY());

        newPos = lineAlg.calculateNewPosition(newPos,0);

        TestCase.assertEquals(3d, newPos.getPositionX());
        TestCase.assertEquals(3d, newPos.getPositionY());

    }

}
