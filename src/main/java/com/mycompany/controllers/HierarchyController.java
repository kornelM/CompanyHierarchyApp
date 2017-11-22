package com.mycompany.controllers;


import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.controllers.pojo.TeamDetails;
import com.mycompany.controllers.pojo.Cooperators;
import com.mycompany.controllers.pojo.HierarchyDetails;
import com.mycompany.service.CompanyService;
import com.mycompany.service.CooperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/company/hierarchy")
public class HierarchyController {

    private CooperationService cooperationService;
    private CompanyService companyService;

    @Autowired
    public HierarchyController(CooperationService cooperationService, CompanyService companyService) {
        this.cooperationService = cooperationService;
        this.companyService = companyService;
    }



    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createTree(@RequestBody List<Department> departments) {
        if (departments.isEmpty()) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        return companyService.buildCompanyHierarchy(departments);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity deleteHierarchy() {
        companyService.deleteCompanyHierarchy();
        Team.clearData();
        cooperationService.clearData();

        return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping(value = "/coworker/add", method = RequestMethod.POST)
    public ResponseEntity<Map<String, List<Team>>> setCooperation(@RequestBody Cooperators request) {

        Map<String, List<Team>> teamListMap = cooperationService.setCoworkers(request.getParentId(), request.getCoworkersIds(), companyService.getAllTeams());
        if (teamListMap != null) {
            return new ResponseEntity<>(teamListMap, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }


    @RequestMapping(value = "/coworker/remove/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<List<Department>> removeCoworker(@PathVariable("name") String teamName) {
        boolean removed = cooperationService.removeConnection(teamName, companyService.getAllTeams());

        if (removed) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/coworker/get/all", method = RequestMethod.GET)
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
            allTeamCoworkersInfo = companyService.getAllTeamCoworkersInfo(teamCoworkers.get(teamName));
            if (allTeamCoworkersInfo != null) {
                return new ResponseEntity<>(allTeamCoworkersInfo, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "get/info/team/{name}", method = RequestMethod.GET)
    public ResponseEntity<TeamDetails> getTeamInfo(@PathVariable("name") String teamName) {
        TeamDetails teamInfo = companyService.getTeamInfo(teamName);

        if (teamInfo != null) {
            return new ResponseEntity<>(teamInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/team/get/all", method = RequestMethod.GET)
    public ResponseEntity<List<Team>> getAllTeam() {
        List<Team> teamList = companyService.getAllTeams();
        if (teamList != null) {
            return new ResponseEntity<>(teamList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/team/add", method = RequestMethod.POST)
    public ResponseEntity<List<Team>> addTeam(@RequestBody HierarchyDetails hierarchyDetails) {
        List<Team> teamList = companyService.addTeam(hierarchyDetails.getDepartment(), hierarchyDetails.getLocation(), hierarchyDetails.getTeam());
        if (teamList != null) {
            return new ResponseEntity<>(teamList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/team/remove", method = RequestMethod.DELETE)
    public ResponseEntity<List<Team>> removeTeam(@RequestBody HierarchyDetails hierarchyDetails) {
        List<Team> listOfTeams = companyService.getAllTeams();
        List<Team> teamList = companyService.deleteTeam(hierarchyDetails.getDepartment(), hierarchyDetails.getLocation(), hierarchyDetails.getTeam());
        if (teamList != null) {
            if (cooperationService.removeConnection(hierarchyDetails.getTeam(), listOfTeams)) {
                return new ResponseEntity<>(teamList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/location/add", method = RequestMethod.POST)
    public ResponseEntity<List<Location>> addLocation(@RequestBody HierarchyDetails hierarchyDetails) {
        List<Location> locations = companyService.addLocation(hierarchyDetails.getDepartment(), hierarchyDetails.getLocation());
        if (locations != null) {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    //REMOVE LOCATION BY GIVING ITS NAME AND DEPARTMENT
    @RequestMapping(value = "/location/remove", method = RequestMethod.DELETE)
    public ResponseEntity<List<Location>> removeLocation(@RequestBody HierarchyDetails hierarchyDetails) {
        List<Location> locations = companyService.deleteLocation(hierarchyDetails.getDepartment(), hierarchyDetails.getLocation());
        if (locations != null) {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //RETURN ALL DEPARTMENT'S LOCATIONS
    @RequestMapping(value = "/location/get/all", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllLocations() {
        List<String> locations = companyService.getLocations();
        if (locations != null) {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //RETURN ALL DEPARTMENT'S NAMES
    @RequestMapping(value = "/department/get/all", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = companyService.getDepartments();
        if (departments != null) {
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //ADD DEPARTMENT BY GIVING ITS NAME
    @RequestMapping(value = "/department/add", method = RequestMethod.POST)
    public ResponseEntity<List<Department>> addDepartment(@RequestBody HierarchyDetails hierarchyDetails) {
        List<Department> departments = companyService.addDepartment(hierarchyDetails.getDepartment());
        if (departments != null) {
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    //REMOVE DEPARTMENT BY GIVING ITS NAME
    //TODO @RequestMapping(value = "/department/remove", method = Request.DELETE) --- should remove deparmtent by its name
    @RequestMapping(value = "/department/remove", method = RequestMethod.DELETE)
    public ResponseEntity<List<Department>> removeDepartment(@RequestBody HierarchyDetails hierarchyDetails) {
        List<Department> departments = companyService.deleteDepartment(hierarchyDetails.getDepartment());
        if (departments != null) {
            return new ResponseEntity<>(departments, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
