package com.mycompany.service.unit;

import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.TeamManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TeamManagerTest {
    private TeamManager teamManager;
    private List<Department> departments;

    @Before
    public void setUp() {
        this.teamManager = new TeamManager();
        init();
    }

    private void init() {
        List<Team> teams1 = new ArrayList<>();
        teams1.add(new Team(0, "Team0"));
        teams1.add(new Team(1, "Team1"));

        List<Location> locations1 = new ArrayList<>();
        locations1.add(new Location("Warszawa", teams1));


        List<Team> teams2 = new ArrayList<>();
        teams2.add(new Team(2, "Team2"));
        teams2.add(new Team(3, "Team3"));

        List<Location> locations2 = new ArrayList<>();
        locations2.add(new Location("Krakow", teams2));


        this.departments = new ArrayList<>();
        departments.add(new Department("Marketing", locations1));
        departments.add(new Department("IT", locations2));
    }

    @Test
    public void testAdd() throws Exception {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("Marketing", "Warszawa", "NewTeam");

        //WHEN
        boolean add = teamManager.add(departments, details);

        //THEN
        Assert.assertTrue(add);
    }

    @Test
    public void testAddTeam_WrongLocation() throws Exception {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("Marketing", "Krakow", "NewTeam");

        //WHEN
        boolean add = teamManager.add(departments, details);

        //THEN
        Assert.assertFalse(add);
    }

    @Test
    public void testGetObjectList(){
        //GIVEN
        HierarchyDetails details =  new HierarchyDetails("IT", "Krakow", null);
        List<Team> expectedTeams = Arrays.asList(new Team(2, "Team2"),
                new Team(3, "Team3"));

        //WHEN
        List<Team> returnedTeams =  teamManager.getObjectsList(departments, details).get();

        //THEN
        Assert.assertEquals(expectedTeams, returnedTeams);
    }

    @Test
    public void testGetObjectList_WrongHierarchyDetails(){
        //GIVEN
        HierarchyDetails details =  new HierarchyDetails("IT", "WrongLocationName", null);

        //WHEN
        Optional<List<Team>> returnedTeams = teamManager.getObjectsList(departments, details);

        //THEN
        Assert.assertFalse(returnedTeams.isPresent());
    }

    @Test
    public void testRemove(){
        //GIVEN
        HierarchyDetails details1 =  new HierarchyDetails("IT", "Krakow", "Team2");
        HierarchyDetails details2 =  new HierarchyDetails("IT", "Krakow", "TeamX");

        //THEN
        Assert.assertTrue(teamManager.remove(departments, details1));
        Assert.assertFalse(teamManager.remove(departments, details2));
    }

    @Test
    public void testGetAll(){
        //GIVEN
        HierarchyDetails details1 =  new HierarchyDetails("IT", "Krakow", null);
        HierarchyDetails details2 =  new HierarchyDetails("IT", "New York", null);

        List<Team> expectedTeamList = Arrays.asList(new Team(2, "Team2"),
                new Team(3, "Team3"));

        //THEN
        Assert.assertEquals(expectedTeamList, teamManager.getAll(departments, details1).get());
        Assert.assertFalse(teamManager.getAll(departments, details2).isPresent());
    }

    @Test
    public void testGet(){
        //GIVEN
        HierarchyDetails details1 =  new HierarchyDetails("Marketing", "Warszawa", "Team1");

        //THEN
        Assert.assertEquals(new Team(1, "Team1"), teamManager.get(departments, details1));
    }

    @Test
    public void testGet_TeamDoesNotExist(){
        //GIVEN
        HierarchyDetails details1 =  new HierarchyDetails("Marketing", "Warszawa", "Team2");

        //THEN
        Assert.assertNull(teamManager.get(departments, details1));
    }
}