package com.mycompany.service.interfaces;

import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.controllers.pojo.TeamDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ICompanyService {

    ResponseEntity buildCompanyHierarchy(List<Department> departmentList);

    ResponseEntity deleteCompanyHierarchy();

    List<Department> addDepartment(String name);

    List<Location> addLocation(String departmentName, String locationName);

    List<Team> addTeam(String departmentName, String locationName, String teamName);

    TeamDetails getTeamInfo(String teamName);

    List<Team> getAllTeams();

    Map<String, TeamDetails> getAllTeamCoworkersInfo(List<Team> teamsCo);

    List<Department> deleteDepartment(String name);

    List<Location> deleteLocation(String departmentName, String locationName);

    List<Team> deleteTeam(String departmentName, String locationName, String teamName);

    List<String> getLocations();

    List<String> getDepartments();
}
