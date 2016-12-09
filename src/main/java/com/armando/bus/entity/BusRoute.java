package com.armando.bus.entity;

/**
 * Created by armando on 8/12/16.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BusRoute {

    public BusRoute(){}

    public BusRoute(int dep_sid, int arr_sid){
        this.dep_sid = dep_sid;
        this.arr_sid = arr_sid;
    }

    public BusRoute(int dep_sid, int arr_sid, boolean direct_bus_route){
        this.dep_sid = dep_sid;
        this.arr_sid = arr_sid;
        this.direct_bus_route = direct_bus_route;
    }

    public BusRoute(boolean direct_bus_route){
        this.direct_bus_route = direct_bus_route;
    }

    private int dep_sid;
    private int arr_sid;
    private boolean direct_bus_route;

    public int getDep_sid() {
        return dep_sid;
    }

    public void setDep_sid(int dep_sid) {
        this.dep_sid = dep_sid;
    }

    public int getArr_sid() {
        return arr_sid;
    }

    public void setArr_sid(int arr_sid) {
        this.arr_sid = arr_sid;
    }

    public boolean isDirect_bus_route() {
        return direct_bus_route;
    }

    public void setDirect_bus_route(boolean direct_bus_route) {
        this.direct_bus_route = direct_bus_route;
    }

    @Override
    public String toString() {
        return "BusRoute{" +
                "dep_sid=" + dep_sid +
                ", arr_sid=" + arr_sid +
                ", direct_bus_route=" + direct_bus_route +
                '}';
    }
}
