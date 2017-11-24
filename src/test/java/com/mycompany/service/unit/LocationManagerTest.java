package com.mycompany.service.unit;

import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.service.LocationManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LocationManagerTest {
    private LocationManager locationManager;
    private List<Department> departments;

    @Before
    public void setUp() {
        this.locationManager = new LocationManager();
        init();
    }

    private void init() {
        List<Location> locations1 = new ArrayList<>();
        locations1.add(new Location("Warszawa", new ArrayList<>()));
        locations1.add(new Location("Gdansk", new ArrayList<>()));

        List<Location> locations2 = new ArrayList<>();
        locations2.add(new Location("Wroclaw", new ArrayList<>()));
        locations2.add(new Location("London", new ArrayList<>()));

        this.departments = new ArrayList<>();
        departments.add(new Department("Directors", locations1));
        departments.add(new Department("IT", locations1));
        departments.add(new Department("HR", locations2));
        departments.add(new Department("Accounting", locations2));
    }


    @Test
    public void addLocation() {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("Directors", "newLocation", null);

        //THEN
        Assert.assertTrue(locationManager.add(departments, details));
    }

    @Test
    public void testAddLocation_NoLocationName() throws Exception {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("IT", null, null);

        //THEN
        Assert.assertFalse(locationManager.add(departments, details));
    }

    @Test
    public void testGetObjectList() {
        //GIVEN
        List<Location> expectedLocations = Arrays.asList(new Location("Warszawa", new ArrayList<>()), new Location("Gdansk", new ArrayList<>()));
        HierarchyDetails hd = new HierarchyDetails("Directors", null, null);

        //WHEN
        List<Location> returnedLocations = locationManager.getObjectsList(departments, hd).get();

        //THEN
        Assert.assertEquals(expectedLocations, returnedLocations);
    }

    @Test
    public void testGetObjectList_LocationListIsNull() {
        //GIVEN
        List<Department> actualDepartmentList = Arrays.asList(new Department("HR", null));
        HierarchyDetails hd = new HierarchyDetails("HR", null, null);

        //WHEN
        Optional<List<Location>> returnedLocations = locationManager.getObjectsList(actualDepartmentList, hd);

        //THEN
        Assert.assertFalse(returnedLocations.isPresent());
    }

    @Test
    public void testGetObjectList_EmptyLocationList() {
        //GIVEN
        List<Department> actualDepartmentList = Arrays.asList(new Department("HR", new ArrayList<>()));
        HierarchyDetails hd = new HierarchyDetails("HR", null, null);

        //WHEN
        Optional<List<Location>> returnedDepartments = locationManager.getObjectsList(actualDepartmentList, hd);

        //THEN
        Assert.assertTrue(returnedDepartments.isPresent());
        Assert.assertTrue(returnedDepartments.get().size() == 0);
    }


    @Test
    public void testRemoveLocation() {
        //GIVEN
        HierarchyDetails details1 = new HierarchyDetails("HR", "London", null);
        HierarchyDetails details2 = new HierarchyDetails("IT", "Suwalki", null);

        //THEN
        Assert.assertTrue(locationManager.remove(departments, details1));
        Assert.assertFalse(locationManager.remove(departments, details2));
    }

    @Test
    public void testGetAllLocation() {
        //GIVEN
        HierarchyDetails details1 = new HierarchyDetails("IT", null, null);
        HierarchyDetails details2 = new HierarchyDetails("invalidDepartmentName", null, null);

        List<Location> expectedLocationList = Arrays.asList(new Location("Warszawa", new ArrayList<>()),
                new Location("Gdansk", new ArrayList<>()));

        //THEN
        Assert.assertEquals(expectedLocationList, locationManager.getAll(departments, details1).get());
        Assert.assertFalse(locationManager.getAll(departments, details2).isPresent());
    }

    @Test
    public void testGetLocationByName() throws Exception {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("Accounting", "Wroclaw", null);
        Location expectedLocation = new Location("Wroclaw", new ArrayList<>());

        //THEN
        Assert.assertEquals(expectedLocation, locationManager.get(departments, details));


    }
}