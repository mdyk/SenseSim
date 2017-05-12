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
import org.mdyk.netsim.logic.event.EventType;
import org.mdyk.netsim.logic.event.InternalEvent;
import org.mdyk.netsim.logic.node.api.DeviceAPI;
import org.mdyk.netsim.logic.node.program.Middleware;
import org.mdyk.netsim.logic.node.program.SensorProgram;
import org.mdyk.netsim.logic.node.simentity.DeviceSimEntity;
import org.mdyk.netsim.logic.node.statistics.event.DeviceStatisticsEvent;
import sensesim.integration.mcop.MCopPlugin;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

// TODO przeniesienie do oddzielnego modułu
public class GroovyMiddleware extends Thread implements Middleware {

    private static final Logger LOG = Logger.getLogger(GroovyMiddleware.class);

    private DeviceAPI deviceAPI;
    private DeviceSimEntity deviceSimEntity;
    private Map<Integer, SensorProgram> programs;
    private GroovyShell groovyShell;

    private MCopPlugin mCopPlugin;

    private int PID = 0;
    private int nodeId;

    public GroovyMiddleware(MCopPlugin mCopPlugin) {
        programs = new ConcurrentHashMap<>();
        groovyShell = new GroovyShell();
        this.mCopPlugin = mCopPlugin;
        EventBusHolder.getEventBus().register(this);
    }

    @Override
    public void initialize() {
//        deviceAPI.api_setOnMessageHandler(new Function<Message, Object>() {
//            @Override
//            public Object apply(Message message) {
//
//                Object messageContent = message.getMessageContent();
//
//                if(messageContent instanceof String){
//                    LOG.trace("messageContent is String");
//                    String groovyScript = (String) messageContent;
//                    Script scriptToRun = groovyShell.parse(groovyScript);
//
//                    if(scriptToRun != null) {
//                        LOG.debug("scriptToRun != null");
//                        GroovyProgram groovyProgram = new GroovyProgram(groovyScript, true);
//                        loadProgram(groovyProgram);
//                    }
//
//                }
//
//                return null;
//            }
//        });

    }

    public void setDeviceAPI(DeviceAPI api) {
        this.deviceAPI = api;
    }

    public void setDeviceSimEntity(DeviceSimEntity simEntity) {
        this.deviceSimEntity = simEntity;
        this.nodeId = this.deviceSimEntity.getDeviceLogic().getID();
    }

    @Override
    public void loadProgram(SensorProgram program) {
        PID++;
        this.programs.put(PID,program);
        program.setPID(PID);
        ((GroovyProgram)program).setProgramStatus(SensorProgram.ProgramStatus.LOADED);
        HashMap<Integer , SensorProgram> sensorIdProgramMap = new HashMap<>();
        sensorIdProgramMap.put(this.nodeId , program);
        EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.PROGRAM_LOADED, sensorIdProgramMap));
    }

    @Override
    public void loadProgram(String code) {
        GroovyProgram groovyProgram = new GroovyProgram(code , false);
        loadProgram(groovyProgram);
    }

    @Override
    public List<SensorProgram> getPrograms() {
         return new ArrayList<>(programs.values());
    }
                                                                                                    
    @Override
    public void execute() {
//        LOG.trace(">> execute");
        for (Map.Entry<Integer, SensorProgram> entry : programs.entrySet()) {
            GroovyProgram groovyProgram = (GroovyProgram) entry.getValue();

            if (groovyProgram.getStatus() != SensorProgram.ProgramStatus.LOADED) {
                continue;
            }

            final Script scriptToRun = groovyShell.parse(groovyProgram.getGroovyScript());


            final GroovyMiddleware me = this;

            new Thread() {
                public void run() {
                    LOG.info("Running program with PID="+PID);
                    me.deviceSimEntity.startProgramExecution(PID);
                    Map<String, Object> params = new HashMap<>();
                    OutputStream os = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(os);
                    params.put("api", deviceAPI);
                    params.put("mCopPlugin" , mCopPlugin);
                    params.put("out", ps);
                    params.put("err", ps);
                    groovyProgram.setOutputStream(os);
                    scriptToRun.setBinding(new Binding(params));
                    try {
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.DURING_EXECUTION);
                        LOG.debug("PID="+PID+" DURING_EXECUTION");
                        Object result = scriptToRun.run();
                        LOG.debug("PID="+PID+" result="+result);
                        groovyProgram.setResult(result);
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.FINISHED_OK);
                        LOG.debug("PID="+PID+" FINISHED_OK");
                    } catch (Exception exc) {
                        LOG.error(exc.getMessage(), exc);
                        ps.print(exc);
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.FINISHED_ERROR);
                        LOG.debug("PID="+PID+" FINISHED_ERROR");
                    } finally {

                        EventBusHolder.getEventBus().post(new DeviceStatisticsEvent(DeviceStatisticsEvent.EventType.PROGRAM_UPDATED, groovyProgram));
                    }
                    me.deviceSimEntity.endProgramExecution(PID);

                }
            }.start();

            if (groovyProgram.resend()) {
                resendProgram(groovyProgram);
            }

