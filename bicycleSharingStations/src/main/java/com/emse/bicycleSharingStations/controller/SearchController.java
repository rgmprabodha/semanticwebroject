package com.emse.bicycleSharingStations.controller;

import com.emse.bicycleSharingStations.model.BicycleStation;
import com.emse.bicycleSharingStations.service.BicycleStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ComponentScan(value = "com.emse.bicycleSharingStations")
@Controller
public class SearchController {
    @Autowired
    BicycleStationService bicycleStationService;


    @RequestMapping("/api/search/{cname}")
    public ResponseEntity<List<BicycleStation>> findStationsByCity(@PathVariable String cname) {
        System.out.println("cname::" + cname);
        List<BicycleStation> bicycleStations = bicycleStationService.findStationsByCity(cname);
        return new ResponseEntity<List<BicycleStation>>(bicycleStations, HttpStatus.OK);
    }
}
