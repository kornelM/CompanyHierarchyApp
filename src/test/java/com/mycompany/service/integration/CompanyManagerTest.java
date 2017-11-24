package com.mycompany.service.integration;

import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.service.details.TeamDetails;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.company.CompanyManager;
import com.mycompany.exceptions.TypeNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CompanyManagerTest {
    private CompanyManager companyManager;


    @Before
    public void setUp() {
        this.companyManager = new CompanyManager();
        companyManager.createDepartments(initDepartmentsForTests());
    }

    @Test
    public void testAddTeam() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", "Poznan", "newTeam");

        //THEN
        Assert.assertTrue(companyManager.add("TEAM", hd));
    }


    @Test(expected = TypeNotFoundException.class)
    public void testAddTeamThrowException() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", "Poznan", "newTeam");

        //THEN
        companyManager.add("invalidType", hd);
    }

    @Test
    public void testAddTeam_TeamExists() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "Krakow", "Team0");

        //THEN
        assertFalse(companyManager.add("TeAm", hd));
    }

    @Test
    public void testAddLocation() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", "Katowice", null);

        //THEN
        Assert.assertTrue(companyManager.add("LOCATION", hd));
    }

    @Test
    public void testAddLocation_LocationExists() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "Krakow", null);

        //THEN
        assertFalse(companyManager.add("Location", hd));
    }

    @Test
    public void testAddDepartment() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("newDepartment", null, null);

        //THEN
        Assert.assertTrue(companyManager.add("department", hd));
    }

    @Test
    public void testAddDepartment_DepartmentExists() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", null, null);

        //THEN
        assertFalse(companyManager.add("department", hd));
    }


    @Test
    public void testRemoveTeam() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "Krakow", "Team0");

        //THEN
        Assert.assertTrue(companyManager.remove("team", hd));
    }


    @Test(expected = TypeNotFoundException.class)
    public void testRemoveTeam_ThrowException() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails(null, null, null);

        companyManager.remove("invalidType", hd);
    }

    @Test
    public void testRemoveTeam_TeamDoesNotExists() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "Krakow", "invalidTeam");

        Assert.assertFalse(companyManager.remove("team", hd));
    }

    @Test
    public void testRemoveLocation() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "Krakow", null);

        //THEN
        Assert.assertTrue(companyManager.remove("location", hd));
    }

    @Test
    public void testRemoveLocation_LocationDoesNotExists() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "invalidLocation", null);

        Assert.assertFalse(companyManager.remove("location", hd));
    }


    @Test
    public void testRemoveDepartment() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", null, null);

        //THEN
        Assert.assertTrue(companyManager.remove("department", hd));
    }

    @Test
    public void testRemoveDepartment_DepartmentDoesNotExists() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("invalidDepartment", null, null);

        Assert.assertFalse(companyManager.remove("department", hd));
    }


    @Test
    public void testGetTeamByName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "Krakow", "Team1");
        Team expectedTeam = new Team(1, "Team1");

        //THEN
        Assert.assertEquals(expectedTeam, companyManager.getByName("team", hd));
    }

    @Test
    public void testGetTeamByName_InvalidName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("Marketing", "Krakow", "invalidTeam");

        Assert.assertNull(companyManager.getByName("team", hd));
    }

    @Test
    public void testGetTeamByName_InvalidLocationName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", "invalidLocName", null);

        //THEN
        Assert.assertNull(companyManager.getByName("team", hd));
    }

    @Test
    public void testGetTeamByName_InvalidDepartmentName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("invalidName", "does not matter cause depName is wrong", null);

        //THEN
        Assert.assertNull(companyManager.getByName("team", hd));
    }


    @Test
    public void testGetLocationByName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", "Poznan", null);
        Location expectedLocation = new Location("Poznan", Arrays.asList(
                new Team(6, "Team6"),
                new Team(7, "Team7"),
                new Team(8, "Team8")));


        //THEN
        Assert.assertEquals(expectedLocation, companyManager.getByName("location", hd));
    }

    @Test
    public void testGetLocationByName_InvalidLocationName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", "invalidLocName", null);


        //THEN
        Assert.assertNull(companyManager.getByName("location", hd));
    }

    @Test
    public void testGetLocationByName_InvalidDepartmentName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("invalidDepName", "invalidLocName", null);


        //THEN
        Assert.assertNull(companyManager.getByName("location", hd));
    }


    @Test
    public void testGetDepartmentByName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("IT", null, null);

        List<Team> teams = Arrays.asList(
                new Team(6, "Team6"),
                new Team(7, "Team7"),
                new Team(8, "Team8"));
        Department expectedDepartment = new Department("IT", Collections.singletonList(new Location("Poznan", teams)));


        //THEN
        Assert.assertEquals(expectedDepartment, companyManager.getByName("department", hd));
    }

    @Test
    public void testGetDepartmentByName_InvalidDepartmentName() throws TypeNotFoundException {
        //GIVEN
        HierarchyDetails hd = new HierarchyDetails("invalidDepName", null, null);


        //THEN
        Assert.assertNull(companyManager.getByName("department", hd));
    }



    @Test
    public void testGetAllTeams() throws Exception {
        //GIVEN
        List<Team> expectedTeamList = Arrays.asList(
                new Team(0, "Team0"),
                new Team(1, "Team1"),
                new Team(2, "Team2"),
                new Team(3, "Team3"),
                new Team(4, "Team4"),
                new Team(5, "Team5"),
                new Team(6, "Team6"),
                new Team(7, "Team7"),
                new Team(8, "Team8"));
        companyManager.createDepartments(initDepartmentsForTests());

        //WHEN
        List<Team> returnedTeams = companyManager.getAllTeams().get();

        //THEN
        Assert.assertEquals(expectedTeamList, returnedTeams);
    }


    @Test
    public void testGetTeamInfo() {
        //GIVEN
        String teamName = "Team5";
        TeamDetails expectedInfo = new TeamDetails("Marketing", "Warszawa");

        companyManager.createDepartments(initDepartmentsForTests());

        //WHEN
        TeamDetails returnedInfo = companyManager.getTeamInfo(teamName);

        //THEN
        Assert.assertEquals(expectedInfo, returnedInfo);
    }













    private List<Department> initDepartmentsForTests() {
        List<Team> teamList1 = new ArrayList<>();
        teamList1.add(new Team(0, "Team0"));
        teamList1.add(new Team(1, "Team1"));
        teamList1.add(new Team(2, "Team2"));

        List<Team> teamList2 = new ArrayList<>();
        teamList2.add(new Team(3, "Team3"));
        teamList2.add(new Team(4, "Team4"));
        teamList2.add(new Team(5, "Team5"));

        List<Team> teamList3 = new ArrayList<>();
        teamList3.add(new Team(6, "Team6"));
        teamList3.add(new Team(7, "Team7"));
        teamList3.add(new Team(8, "Team8"));

        List<Location> locationList1 = new ArrayList<>();
        locationList1.add(new Location("Krakow", teamList1));
        locationList1.add(new Location("Warszawa", teamList2));

        List<Location> locationList2 = new ArrayList<>();
        locationList2.add(new Location("Poznan", teamList3));


        List<Department> newDepartments = new ArrayList<>();
        newDepartments.add(new Department("Marketing", locationList1));
        newDepartments.add(new Department("IT", locationList2));

        return newDepartments;
    }


}