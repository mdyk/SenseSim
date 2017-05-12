package org.mdyk.sensesim.simulation.engine.dissim.nodes.events;

import dissim.simspace.core.SimControlException;
import dissim.simspace.process.BasicSimAction;
import org.apache.log4j.Logger;

/**
 * Created by Michal on 2017-05-12.
 */
public class MoveActivity extends BasicSimAction<DisSimNodeEntity, Object> {


    private static final Logger LOG = Logger.getLogger(MoveActivity.class);
    /**
     * Delay of EndMoveActivity occurrence in seconds.
     */
    public static final double END_MOVE_DELAY = 0.2;
    public static final double MOVE_DURATION = 0.01;

    private DisSimNodeEntity disSimNodeEntity;

    public MoveActivity(DisSimNodeEntity entity) throws SimControlException {
        super(entity, Math.random() , END_MOVE_DELAY , MOVE_DURATION);
        this.disSimNodeEntity = entity;
    }

    @Override
    protected void transitionOnStart() throws SimControlException {
        
    }

    @Override
    protected void transitionOnFinish() throws SimControlException {
        disSimNodeEntity.getProgrammableNode().move();
//        disSimNodeEntity.notifyObservers(MoveActivity.class);
    }

    @Override
    protected void onInterruption() throws SimControlException {
        getSimEntity().getProgrammableNode().stopMoveing();
    }
}
