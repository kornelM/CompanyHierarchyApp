package com.mycompany.controllers;


import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.company.CompanyManager;
import com.mycompany.service.company.CooperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {


    private CompanyManager companyManager;
    private CooperationService cooperationService;

    @Autowired
    public CompanyController(CompanyManager companyManager, CooperationService cooperationService) {
        this.companyManager = companyManager;
        this.cooperationService = cooperationService;
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

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    public ResponseEntity deleteHierarchy() {
        if (companyManager.deleteCompanyHierarchy()) {
            Team.clearData();
            cooperationService.clearData();
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }


}
