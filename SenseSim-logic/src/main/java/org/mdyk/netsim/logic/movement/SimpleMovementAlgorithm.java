package org.mdyk.netsim.logic.movement;

import org.mdyk.netsim.logic.util.Position;

/**
 *  Interfejs do algorytmu ruchu węzłów
 */
public interface SimpleMovementAlgorithm extends MovementAlgorithm {

    /**
     * Określa nową pozycję węzła
     * @param currentPosition
     * @return
     */
    public Position calculateNewPosition(Position currentPosition, double speed);

    public void randomizeParams();

}
