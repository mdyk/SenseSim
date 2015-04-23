package org.mdyk.netsim.logic.node.program.groovy;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

// TODO przeniesienie do oddzielnego modułu
public class GroovyMiddleware extends Thread implements Middleware {

    private static final Logger LOG = Logger.getLogger(GroovyMiddleware.class);

    private SensorAPI sensorAPI;
    private SensorSimEntity sensorSimEntity;
    private Map<Integer, SensorProgram> programs;
    private GroovyShell groovyShell;

    int PID = 0;

    public GroovyMiddleware() {
        programs = new ConcurrentHashMap<>();
        groovyShell = new GroovyShell();
    }

    @Override
    public void initialize() {
        sensorAPI.api_setOnMessageHandler(new Function<Message, Object>() {
            @Override
            public Object apply(Message message) {

                Object messageContent = message.getMessageContent();

                if(messageContent instanceof String){
                    LOG.trace("messageContent is String");



                }

                return null;
            }
        });
        this.start();
    }

    @Override
    public void setSensorAPI(SensorAPI api) {
        this.sensorAPI = api;
    }

    @Override
    public void setSensorSimEntity(SensorSimEntity simEntity) {
        this.sensorSimEntity = simEntity;
    }

    @Override
    public void loadProgram(SensorProgram program) {
        this.programs.put(PID++,program);
    }

    @Override
    public List<SensorProgram> getPrograms() {
         return new ArrayList<>(programs.values());
    }

    @Override
    public void execute() {
//        LOG.trace(">> execute");


//        Iterator<SensorProgram> programsList = programs.values().iterator();
        Iterator<Map.Entry<Integer,SensorProgram>> iter = programs.entrySet().iterator();

        while(iter.hasNext()) {
            Map.Entry<Integer,SensorProgram> entry = iter.next();
            GroovyProgram groovyProgram = (GroovyProgram) entry.getValue();
            Script scriptToRun = groovyShell.parse(groovyProgram.getGroovyScript());

            final GroovyMiddleware me = this;

            new Thread()
            {
                public void run() {
                    me.sensorSimEntity.startProgramExecution(PID);
                    scriptToRun.run();
                    me.sensorSimEntity.endProgramExecution(PID);

                }
            }.start();

            programs.remove(entry.getKey());
        }

        // TODO określenie co jeszcze miałby robić middleware

//        LOG.trace("<< execute");
    }

    @Override
    public void run() {
        // TODO określenie warunków stopu pętli
        while (true) {
            execute();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage() , e);
            }
        }

    }
}
