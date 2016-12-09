package com.armando.bus.service.impl;

import com.armando.bus.entity.BusRoute;
import com.armando.bus.entity.Process;
import com.armando.bus.service.BusRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by armando on 8/12/16.
 */
@Service
public class BusRouteServiceImpl implements BusRouteService {

    private static final Logger LOG = LoggerFactory.getLogger(BusRouteServiceImpl.class);

    public BusRoute findRoute(String filePath, int dep_sid, int arr_sid) {

        BusRoute busRouteResult = new BusRoute(Boolean.FALSE);

        if(dep_sid == arr_sid){
            LOG.debug("Same origin and destination, ignoring process.");
            return busRouteResult;
        }else {
            LOG.debug("File: " + filePath);
            LOG.debug("Finding route, dep_sid: " + dep_sid + ", arr_sid: " + arr_sid);

            BusRoute busRoute = new BusRoute(dep_sid, arr_sid);
            Process process = new Process();

            long startTime = System.nanoTime();

            ExecutorService producerPool = Executors.newFixedThreadPool(1);
            producerPool.submit(new ReaderThread(filePath, Boolean.FALSE));

            // create a pool of consumer threads to parse the lines read
            ExecutorService consumerPool = Executors.newFixedThreadPool(1);
            consumerPool.submit(new ReaderThread(filePath, Boolean.TRUE, process, busRoute, busRouteResult));

            while (!process.isCompleted()) {
                LOG.debug("Not done yet");
            }

            LOG.debug("Process Completed!");

            producerPool.shutdown();
            consumerPool.shutdown();

            long endTime = System.nanoTime();
            long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
            LOG.debug("Total elapsed time: " + elapsedTimeInMillis + " ms");

            return busRouteResult;
        }

    }


}
