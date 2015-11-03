package org.mdyk.sensesim.simulation.engine.dissim.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SaveStatisticsInterceptor {


    @Pointcut("execution(* *(..)) && @annotation(org.mdyk.netsim.logic.aop.statistics.SaveStatistics)")
    public void saveStatistics() {}

    @Around("saveStatistics()")
    public Object saveStatistics(ProceedingJoinPoint joinPoint) throws Throwable  {
        Object obj = joinPoint.proceed();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return obj;
    }

}
