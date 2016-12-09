package com.armando.bus.control;

import com.armando.bus.entity.BusRoute;
import com.armando.bus.service.BusRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

@RestController
@ComponentScan(basePackages = { "com.armando.bus.service"})
@RequestMapping(value = "/api")
public class APIController {

    @Autowired
    BusRouteService busRouteService;

    @Value("${filePath}")
    private String filePath;

    /**
     * Returns a the ids of bus routes if there is any direct connection.
     *
     * @param  dep_sid  an absolute URL giving the base location of the image
     * @param  arr_sid the location of the image, relative to the url argument
     * @return a JSON object, direct_bus_route is true if there exists a bus route
     * in the input data that connects the stations represented by dep_sid and arr_sid
     * otherwise direct_bus_route must be set to false
     */
    @RequestMapping(value = "/direct", method = RequestMethod.GET)
    public BusRoute direct(@RequestParam(value="dep_sid") int dep_sid, @RequestParam(value="arr_sid") int arr_sid) {
        return busRouteService.findRoute(filePath, dep_sid, arr_sid);
    }

}
