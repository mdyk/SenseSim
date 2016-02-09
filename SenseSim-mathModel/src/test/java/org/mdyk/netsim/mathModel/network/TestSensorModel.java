package org.mdyk.netsim.mathModel.network;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.ability.IAbilityModel;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.mathModel.sensor.DefaultSensorModel;

import java.util.ArrayList;
import java.util.List;


public class TestSensorModel extends DefaultSensorModel {

    protected TestSensorModel(int id, Position position, int radioRange, int velocity) {
        super(id, position, radioRange, 5000, velocity, new ArrayList<AbilityType>());
    }

    protected TestSensorModel(int id) {
        super(id, null, 0, 0, 0, new ArrayList<AbilityType>());
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
