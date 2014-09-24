package org.mdyk.netsim.logic.ability;

import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.ability.IAbilityModel;

public class TemperatureAbility implements IAbilityModel {

    private AbilityType abilityType = AbilityType.TEMPERATURE;

    @Override
    public String getAbilityName() {
        return abilityType.name();
    }


}
