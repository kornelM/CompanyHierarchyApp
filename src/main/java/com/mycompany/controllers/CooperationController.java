package com.mycompany.controllers;


import com.mycompany.hierarchyObjects.Company;
import com.mycompany.hierarchyObjects.Cooperators;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.company.CompanyManager;
import com.mycompany.service.company.CooperationService;
import com.mycompany.service.details.TeamDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/coworker")
public class CooperationController {
    private CompanyManager companyManager;
    private CooperationService cooperationService;

    @Autowired
    public CooperationController(CompanyManager companyManager, CooperationService cooperationService) {
        this.companyManager = companyManager;
        this.cooperationService = cooperationService;
    }



    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<Team>>> setCooperation(@RequestBody Cooperators request) {

        Map<String, List<Team>> teamListMap =
                cooperationService.setCoworkers(request.getParentId(), request.getCoworkersIds(), companyManager.getAllTeams().get());
        if (teamListMap != null) {
            return new ResponseEntity<>(teamListMap, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }


    @RequestMapping(value = "remove/{name}", method = RequestMethod.DELETE)
    public ResponseEntity removeCoworker(@PathVariable("name") String teamName) {
        if (cooperationService.removeConnection(teamName, companyManager.getAllTeams().get())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "get/all", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<Team>>> getAllCoworkers() {
        Map<String, List<Team>> teamCo = cooperationService.getTeamCo();
        if (teamCo.size() != 0) {
            return new ResponseEntity<>(teamCo, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    @RequestMapping(value = "/team/get/coworker/{name}", method = RequestMethod.GET)
    public ResponseEntity getTeamCoworkers(@PathVariable("name") String teamName) {
        Map<String, TeamDetails> allTeamCoworkersInfo;

        Map<String, List<Team>> teamCoworkers = cooperationService.getTeamCo();
        if (teamCoworkers != null) {
            allTeamCoworkersInfo = companyManager.getAllTeamCoworkersInfo(teamCoworkers.get(teamName));
            if (allTeamCoworkersInfo != null) {
                return new ResponseEntity<>(allTeamCoworkersInfo, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
