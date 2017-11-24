package com.mycompany.service.company;

import com.mycompany.service.*;
import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.service.details.TeamDetails;
import com.mycompany.service.validation.TypeValidator;
import com.mycompany.exceptions.TypeNotFoundException;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.interfaces.CompanyObject;
import com.mycompany.service.interfaces.ObjectManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class CompanyManager {
    private List<Department> departments;

    private TypeValidator typeValidator;
    private Map<String, ObjectManager> managerMap;
    private final Logger logger = Logger.getLogger(CompanyManager.class);


    public CompanyManager() {
        initMap();
        this.typeValidator = new TypeValidator();
    }

    public boolean createDepartments(List<Department> departmentList) {
        if (departments == null && departmentList.size() != 0) {
            this.departments = departmentList;
            return true;
        } else {
            logger.warn("Tree already exists!");
            return false;
        }
    }

    public Optional<List<Team>> getAllTeams() {
        List<Team> teamsToReturn = new ArrayList<>();
        for (Department d : departments) {
            for (Location l : d.getLocations()) {
                teamsToReturn.addAll(l.getTeams());
            }
        }
        return Optional.of(teamsToReturn);
    }

    public List<String> getCompanyObjNames(String type) throws TypeNotFoundException {
        List<String> companyObjects = new ArrayList<>();

        if (type.equals("department")) {
            companyObjects.addAll(departments.stream()
                    .map(Department::getName).collect(Collectors.toList()));
        } else if (type.equals("location")) {
            companyObjects.addAll(departments.stream()
                    .map(Department::getLocations)
                    .flatMap(List::stream)
                    .map(Location::getName)
                    .collect(Collectors.toList()));

        } else if (type.equals("team")) {
            companyObjects.addAll(departments.stream()
                    .map(Department::getLocations)
                    .flatMap(List::stream)
                    .map(Location::getTeams)
                    .flatMap(List::stream)
                    .map(Team::getName)
                    .collect(Collectors.toList()));


        } else {
            throw new TypeNotFoundException(type);
        }
        return companyObjects;
    }


    public TeamDetails getTeamInfo(String teamName) {
        for (Department d : departments) {
            for (Location l : d.getLocations()) {
                for (Team t : l.getTeams()) {
                    if (t.getName().equals(teamName)) {
                        return new TeamDetails(d.getName(), l.getName());
                    }
                }
            }
        }
        return null;
    }


    public Map<String, TeamDetails> getAllTeamCoworkersInfo(List<Team> teamsCo) {
        if (teamsCo != null) {
            return teamsCo.stream()
                    .collect(Collectors.toMap(Team::getName, t -> getTeamInfo(t.getName())));
        }
        return null;
    }


    public boolean add(String type, HierarchyDetails hd) throws TypeNotFoundException {
        if (typeValidator.checkType(type)) {
            if (!managerMap.get(type.toLowerCase()).add(departments, hd)) {
                logger.error("Object " + hd + " not added");
                return false;
            } else {
                return true;
            }
        }
        throw new TypeNotFoundException("Type " + type + " not found");
    }

    public boolean remove(String type, HierarchyDetails hd) throws TypeNotFoundException {
        if (typeValidator.checkType(type)) {
            if (!managerMap.get(type.toLowerCase()).remove(departments, hd)) {
                logger.error("Object " + hd + " not removed");
                return false;
            } else {
                return true;
            }
        }
        throw new TypeNotFoundException("Type " + type + " not found");
    }


    public CompanyObject getByName(String type, HierarchyDetails hd) throws TypeNotFoundException {
        if (typeValidator.checkType(type)) {
            return managerMap.get(type.toLowerCase()).get(departments, hd);
        } else {
            throw new TypeNotFoundException("Type " + type + " not found");
        }
    }


//    public Optional getAll(String type, HierarchyDetails hd) throws TypeNotFoundException {
//        if (typeValidator.checkType(type)) {
//            return managerMap.get(type.toLowerCase()).getAll(departments, hd);
//        }
//        throw new TypeNotFoundException("Type " + type + " not found");
//    }


    private void initMap() {
        managerMap = new HashMap<>();
        managerMap.put("team", new TeamManager());
        managerMap.put("location", new LocationManager());
        managerMap.put("department", new DepartmentManager());
    }


}
