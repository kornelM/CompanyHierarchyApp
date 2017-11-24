package com.mycompany.service.unit;

import com.mycompany.service.details.TeamDetails;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.company.CompanyManager;
import com.mycompany.service.company.CooperationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


public class CooperationServiceTest {
    private CooperationService cooperationService;
    private CompanyManager companyManager;

    @Before
    public void setUp() {
        this.companyManager = new CompanyManager();
        this.cooperationService = new CooperationService();
    }


    @Test
    public void testSetCoworkers() throws Exception {
        //GIVEN
        int idOfTeam = 3;
        Integer[] coworkersIds = {5, 2};
        List<Team> teamList = Arrays.asList(new Team(1, "Team1"),
                new Team(2, "Team2"), new Team(3, "Team3"),
                new Team(4, "Team4"), new Team(5, "Team5"),
                new Team(6, "Team6"), new Team(7, "Team7"));
        Map<String, List<Team>> expectedMapOfConnectedTeams = new HashMap<>();
        expectedMapOfConnectedTeams.put("Team5", (Arrays.asList(new Team(2, "Team2"), new Team(3, "Team3"))));
        expectedMapOfConnectedTeams.put("Team2", (Arrays.asList(new Team(5, "Team5"), new Team(3, "Team3"))));
        expectedMapOfConnectedTeams.put("Team3", (Arrays.asList(new Team(5, "Team5"), new Team(2, "Team2"))));


        //WHEN
        Map<String, List<Team>> returnedMapOfConnectedTeams
                = cooperationService.setCoworkers(idOfTeam, coworkersIds, teamList);


        //THEN
        Assert.assertEquals(expectedMapOfConnectedTeams, returnedMapOfConnectedTeams);
    }


    @Test
    public void testGetTeamFromList() throws Exception {
        //GIVEN
        List<Team> teamList = Arrays.asList(new Team(1, "Team1"),
                new Team(2, "Team2"), new Team(3, "Team3"),
                new Team(4, "Team4"), new Team(5, "Team5"));
        int idOfTeamToReturn = 2;
        Team expectedTeam = new Team(2, "Team2");
        Team returnedTeam;


        //WHEN
        returnedTeam = cooperationService.getTeamFromList(idOfTeamToReturn, teamList);


        //THEN
        Assert.assertEquals(expectedTeam, returnedTeam);
    }

    @Test
    public void testGetTeamByName() {
        //GIVEN
        List<Team> teamList = Arrays.asList(new Team(1, "Team1"),
                new Team(2, "Team2"), new Team(3, "Team3"),
                new Team(4, "Team4"), new Team(5, "Team5"));
        String teamName = "Team2";

        Team expectedTeam = new Team(2, "Team2");


        //WHEN
        Team returnedTeam = cooperationService.getTeamByName(teamName, teamList);


        //THEN
        Assert.assertEquals(expectedTeam, returnedTeam);


    }


    @Test
    public void testCheckTeamList() {
        //GIVEN
        List<Team> expectedTeamList = new ArrayList<>();
        expectedTeamList.addAll(Arrays.asList(new Team(1, "Team1"),
                new Team(2, "Team2"), new Team(3, "Team3"),
                new Team(4, "Team4")));

        List<Team> listOfAvailableTeam = new ArrayList<>();
        listOfAvailableTeam.addAll(Arrays.asList(new Team(1, "Team1"),
                new Team(2, "Team2"), new Team(3, "Team3")));

        List<Team> teamsToAdd = new ArrayList<>();
        teamsToAdd.addAll(Arrays.asList(new Team(1, "Team1"),
                new Team(2, "Team2"), new Team(4, "Team4")));


        //WHEN
        List<Team> returnedTeams = cooperationService.checkTeamList(teamsToAdd, listOfAvailableTeam);


        //THEN
        Assert.assertEquals(expectedTeamList, returnedTeams);
    }


    @Test
    public void testRemoveConnection() {
        //GIVEN
        String teamName = "Team2";
        Integer coworkers[] = {2, 3};
        List<Team> teams = Arrays.asList(new Team(1, "Team1"), new Team(2, "Team2"), new Team(3, "Team3"));

        cooperationService.setCoworkers(1, coworkers, teams);

        //WHEN
        boolean result = cooperationService.removeConnection(teamName, teams);

        //THEN
        Assert.assertTrue(result);
    }


    @Test
    public void testGetAllTeamCoworkersInfo() {
        //GIVEN
        String teamName = "Team2";
        int parentId = 4;
        Integer coworkers[] = {2, 8, 10, 15, 18};
        companyManager.createDepartments(initHierarchy());
        cooperationService.setCoworkers(parentId, coworkers, companyManager.getAllTeams().get());

        Map<String, TeamDetails> expectedMap = new HashMap<>();
        expectedMap.put("Team4", new TeamDetails("Marketing", "Krakow"));
        expectedMap.put("Team8", new TeamDetails("IT", "Szczecin"));
        expectedMap.put("Team10", new TeamDetails("IT", "Warszawa"));
        expectedMap.put("Team15", new TeamDetails("HR", "Krakow"));
        expectedMap.put("Team18", new TeamDetails("HR", "Poznan"));

        //WHEN
        Map<String, TeamDetails> returnedMap
                = companyManager.getAllTeamCoworkersInfo(cooperationService.getTeamCo().get(teamName));


        //THEN
        Assert.assertEquals(expectedMap, returnedMap);
    }

    @Test
    public void testGetTeamInfo() {
        //GIVEN
        companyManager.createDepartments(initHierarchy());
        TeamDetails expected1 = new TeamDetails("Marketing", "Krakow");
        TeamDetails expected2 = new TeamDetails("IT", "Warszawa");
        TeamDetails expected3 = new TeamDetails("HR", "Poznan");


        //WHEN
        TeamDetails returnedInfo1 = companyManager.getTeamInfo("Team0");
        TeamDetails returnedInfo2 = companyManager.getTeamInfo("Team10");
        TeamDetails returnedInfo3 = companyManager.getTeamInfo("Team18");

        //THEN
        Assert.assertEquals(expected1, returnedInfo1);
        Assert.assertEquals(expected2, returnedInfo2);
        Assert.assertEquals(expected3, returnedInfo3);
    }

    private List<Department> initHierarchy() {
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Marketing", Collections.singletonList(new Location("Krakow",
                Arrays.asList(new Team(0, "Team0"), new Team(1, "Team1"), new Team(2, "Team2"), new Team(3, "Team3"), new Team(4, "Team4"))))));

        departments.add(new Department("IT", Arrays.asList(
                new Location("Szczecin", Arrays.asList(new Team(5, "Team5"), new Team(6, "Team6"), new Team(7, "Team7"), new Team(8, "Team8"))),
                new Location("Warszawa", Arrays.asList(new Team(9, "Team9"), new Team(10, "Team10"), new Team(11, "Team11"), new Team(12, "Team12"))))));

        departments.add(new Department("HR", Arrays.asList(
                new Location("Krakow", Arrays.asList(new Team(13, "Team13"), new Team(14, "Team14"), new Team(15, "Team15"), new Team(16, "Team16"))),
                new Location("Poznan", Arrays.asList(new Team(17, "Team17"), new Team(18, "Team18"))))));


        return departments;
    }

}