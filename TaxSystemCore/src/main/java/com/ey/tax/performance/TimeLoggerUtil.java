package com.ey.tax.performance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by zhuji on 2/6/2018.
 */
public class TimeLoggerUtil {
    private static final Logger logger = LogManager.getLogger("performance");

    private ThreadLocal<Deque<Split>> stopWatchStack = new ThreadLocal<Deque<Split>>(){
        @Override
        protected Deque<Split> initialValue() {
            return new LinkedList<>();
        }
    };
    private TimeLoggerUtil(){}

    private static class TimeLoggerUtilHolder{
        private static TimeLoggerUtil INSTANCE = new TimeLoggerUtil();
    }

    public static TimeLoggerUtil getInstance(){
        return TimeLoggerUtilHolder.INSTANCE;
    }

    public void start(String stopwatchName){
        if(logger.isDebugEnabled()){
            stopwatchName = Thread.currentThread().getId()+"_"+stopwatchName;
            Stopwatch stopwatch = SimonManager.getStopwatch(stopwatchName);
            Split split = stopwatch.start();
            stopWatchStack.get().addLast(split);
        }
    }

    public void stop(){
        if(logger.isDebugEnabled()){
            Split split = stopWatchStack.get().pollLast();
            split.stop();
            logger.debug(split.getStopwatch().toString());
        }
    }
}
