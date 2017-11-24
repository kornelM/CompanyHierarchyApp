package com.mycompany.controllers;


import com.mycompany.hierarchyObjects.Department;
import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.service.company.CompanyManager;
import com.mycompany.exceptions.TypeNotFoundException;
import com.mycompany.service.interfaces.CompanyObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/company/hierarchy")
public class HierarchyController {

    private CompanyManager companyManager;

    @Autowired
    public HierarchyController(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createTree(@RequestBody List<Department> departments) {
        if (departments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (companyManager.createDepartments(departments)) {
            return new ResponseEntity<>("Hierarchy tree created!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Hierarchy tree not created", HttpStatus.CONFLICT);
        }
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> add(@RequestParam("type") String type,
                                      @RequestBody HierarchyDetails hd) {
        try {
            if (companyManager.add(type, hd)) {
                return new ResponseEntity<>("Object added!", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Object not added!", HttpStatus.CONFLICT);
            }
        } catch (TypeNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public ResponseEntity<String> remove(@RequestParam(value = "type") String type,
                                         @RequestBody HierarchyDetails hd) {
        try {
            if (companyManager.remove(type, hd)) {
                return new ResponseEntity<>("Object removed!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Object not removed!", HttpStatus.CONFLICT);
            }
        } catch (TypeNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<CompanyObject> get(@RequestParam(value = "type") String type,
                                             @RequestParam(value = "department", required = false) String dep,
                                             @RequestParam(value = "location", required = false) String loc,
                                             @RequestParam(value = "team", required = false) String team) {
        HierarchyDetails hd = new HierarchyDetails(dep, loc, team);
        try {
            CompanyObject returnObj = companyManager.getByName(type, hd);
            if (returnObj != null) {
                return new ResponseEntity<>(returnObj, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (TypeNotFoundException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/get/all", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getAll(@RequestParam(value = "type") String type) {

        Optional<List<String>> objList;
        try {
            objList = Optional.of(companyManager.getCompanyObjNames(type));
        } catch (TypeNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(objList.get(), HttpStatus.OK);
    }


//    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
//    public ResponseEntity deleteHierarchy() {
//        companyService.deleteCompanyHierarchy();
//        Team.clearData();
//        cooperationService.clearData();
//
//        return new ResponseEntity(HttpStatus.OK);
//    }


//
//
//    @RequestMapping(value = "get/info/team/{name}", method = RequestMethod.GET)
//    public ResponseEntity<TeamDetails> getTeamInfo(@PathVariable("name") String teamName) {
//        TeamDetails teamInfo = companyService.getTeamInfo(teamName);
//
//        if (teamInfo != null) {
//            return new ResponseEntity<>(teamInfo, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//
//    @RequestMapping(value = "/team/get/all", method = RequestMethod.GET)
//    public ResponseEntity<List<Team>> getAllTeam() {
//        List<Team> teamList = companyService.getAllTeams();
//        if (teamList != null) {
//            return new ResponseEntity<>(teamList, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//    @RequestMapping(value = "/team/add", method = RequestMethod.POST)
//    public ResponseEntity<List<Team>> addTeam(@RequestBody HierarchyDetails hierarchyDetails) {
//        List<Team> teamList = companyService.addTeam(hierarchyDetails.getDepartmentName(), hierarchyDetails.getLocationName(), hierarchyDetails.getTeamName());
//        if (teamList != null) {
//            return new ResponseEntity<>(teamList, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//    @RequestMapping(value = "/team/remove", method = RequestMethod.DELETE)
//    public ResponseEntity<List<Team>> removeTeam(@RequestBody HierarchyDetails hierarchyDetails) {
//        List<Team> listOfTeams = companyService.getAllTeams();
//        List<Team> teamList = companyService.deleteTeam(hierarchyDetails.getDepartmentName(), hierarchyDetails.getLocationName(), hierarchyDetails.getTeamName());
//        if (teamList != null) {
//            if (cooperationService.removeConnection(hierarchyDetails.getTeamName(), listOfTeams)) {
//                return new ResponseEntity<>(teamList, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
//
//
//    @RequestMapping(value = "/location/add", method = RequestMethod.POST)
//    public ResponseEntity<List<Location>> addLocation(@RequestBody HierarchyDetails hierarchyDetails) {
//        List<Location> locations = companyService.addLocation(hierarchyDetails.getDepartmentName(), hierarchyDetails.getLocationName());
//        if (locations != null) {
//            return new ResponseEntity<>(locations, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//    }
//
//    //REMOVE LOCATION BY GIVING ITS NAME AND DEPARTMENT
//    @RequestMapping(value = "/location/remove", method = RequestMethod.DELETE)
//    public ResponseEntity<List<Location>> removeLocation(@RequestBody HierarchyDetails hierarchyDetails) {
//        List<Location> locations = companyService.deleteLocation(hierarchyDetails.getDepartmentName(), hierarchyDetails.getLocationName());
//        if (locations != null) {
//            return new ResponseEntity<>(locations, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    //RETURN ALL DEPARTMENT'S LOCATIONS
//    @RequestMapping(value = "/location/get/all", method = RequestMethod.GET)
//    public ResponseEntity<List<String>> getAllLocations() {
//        List<String> locations = companyService.getLocations();
//        if (locations != null) {
//            return new ResponseEntity<>(locations, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    //RETURN ALL DEPARTMENT'S NAMES
//    @RequestMapping(value = "/department/get/all", method = RequestMethod.GET)
//    public ResponseEntity<List<String>> getAllDepartments() {
//        List<String> departments = companyService.getDepartments();
//        if (departments != null) {
//            return new ResponseEntity<>(departments, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    //ADD DEPARTMENT BY GIVING ITS NAME
//    @RequestMapping(value = "/department/add", method = RequestMethod.POST)
//    public ResponseEntity<List<Department>> addDepartment(@RequestBody HierarchyDetails hierarchyDetails) {
//        List<Department> departments = companyService.addDepartment(hierarchyDetails.getDepartmentName());
//        if (departments != null) {
//            return new ResponseEntity<>(departments, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//    }
//
//    //REMOVE DEPARTMENT BY GIVING ITS NAME
//    //TODO @RequestMapping(value = "/department/remove", method = Request.DELETE) --- should remove deparmtent by its name
//    @RequestMapping(value = "/department/remove", method = RequestMethod.DELETE)
//    public ResponseEntity<List<Department>> removeDepartment(@RequestBody HierarchyDetails hierarchyDetails) {
//        List<Department> departments = companyService.deleteDepartment(hierarchyDetails.getDepartmentName());
//        if (departments != null) {
//            return new ResponseEntity<>(departments, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

}
