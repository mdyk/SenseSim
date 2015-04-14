package org.mdyk.netsim.logic.node.program.groovy;

import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// TODO przeniesienie do oddzielnego modułu
public class GroovyMiddleware implements Middleware {

    private static final Logger LOG = Logger.getLogger(GroovyMiddleware.class);

    private SensorAPI sensorAPI;
    private List<SensorProgram> programs;

    @Override
    public void initialize() {
        sensorAPI.api_setOnMessageHandler(new Function<Message<?>, Object>() {
            @Override
            public Object apply(Message<?> message) {

                // TODO

                return null;
            }
        });
    }

    @Override
    public void setSensorAPI(SensorAPI api) {
        this.sensorAPI = api;
        this.programs = new ArrayList<>();
    }

    @Override
    public void loadProgram(SensorProgram program) {
        this.programs.add(program);
    }

    @Override
    public List<SensorProgram> getPrograms() {
        return this.programs;
    }

    @Override
    public void execute() {
        LOG.trace(">> execute");

//        for(SensorProgram sensorProgram : programs) {
//            sensorProgram.execute();
//        }

        // TODO określenie co jeszcze miałby robić middleware

        LOG.trace("<< execute");
    }
}
