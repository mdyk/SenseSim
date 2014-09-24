package org.mdyk.netsim.logic.movement;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.logic.util.RandomGenerator;


/**
 *  Algorytm ruchu po linii
 */
public class LineMovementAlgorithm implements SimpleMovementAlgorithm {

    private enum DirectionType { desc , asc }

    private int DIRECTION_X = 1;
    private int DIRECTION_Y = 1;

    private int INCREMENT_X = 1;
    private int INCREMENT_Y = 1;

    @Override
    public Position calculateNewPosition(Position currentPosition, double speeds) {

        double currentX = currentPosition.getPositionX();
        double currentY = currentPosition.getPositionY();

        adjustMovement(currentPosition);

        double newX = currentX + getXIncrement();
        double newY = currentY + getYIncrement();

        // TODO Obsługa 3-go wymiaru
        return new Position(newX , newY, 0);
    }

    /**
     * Weryfikuje, czy obiekt nie wyszedł poza obszar. Jeśli tak, ustalany jest przeciwny
     * kierunek ruchu.
     */
    private void adjustMovement(Position currentPosition) {
        double currentX = currentPosition.getPositionX();
        double currentY = currentPosition.getPositionY();
        // TODO zakres ruchu powinien być określony w konfiguracji
        if(currentX >= 400) {
            changeDirectionX(DirectionType.desc);
        }

        if(currentX <= 1) {
            changeDirectionX(DirectionType.asc);
        }

        if(currentY >= 400) {
            changeDirectionY(DirectionType.desc);
        }

        if(currentY <= 1) {
            changeDirectionY(DirectionType.asc);
        }
    }

    private int getXIncrement() {
        return DIRECTION_X * INCREMENT_X;
    }

    private int getYIncrement() {
        return DIRECTION_Y * INCREMENT_Y;
    }

    private void changeDirectionX(DirectionType direction) {
        switch (direction) {
            case asc:
                DIRECTION_X = 1;
                break;
            case desc:
                DIRECTION_X = -1;
                break;
        }
    }

    private void changeDirectionY(DirectionType direction) {
        switch (direction) {
            case asc:
                DIRECTION_Y = 1;
                break;
            case desc:
                DIRECTION_Y = -1;
                break;
        }
    }

    @Override
    public void randomizeParams() {

        INCREMENT_Y = RandomGenerator.randomInt(-5,5);
        INCREMENT_X = RandomGenerator.randomInt(-5,5);
    }

    /**
     * TEST ONLY
     */
    @Deprecated
    public void setINCREMENT_X(int INCREMENT_X) {
        this.INCREMENT_X = INCREMENT_X;
    }

    /**
     * TEST ONLY
     */
    @Deprecated
    public void setINCREMENT_Y(int INCREMENT_Y) {
        this.INCREMENT_Y = INCREMENT_Y;
    }
}
