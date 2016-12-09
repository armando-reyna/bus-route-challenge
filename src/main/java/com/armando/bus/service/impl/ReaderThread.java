package com.armando.bus.service.impl;

import com.armando.bus.entity.BusRoute;
import com.armando.bus.entity.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

/**
 * Created by armando on 8/12/16.
 */
public class ReaderThread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ReaderThread.class);

    private final static BlockingQueue<String> linesReadQueue = new ArrayBlockingQueue<String>(30);

    private String filePath;
    private BusRoute busRoute;
    private boolean isConsumer;
    private Process process;
    private BusRoute busRouteResult;

    public static int LINES_NO = 0;
    private int countLines = 0;

    public ReaderThread(String filePath, boolean consumer) {
        this.filePath = filePath;
        this.isConsumer = consumer;
    }

    public ReaderThread(String filePath, boolean consumer, Process process, BusRoute busRoute, BusRoute busRouteResult) {
        this.filePath = filePath;
        this.isConsumer = consumer;
        this.process = process;
        this.busRoute = busRoute;
        this.busRouteResult = busRouteResult;
    }

    private void readFile() {
        Path file = Paths.get(this.filePath);
        try {
            Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8);
            Iterator<String> iterator = lines.iterator();

            String firstline = iterator.next();
            try{
                LINES_NO = Integer.parseInt(firstline);
                LOG.debug("LINES_NO: "+LINES_NO);
                LOG.debug("countLines: "+countLines);
            }catch(NumberFormatException nfex){
                LOG.warn("Line ommited, id is not an integer.");
            }

            while(iterator.hasNext()) {
                String line = iterator.next();
                LOG.debug("read=" + line);
                linesReadQueue.put(line);
                //Adds the file line string to the queue
                LOG.debug(Thread.currentThread().getName() + ":: producer count = "+  linesReadQueue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOG.debug(Thread.currentThread().getName() + " producer is done");
    }

    @Override
    public void run() {
        if (isConsumer) {
            consume();
        } else {
            readFile();
        }
    }

    private void consume() {
        String lineToProcess;
        String []lineSplitted;
        int routeId;
        int stationAId;
        int stationBId;
        try {
            while (!this.process.isCompleted()) {

                lineToProcess = linesReadQueue.take();
                LOG.debug("line to process:" + lineToProcess);

                lineSplitted = lineToProcess.split(" ");
                if(lineSplitted.length >= 3){
                    try{
                        routeId = Integer.parseInt(lineSplitted[0]);
                        for (int indexId = 1; indexId <lineSplitted.length-1; indexId++){
                            stationAId = Integer.parseInt(lineSplitted[indexId]);
                            stationBId = Integer.parseInt(lineSplitted[indexId+1]);
                            LOG.debug("from :" + stationAId);
                            LOG.debug("to :" + stationBId);
                            // evaluate first posible flow
                            if((stationAId == busRoute.getDep_sid() && stationBId == busRoute.getArr_sid())
                                    // evaluate second posible flow
                                    || (stationAId == busRoute.getArr_sid() && stationBId == busRoute.getDep_sid())){
                                LOG.debug("Route founded: " + routeId);
                                this.busRouteResult.setDep_sid(stationAId);
                                this.busRouteResult.setArr_sid(stationBId);
                                this.busRouteResult.setDirect_bus_route(Boolean.TRUE);
                                LOG.debug("Result: " + this.busRouteResult.toString());
                                this.process.setCompleted(Boolean.TRUE);
                                return;
                            }
                        }

                    }catch(NumberFormatException nfex){
                        LOG.warn("Line ommited, id is not an integer.");
                    }
                }else {
                    LOG.warn("Line ommited, doesn't have at least route id, route id and route id");
                }
                countLines++;

                LOG.debug("Line processed");
                LOG.debug("LINES_NO: "+LINES_NO);
                LOG.debug("countLines: "+countLines);

                if(countLines >= LINES_NO){
                    LOG.debug("Set completed process");
                    this.process.setCompleted(Boolean.TRUE);
                }

                LOG.debug("Is process completed: "+this.process.isCompleted());

                LOG.debug(Thread.currentThread().getName() + ":: consumer count:" + linesReadQueue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LOG.debug(Thread.currentThread().getName() + " consumer is done");
    }

}
