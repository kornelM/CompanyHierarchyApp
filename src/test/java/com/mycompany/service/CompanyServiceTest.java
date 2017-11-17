package com.mycompany.service;

import com.mycompany.container.pojos.Department;
import com.mycompany.container.pojos.Location;
import com.mycompany.container.pojos.Team;
import com.mycompany.container.pojos.TeamInfoResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class CompanyServiceTest {

    private CompanyService companyService;
    private CooperationService cooperationService;

    @Before
    public void setUp() {
        companyService = new CompanyService();
        cooperationService = new CooperationService();
    }

    @Test
    public void testBuildCompanyHierarchy() {
        //GIVEN
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Marketing", Collections.singletonList(new Location("Krakow",
                Arrays.asList(new Team("Siths"), new Team("Jedi"), new Team("Wookiees"))))));
        List<String> expectedDepartment =Collections.singletonList("Marketing");
        //WHEN
        companyService.buildCompanyHierarchy(departments);

        //THEN
        Assert.assertTrue(companyService.getDepartments() != null);
        Assert.assertTrue(companyService.getDepartments().size() > 0);
        Assert.assertEquals(expectedDepartment, companyService.getDepartments());
    }

    @Test
    public void testGetAllTeamCoworkersInfo() {
        //GIVEN
        String teamName = "Team1";
        int parentId = 1;
        Integer coworkers[] = {4, 7};
        companyService.buildCompanyHierarchy(initHierarchy());
        cooperationService.setCoworkers(parentId, coworkers, companyService.getAllTeams());
        Map<String, TeamInfoResponse> expectedMap = new HashMap<>();
        expectedMap.put("Team4", new TeamInfoResponse("IT", "Szczecin"));
        expectedMap.put("Team7", new TeamInfoResponse("HR", "Krakow"));

        //WHEN
        Map<String, TeamInfoResponse> returnedMap = companyService.getAllTeamCoworkersInfo(cooperationService.getTeamCo().get(teamName));

        //THEN
        Assert.assertEquals(expectedMap, returnedMap);
    }

    @Test
    public void testGetAllTeamCoworkersInfoWhenThereIsNoCoworkers() {
        //GIVEN
        String teamName = "Team1";
        companyService.buildCompanyHierarchy(initHierarchy());

        //WHEN
        Map<String, TeamInfoResponse> returnedMap = companyService.getAllTeamCoworkersInfo(cooperationService.getTeamCo().get(teamName));

        //THEN
        Assert.assertEquals(null, returnedMap);
    }

    @Test
    public void testGetTeamInfo() {
        //GIVEN
        String teamName = "Team5";
        TeamInfoResponse expectedInfo = new TeamInfoResponse("IT", "Szczecin");

        companyService.buildCompanyHierarchy(initHierarchy());

        //WHEN
        TeamInfoResponse returnedInfo = companyService.getTeamInfo(teamName);

        //THEN
        Assert.assertEquals(expectedInfo, returnedInfo);
    }

    @Test
    public void testGetAllTeams() throws Exception {
        //GIVEN
        List<Team> expectedTeamList = Arrays.asList(new Team(0, "Team0"), new Team(1, "Team1"), new Team(2, "Team2"),
                new Team(3, "Team3"), new Team(4, "Team4"), new Team(5, "Team5"), new Team(6, "Team6"), new Team(7, "Team7"));
        companyService.buildCompanyHierarchy(initHierarchy());

        //WHEN
        List<Team> returnedTeams = companyService.getAllTeams();

        //THEN
        Assert.assertEquals(expectedTeamList, returnedTeams);
    }

    @Test
    public void testDeleteCompanyHierarchy() throws Exception {
        //GIVEN
        companyService.buildCompanyHierarchy(initHierarchy());
        List<String> expectedDepartment = new ArrayList<>(); //Arrays.asList("Marketing", "IT", "HR");

        //WHEN
        companyService.deleteCompanyHierarchy();

        //THEN
        List<String> departments = companyService.getDepartments();
        Assert.assertEquals(expectedDepartment, departments);
    }

    @Test
    public void testAddDepartment() throws Exception {
        //GIVEN
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("D0", new ArrayList<>()));
        departments.add(new Department("D1", new ArrayList<>()));
        departments.add(new Department("D2", new ArrayList<>()));
        companyService.buildCompanyHierarchy(departments);
        List<Department> expectedDepartmentsList = Arrays.asList(new Department("D0", new ArrayList<>()),
                new Department("D1", new ArrayList<>()), new Department("D2", new ArrayList<>()), new Department("D3", new ArrayList<>()));
        String newDepartmentName = "D3";

        //WHEN
        List<Department> returnedDepartments = companyService.addDepartment(newDepartmentName);


        //THEN
        Assert.assertEquals(expectedDepartmentsList, returnedDepartments);
    }

    @Test
    public void testAddLocation() throws Exception {
        //GIVEN
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("L0", new ArrayList<>()));
        locations.add(new Location("L1", new ArrayList<>()));

        List<Department> departments = new ArrayList<>();
        departments.add(new Department("D0", locations));

        List<Location> expectedLocations = Arrays.asList(new Location("L0", new ArrayList<>()), new Location("L1", new ArrayList<>()),
                new Location("L2", new ArrayList<>()));

        companyService.buildCompanyHierarchy(departments);

        String newLocationName = "L2";
        String departmentName = "D0";

        //WHEN
        List<Location> returnedLocations = companyService.addLocation(departmentName, newLocationName);

        //THEN
        Assert.assertEquals(expectedLocations, returnedLocations);
    }

    @Test
    public void testAddTeam() throws Exception {
        //GIVEN
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(0, "Team0"));
        teams.add(new Team(1, "Team1"));

        List<Location> locations = new ArrayList<>();
        locations.add(new Location("L0", teams));

        List<Department> departments = new ArrayList<>();
        departments.add(new Department("D0", locations));

        List<Team> expectedTeaList = Arrays.asList(new Team(0, "Team0"),
                new Team(1, "Team1"), new Team(5, "Team2")); //there will be created 5 teams before added team

        companyService.buildCompanyHierarchy(departments);

        String teamName = "Team2";
        String locationName = "L0";
        String departmentName = "D0";

        //WHEN
        List<Team> returnedTeams = companyService.addTeam(departmentName, locationName, teamName);

        //THEN
        Assert.assertEquals(expectedTeaList, returnedTeams);
    }

    @Test
    public void testDeleteDepartment() {
        //GIVEN
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("D0", new ArrayList<>()));
        departments.add(new Department("D1", new ArrayList<>()));
        departments.add(new Department("D2", new ArrayList<>()));
        companyService.buildCompanyHierarchy(departments);
        List<Department> expectedDepartmentsList = Arrays.asList(new Department("D0", new ArrayList<>()),
                new Department("D1", new ArrayList<>()));
        String departmentToDelete = "D2";

        //WHEN
        List<Department> returnedDepartments = companyService.deleteDepartment(departmentToDelete);

        //THEN
        Assert.assertEquals(expectedDepartmentsList, returnedDepartments);
    }


    @Test
    public void testDeleteLocation() {
        //GIVEN
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("L0", new ArrayList<>()));
        locations.add(new Location("L1", new ArrayList<>()));

        List<Department> departments = new ArrayList<>();
        departments.add(new Department("D0", locations));

        List<Location> expectedLocations = Collections.singletonList(new Location("L0", new ArrayList<>()));

        companyService.buildCompanyHierarchy(departments);

        String locationToDelete = "L1";
        String departmentName = "D0";

        //WHEN
        List<Location> returnedLocations = companyService.deleteLocation(departmentName, locationToDelete);

        //THEN
        Assert.assertEquals(expectedLocations, returnedLocations);
    }


    @Test
    public void testDeleteTeam() throws Exception {
        //GIVEN
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(0, "Team0"));
        teams.add(new Team(1, "Team1"));
        teams.add(new Team(2, "Team2"));

        List<Location> locations = new ArrayList<>();
        locations.add(new Location("L0", teams));

        List<Department> departments = new ArrayList<>();
        departments.add(new Department("D0", locations));

        List<Team> expectedTeaList = Arrays.asList(new Team(0, "Team0"),
                new Team(1, "Team1")); //there will be created 5 teams before added team

        companyService.buildCompanyHierarchy(departments);

        String teamName = "Team2";
        String locationName = "L0";
        String departmentName = "D0";

        //WHEN
        List<Team> returnedTeams = companyService.deleteTeam(departmentName, locationName, teamName);

        //THEN
        Assert.assertEquals(expectedTeaList, returnedTeams);
    }







    private List<Department> initHierarchy() {
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Marketing", Collections.singletonList(new Location("Krakow",
                Arrays.asList(new Team(0, "Team0"), new Team(1, "Team1"), new Team(2, "Team2"))))));

        departments.add(new Department("IT", Collections.singletonList(
                new Location("Szczecin", Arrays.asList(new Team(3, "Team3"), new Team(4, "Team4"), new Team(5, "Team5"))))));

        departments.add(new Department("HR", Collections.singletonList(
                new Location("Krakow", Arrays.asList(new Team(6, "Team6"), new Team(7, "Team7"))))));


        return departments;
    }

}