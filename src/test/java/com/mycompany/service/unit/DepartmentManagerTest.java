package com.mycompany.service.unit;

import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.service.DepartmentManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DepartmentManagerTest {
    private DepartmentManager departmentManager;
    private List<Department> departments;

    @Before
    public void setUp() {
        this.departmentManager = new DepartmentManager();
        init();
    }

    private void init() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("Warszawa", new ArrayList<>()));

        this.departments = new ArrayList<>();
        departments.add(new Department("Marketing", locations));
        departments.add(new Department("IT", locations));
        departments.add(new Department("HR", locations));
        departments.add(new Department("Accounting", locations));
    }

    @Test
    public void testAddDepartment() throws Exception {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("Directors", null, null);

        //WHEN
        boolean departmentAdded = departmentManager.add(departments, details);

        //THEN
        Assert.assertTrue(departmentAdded);
    }

    @Test
    public void testAddDepartment_NoDepartmentName() throws Exception {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails(null, null, null);

        //WHEN
        boolean departmentAdded = departmentManager.add(departments, details);

        //THEN
        Assert.assertFalse(departmentAdded);
    }

    @Test
    public void testGetObjectList() {
        //GIVEN
        List<Department> expectedDepartments = Arrays.asList(new Department("Marketing", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))),
                new Department("IT", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))),
                new Department("HR", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))),
                new Department("Accounting", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))));

        //WHEN
        List<Department> returnedDepartments = departmentManager.getObjectsList(departments, null).get();

        //THEN
        Assert.assertEquals(expectedDepartments, returnedDepartments);
    }

    @Test
    public void testGetObjectList_DepartmentsListIsNull() {
        List<Department> actualDepartmentList = null;

        //WHEN
        Optional<List<Department>> returnedDepartments = departmentManager.getObjectsList(actualDepartmentList, null);

        //THEN
        Assert.assertFalse(returnedDepartments.isPresent());
    }

    @Test
    public void testGetObjectList_EmptyDepartmentsList() {
        List<Department> actualDepartmentList = new ArrayList<>();

        //WHEN
        Optional<List<Department>> returnedDepartments = departmentManager.getObjectsList(actualDepartmentList, null);

        //THEN
        Assert.assertTrue(returnedDepartments.isPresent());
        Assert.assertTrue(returnedDepartments.get().size() == 0);
    }


    @Test
    public void testRemoveDepartmentByName() {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("IT", "nothingToDo", "canBeNull");
        HierarchyDetails details1 = new HierarchyDetails("Candy sellers", "", null);

        //THEN
        Assert.assertTrue(departmentManager.remove(departments, details));
        Assert.assertFalse(departmentManager.remove(departments, details1));
    }

    @Test
    public void testGetAllDepartments() {
        //GIVEN

        List<Department> expectedDepartments = Arrays.asList(new Department("Marketing", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))),
                new Department("IT", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))),
                new Department("HR", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))),
                new Department("Accounting", Collections.singletonList(new Location("Warszawa", new ArrayList<>()))));

        List<Department> emptyDepartmentsList = new ArrayList<>();


        //THEN //null
        Assert.assertEquals(expectedDepartments, departmentManager.getAll(departments, null).get());
        Assert.assertTrue(departmentManager.getAll(emptyDepartmentsList, null).isPresent());
    }

    @Test
    public void testGetDepartmentByName() {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("IT", null, null);
        Department expectedDepartment = new Department("IT", Collections.singletonList(new Location("Warszawa", new ArrayList<>())));

        //THEN
        Assert.assertEquals(expectedDepartment, departmentManager.get(departments, details));
    }

    @Test
    public void testGet_DepartmentDoesNotExist() {
        //GIVEN
        HierarchyDetails details = new HierarchyDetails("Flat-Earthers", null, null);

        //THEN
        Assert.assertNull(departmentManager.get(departments, details));
    }

}