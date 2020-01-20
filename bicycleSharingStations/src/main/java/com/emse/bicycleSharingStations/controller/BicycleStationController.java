package com.emse.bicycleSharingStations.controller;

import com.emse.bicycleSharingStations.service.BicycleStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@ComponentScan(value = "com.emse.bicycleSharingStations")
@Controller
public class BicycleStationController {

    @Autowired
    BicycleStationService bicycleStationService;

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        return "bicycle";
    }

    @RequestMapping("/extractStatic")
    public String extractStatic(Map<String, Object> model) {
        return "extractStatic";
    }

    @RequestMapping("/bicycleSearch")
    public String bicycleSearch(Map<String, Object> model) {
        return "bicycleSearchOptions";
    }
}