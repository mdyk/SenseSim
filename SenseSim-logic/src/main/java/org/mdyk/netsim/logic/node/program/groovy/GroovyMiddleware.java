package org.mdyk.netsim.logic.node.program.groovy;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.mdyk.netsim.logic.communication.Message;
import org.mdyk.netsim.logic.event.EventBusHolder;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.api.SensorAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.simentity.SensorSimEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

// TODO przeniesienie do oddzielnego modułu
public class GroovyMiddleware extends Thread implements Middleware {

    private static final Logger LOG = Logger.getLogger(GroovyMiddleware.class);

    private SensorAPI sensorAPI;
    private SensorSimEntity sensorSimEntity;
    private Map<Integer, SensorProgram> programs;
    private GroovyShell groovyShell;

    private int PID = 0;
    private int nodeId;

    public GroovyMiddleware() {
        programs = new ConcurrentHashMap<>();
        groovyShell = new GroovyShell();
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    public void initialize() {
        sensorAPI.api_setOnMessageHandler(new Function<Message, Object>() {
            @Override
            public Object apply(Message message) {

                Object messageContent = message.getMessageContent();

                if(messageContent instanceof String){
                    LOG.trace("messageContent is String");
                    String groovyScript = (String) messageContent;
                    Script scriptToRun = groovyShell.parse(groovyScript);

                    if(scriptToRun != null) {
                        LOG.debug("scriptToRun != null");
                        GroovyProgram groovyProgram = new GroovyProgram(groovyScript, true);
                        loadProgram(groovyProgram);
                    }

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
        this.nodeId = this.sensorSimEntity.getSensorLogic().getID();
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
                    Map<String, Object> params = new HashMap<>();
                    params.put("api" , sensorAPI);
                    scriptToRun.setBinding(new Binding(params));
                    try{
                        scriptToRun.run();
                    } catch (Exception exc) {
                        LOG.error(exc.getMessage(),exc);
                    }
                    me.sensorSimEntity.endProgramExecution(PID);

                }
            }.start();
            resendProgram(groovyProgram);
            programs.remove(entry.getKey());
        }

        // TODO określenie co jeszcze miałby robić middleware
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

    @Subscribe
    @AllowConcurrentEvents
    @SuppressWarnings("unchecked")
    public void processEvent(InternalEvent event) {
        LOG.trace(">> processEvent");
        try{
            switch(event.getEventType()){
                case LOAD_PROGRAM:
                    LOG.debug(">> LOAD_PROGRAM event");
                    Pair<Integer, String> programToInstall = (Pair<Integer, String>) event.getPayload();
                    // The program should be installed in current node.
                    if(programToInstall.getKey()!=null && programToInstall.getKey().equals(nodeId)){
                        LOG.debug("Installing program on node: " + nodeId);
                        GroovyProgram groovyProgram = new GroovyProgram(programToInstall.getValue(), true);
                        loadProgram(groovyProgram);
                    }
                    break;
            }
        } catch (Exception exc){
            LOG.error(exc.getMessage() ,exc);
        }
        LOG.trace("<< processEvent");
    }

    @SuppressWarnings("unchecked")
    private void resendProgram(GroovyProgram program) {
        LOG.trace(">> resendProgram");
        if(program.resend()) {
            List<Integer> neighbours = sensorAPI.api_scanForNeighbors();
            for (Integer neighbour : neighbours) {
                sensorAPI.api_sendMessage(sensorAPI.api_getMyID(), neighbour, program.getGroovyScript() , null );
            }
        }
        LOG.trace("<< resendProgram");
    }

}
