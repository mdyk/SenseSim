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
import org.mdyk.netsim.logic.node.statistics.event.StatisticsEvent;

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
        PID++;
        this.programs.put(PID,program);
        program.setPID(PID);
        ((GroovyProgram)program).setProgramStatus(SensorProgram.ProgramStatus.LOADED);
        HashMap<Integer , SensorProgram> sensorIdProgramMap = new HashMap<>();
        sensorIdProgramMap.put(this.nodeId , program);
        EventBusHolder.getEventBus().post(new StatisticsEvent(StatisticsEvent.EventType.PROGRAM_LOADED, sensorIdProgramMap));
    }

    @Override
    public List<SensorProgram> getPrograms() {
         return new ArrayList<>(programs.values());
    }

    @Override
    public void execute() {
        LOG.trace(">> execute");
        for (Map.Entry<Integer, SensorProgram> entry : programs.entrySet()) {
            GroovyProgram groovyProgram = (GroovyProgram) entry.getValue();

            if (groovyProgram.getStatus() != SensorProgram.ProgramStatus.LOADED) {
                continue;
            }

            Script scriptToRun = groovyShell.parse(groovyProgram.getGroovyScript());

            final GroovyMiddleware me = this;

            new Thread() {
                public void run() {
                    LOG.info("Running program with PID="+PID);
                    me.sensorSimEntity.startProgramExecution(PID);
                    Map<String, Object> params = new HashMap<>();
                    params.put("api", sensorAPI);
                    scriptToRun.setBinding(new Binding(params));
                    try {
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.DURING_ECECUTION);
                        LOG.debug("PID="+PID+" DURING_ECECUTION");
                        Object result = scriptToRun.run();
                        LOG.debug("PID="+PID+" result="+result);
                        groovyProgram.setResult(result);
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.FINISHED_OK);
                        LOG.debug("PID="+PID+" FINISHED_OK");
                    } catch (Exception exc) {
                        LOG.error(exc.getMessage(), exc);
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.FINISHED_ERROR);
                        LOG.debug("PID="+PID+" FINISHED_ERROR");
                    } finally {
                        EventBusHolder.getEventBus().post(new StatisticsEvent(StatisticsEvent.EventType.PROGRAM_UPDATED, groovyProgram));
                    }
                    me.sensorSimEntity.endProgramExecution(PID);

                }
            }.start();

            if (groovyProgram.resend()) {
                resendProgram(groovyProgram);
            }

            LOG.trace("<< execute");
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
        List<Integer> neighbours = sensorAPI.api_scanForNeighbors();
        for (Integer neighbour : neighbours) {
            sensorAPI.api_sendMessage(program.getGroovyScript().hashCode(), sensorAPI.api_getMyID(), neighbour, program.getGroovyScript() , null );
        }
        LOG.trace("<< resendProgram");
    }

}
