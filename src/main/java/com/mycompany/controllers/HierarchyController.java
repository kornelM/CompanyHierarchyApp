package com.mycompany.controllers;


import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.company.CooperationService;
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
    private CooperationService cooperationService;
    private CompanyManager companyManager;

    @Autowired
    public HierarchyController(CompanyManager companyManager) {
        this.companyManager = companyManager;
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
                if(type.equals("team")){
                    cooperationService.removeConnection(hd.getTeamName(), companyManager.getAllTeams().get());
                }
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
}
