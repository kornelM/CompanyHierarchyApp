package com.mycompany.service.integration;

import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.company.CompanyManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompanyManagerCreateTest {

    @Test
    public void testCreateDepartments() throws Exception {

        //GIVEN
        CompanyManager companyManager = new CompanyManager();
        List<Department> newDepartments = Arrays.asList(
                new Department("Marketing", Collections.singletonList(new Location("Berlin", Collections.singletonList(new Team(0, "Team0"))))),
                new Department("IT", Collections.singletonList(new Location("Szczecin", Arrays.asList(new Team(1, "Team1"), new Team(2, "Team2"))))),
                new Department("HR", Collections.singletonList(new Location("Katowice", Arrays.asList(new Team(2, "Team2"), new Team(3, "Team3")))))
        );


        //THEN
        boolean created = companyManager.createDepartments(newDepartments);
        Assert.assertTrue(created);

        created = companyManager.createDepartments(newDepartments);
        Assert.assertFalse(created);

        created = companyManager.createDepartments(null);
        Assert.assertFalse(created);

        created = companyManager.createDepartments(new ArrayList<>());
        Assert.assertFalse(created);
    }
}
