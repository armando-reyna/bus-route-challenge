package com.armando.bus.service;

import com.armando.bus.entity.BusRoute;

/**
 * Created by armando on 8/12/16.
 */
public interface BusRouteService {

    BusRoute findRoute(String filePath, int dep_sid, int arr_sid);

}
