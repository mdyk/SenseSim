package org.mdyk.netsim.mathModel.network;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.IAbilityModel;
import org.mdyk.netsim.mathModel.communication.Message;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;

import java.util.List;


public class TestSensorModel extends DefaultSensorModel {

    protected TestSensorModel(int id, Position position, int radioRange, int velocity) {
        super(id, position, radioRange, velocity, null);
    }

    protected TestSensorModel(int id) {
        super(id, null, 0, 0, null);
    }

    @Override
    public void sense() {
        // unused
    }

    @Override
    public List<IAbilityModel> getAbilities() {
        return null;
    }

    @Override
    protected void onMessage(double time, Message message) {
        // unused
    }
}
