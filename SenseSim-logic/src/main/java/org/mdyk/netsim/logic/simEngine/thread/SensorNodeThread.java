package org.mdyk.netsim.logic.simEngine.thread;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.movement.MovementAlgorithm;
import org.mdyk.netsim.mathModel.sensor.SensorNode;
import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Prosta implementacja węzła sieci
 */
public abstract class  SensorNodeThread<P extends Position, M extends MovementAlgorithm>
                      extends DefaultSensorModel<P> implements SensorNode<P>, Runnable {

    public enum State{ACTIVE, PAUSED}

    private static final Logger LOG = Logger.getLogger(SensorNodeThread.class);
    List<M>             movementAlgs;
    protected M         currentMovementAlg;
    private State       nodeState;
    private boolean     working = true;

    private Object lock;

    public SensorNodeThread(int id, P position, int radioRange, double velocity, List<AbilityType> abilities) {
        super(id,position,radioRange, 5000, velocity,abilities);
        initPosition();

        movementAlgs = new LinkedList<>();
        nodeState = State.ACTIVE;
    }

    /**
     * Method executed (inside run()) before node starts working
     */
    protected abstract void init();

    protected abstract void initPosition();

    @Override
    public void run() {
        init();
        lock = new Object();
        while(working) {
            while (nodeState == State.ACTIVE && working) {
                work();
            }

            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        work();
    }

    private void changeMovementAlg(){
        // TODO zmiana algorytmu ruchu
    }


    @Override
    public void startNode() {
        LOG.debug("Węzeł wystartował");
        new Thread(this).start();
    }

    @Override
    public void stopNode() {
        this.working = false;

    }

    @Override
    public void pauseNode() {
        this.nodeState = State.PAUSED;
    }

    @Override
    public void resumeNode() {
        this.nodeState = State.ACTIVE;
        synchronized (lock){
            lock.notifyAll();
        }
    }
}