//            LOG.trace("<< execute");
        }

        // TODO określenie co jeszcze miałby robić middleware
    }

    private void testProgram (GroovyProgram groovyProgram) {

        final OutputStream os = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(os);

        try {
            final Script scriptToRun = groovyShell.parse(groovyProgram.getGroovyScript());

            new Thread() {
                public void run() {
                    Map<String, Object> params = new HashMap<>();
                    params.put("api", deviceAPI);
                    params.put("mCopPlugin" , mCopPlugin);
                    params.put("out", ps);
                    params.put("err", ps);
                    scriptToRun.setBinding(new Binding(params));
                    try {
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.DURING_EXECUTION);
                        Object result = scriptToRun.run();
                        groovyProgram.setResult(result);
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.FINISHED_OK);
                    } catch (Exception exc) {
                        LOG.error(exc.getMessage(), exc);
                        ps.print(exc);
                        groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.FINISHED_ERROR);
                    } finally {
                        EventBusHolder.getEventBus().post(new InternalEvent(EventType.END_TEST_PROGRAM_EXECUTION, os));
                    }

                }
            }.start();
        } catch (Exception exc) {
            LOG.error(exc.getMessage(), exc);
            ps.print(exc);
            groovyProgram.setProgramStatus(SensorProgram.ProgramStatus.FINISHED_ERROR);
        } finally {
            EventBusHolder.getEventBus().post(new InternalEvent(EventType.END_TEST_PROGRAM_EXECUTION, os));
        }
        

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
                        // FIXME rozsyłanie programu powinno być określane w konsoli
                        GroovyProgram groovyProgram = new GroovyProgram(programToInstall.getValue(), false);
                        loadProgram(groovyProgram);
                    }
                    break;

                case START_TEST_PROGRAM_EXECUTION:
                    LOG.debug(">> START_TEST_PROGRAM_EXECUTION event");
                    programToInstall = (Pair<Integer, String>) event.getPayload();
                    // The program should be installed in current node.
                    if(programToInstall.getKey()!=null && programToInstall.getKey().equals(nodeId)){
                        LOG.debug("Installing program on node: " + nodeId);
                        GroovyProgram groovyProgram = new GroovyProgram(programToInstall.getValue(), true);
                        testProgram(groovyProgram);
                    }
                    break;

                case SIM_START_NODES:
                    new Thread(this).start();
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
        
        Map<Integer , String> commInterfaces =  deviceAPI.api_listCommunicationInterfaces();

        for(Integer commIntId : commInterfaces.keySet()) {
            List<Integer> neighbours = deviceAPI.api_scanForNeighbors(commIntId);
            for (Integer neighbour : neighbours) {
                deviceAPI.api_sendMessage(program.getGroovyScript().hashCode(), deviceAPI.api_getMyID(), neighbour, commIntId, program.getGroovyScript() , null );
            }
        }
        LOG.trace("<< resendProgram");
    }

}
