package org.mdyk.netsim.logic.node.program.groovy;

import groovy.lang.GroovyShell;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// TODO przeniesienie do oddzielnego modułu
public class GroovyMiddleware extends Thread implements Middleware {

    private static final Logger LOG = Logger.getLogger(GroovyMiddleware.class);

    private SensorAPI sensorAPI;
    private List<SensorProgram> programs;

    private GroovyShell groovyShell;

    public GroovyMiddleware() {
        programs = new ArrayList<>();
        groovyShell = new GroovyShell();
    }

    @Override
    public void initialize() {
        sensorAPI.api_setOnMessageHandler(new Function<Message, Object>() {
            @Override
            public Object apply(Message message) {

//                message.getMessageContent();


                return null;
            }
        });
        this.start();
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
//        LOG.trace(">> execute");

//        for(SensorProgram sensorProgram : programs) {
//            sensorProgram.execute();
//        }

        // TODO określenie co jeszcze miałby robić middleware

//        LOG.trace("<< execute");
    }

    @Override
    public void run() {
//        super.run();    //To change body of overridden methods use File | Settings | File Templates.

        while (true) {
            execute();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
