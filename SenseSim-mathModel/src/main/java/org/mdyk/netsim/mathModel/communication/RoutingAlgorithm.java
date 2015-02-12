package org.mdyk.netsim.mathModel.communication;


import org.mdyk.netsim.mathModel.sensor.ISensorModel;

import java.util.List;

public interface RoutingAlgorithm {

    public List<ISensorModel<?>> getNodesToHop(ISensorModel<?> sender);

}
