package com.mycompany.service;

import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.controllers.pojo.TeamDetails;
import com.mycompany.service.interfaces.ICompanyService;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class CompanyService implements ICompanyService {
    private List<Department> departments;
    private final Logger logger = Logger.getLogger(CompanyService.class);


    @Override
    public ResponseEntity buildCompanyHierarchy(List<Department> departmentList) {
        if (departments == null) {
            departments = departmentList;
            logger.info("Tree created!");
            return new ResponseEntity<>("Departments list created!", HttpStatus.CREATED);
        } else {
            logger.warn("Tree already exists!");
            return new ResponseEntity<>("Departments list already exists!", HttpStatus.CONFLICT);
        }
    }

    @Override
    public Map<String, TeamDetails> getAllTeamCoworkersInfo(List<Team> teamsCo) {
        if (teamsCo != null) {
            return teamsCo.stream()
                    .collect(Collectors.toMap(Team::getName, t -> getTeamInfo(t.getName())));
        }
        logger.warn("Team does not have coworkers!");
        return null;
    }

    @Override
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
        logger.error("Team with name: " + teamName + "does not exists!");
        return null;
        // 3x for loop don't look good I know,
        // I don't have a clue how to change it for now ( O(n3))
    }

    @Override
    public List<Team> getAllTeams() {
        List<Team> teamsToReturn = new ArrayList<>();

        for (Department d : departments) {
            for (Location l : d.getLocations()) {
                teamsToReturn.addAll(l.getTeams());
            }
        }
        return teamsToReturn;
    }

    @Override
    public ResponseEntity deleteCompanyHierarchy() {
        if (departments != null) {
            departments = null;
            logger.info("Tree removed.");
            return new ResponseEntity<>("Tree removed!", HttpStatus.OK);
        } else {
            logger.error("Cannot remove tree which does not exists!");
            return new ResponseEntity<>("Tree does not exists!", HttpStatus.CONFLICT);
        }
    }

    @Override
    public List<Department> addDepartment(String name) {
        if (departments.add(new Department(name, new ArrayList<>()))) {
            return departments;
        } else {
            logger.error("Department not added!");
            return null;
        }
    }

    @Override
    public List<Location> addLocation(String departmentName, String locationName) {
        for (Department d : departments) {
            if (d.getName().equals(departmentName)) {
                if (d.getLocations().add(new Location(locationName, new ArrayList<>()))) {
                    logger.info("Location: " + locationName + " added.");
                    return d.getLocations();
                }
            }
        }
        logger.error("Location: " + locationName + " not added!");
        return null;
    }

    @Override
    public List<Team> addTeam(String departmentName, String locationName, String teamName) {
        for (Department d : departments) {
            for (Location l : d.getLocations()) {
                boolean exists = l.getTeams().stream()
                        .anyMatch(team -> team.getName().equals(teamName));
                if (!exists) {
                    if (l.getTeams().add(new Team(teamName))) {
                        logger.info("Team: " + teamName + " added.");
                        return l.getTeams();
                    }
                }
            }
        }
        logger.error("Team: " + teamName + " already exists!");
        return null;
    }


    @Override
    public List<String> getDepartments() {
        if(departments != null){
            return departments.stream()
                    .map(Department::getName)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    @Override
    public List<String> getLocations() {
        List<String> locationNames = new ArrayList<>();
        for (Department d : departments) {
            locationNames.addAll(d.getLocations().stream()
                    .map(Location::getName)
                    .collect(Collectors.toList()));
        }
        return locationNames;
    }


    @Override
    public List<Department> deleteDepartment(String name) {
        if (departments != null && departments.size() > 0) {
            for (int i = 0; i < departments.size(); i++) {
                if (departments.get(i).getName().equals(name)) {
                    departments.remove(i);
                    logger.info("Department: " + name + " removed.");
                    return departments;
                }
            }
        }
        logger.error("Department: " + name + " not removed!");
        return null;
    }


    @Override
    public List<Location> deleteLocation(String departmentName, String locationName) {
        if (!departments.isEmpty()) {
            for (Department department : departments) {
                if (department.getName().equals(departmentName)) {
                    List<Location> locations = department.getLocations();
                    if (!locations.isEmpty()) {
                        for (int j = 0; j < locations.size(); j++) {
                            if (locations.get(j).getName().equals(locationName)) {
                                locations.remove(j);
                                return locations;
                            }
                            logger.error("Location: " + locationName + " not found!");
                        }
                    }
                }
                logger.error("Department: " + departmentName + " not found!");
            }
        }
        logger.error("Department list is empty!");
        return null;
    }

    @Override
    public List<Team> deleteTeam(String departmentName, String locationName, String teamName) {
        if (!departments.isEmpty()) {
            for (Department department : departments) {
                if (department.getName().equals(departmentName)) {
                    List<Location> locations = department.getLocations();
                    if (!locations.isEmpty()) {
                        for (Location l : locations) {
                            if (l.getName().equals(locationName))
                                if (!l.getTeams().isEmpty()) {
                                    List<Team> teams = l.getTeams();
                                    for (int k = 0; k < teams.size(); k++) {
                                        if (teams.get(k).getName().equals(teamName)) {
                                            teams.remove(k);
                                            return teams;
                                        }
                                        logger.error("Team: " + teamName + " not found!");
                                    }
                                }
                        }
                        logger.error("Location:" + locationName + " not found!");
                    }
                }
                logger.error("Department: " + departmentName + " not found!");
            }
        }
        logger.error("Department list is empty!");
        return null; //another nested for loop monster  :/
    }
}

