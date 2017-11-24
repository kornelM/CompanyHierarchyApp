package com.mycompany.service.interfaces;

import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.hierarchyObjects.Department;

import java.util.List;
import java.util.Optional;

public interface ObjectManager {
    boolean add(List<Department> departments, HierarchyDetails hd);

    boolean remove(List<Department> departments, HierarchyDetails hierarchyDetails);

    Optional getAll(List<Department> departments, HierarchyDetails hierarchyDetails);

    CompanyObject get(List<Department> departments, HierarchyDetails hd);
//    boolean isTeamPresent(Optional list, String objName);
}
