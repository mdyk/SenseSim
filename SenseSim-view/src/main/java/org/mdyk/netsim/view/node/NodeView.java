package org.mdyk.netsim.view.node;

import org.mdyk.netsim.logic.util.Position;
import org.mdyk.netsim.mathModel.ability.AbilityType;
import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.ArrayList;
import java.util.List;

/**
  Abstract class for node view

 */
public abstract class NodeView<C, P extends Position> {

    protected ISensorModel<P> node;
    protected C nodeContainer;

    public NodeView(ISensorModel node, C nodeContainer) {
        this.node = node;
        this.nodeContainer = nodeContainer;
        prepareView();
    }

    protected abstract void prepareView();

    /**
     * Renders node in provided nodeContainer
     */
    public abstract void renderNode();

    public abstract void relocate(P newPosition);

    protected abstract void relocateEdges(P newPosition);

    public int getID() {
        return node.getID();
    }

    public List<AbilityType> getAbilities() {
        return node.getAbilities();
    }

    public List<String> getAbilitesNames() {
        ArrayList<String> abilityNames = new ArrayList<>(getAbilities().size());

        for(AbilityType ability : getAbilities()) {
            abilityNames.add(ability.name());
        }

        return abilityNames;
    }

    public abstract void setEdge(ISensorModel secondEndNode);

    public abstract void removeEdge(ISensorModel secondEndNode);

    public abstract P getNodePosition();

    public ISensorModel<P> getNode() {
        return node;
    }

}
