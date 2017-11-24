package com.mycompany.service;

import com.mycompany.hierarchyObjects.Department;
import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.service.interfaces.ListCrawler;
import com.mycompany.service.interfaces.ObjectManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentManager implements ObjectManager, ListCrawler {
    private final Logger logger = Logger.getLogger(DepartmentManager.class);

    public DepartmentManager() {
    }


    @Override
    public boolean add(List<Department> dep, HierarchyDetails hd) {
        if (hd.getDepartmentName() == null) {
            return false;
        }
        Optional<List<Department>> departmentList = getObjectsList(dep, hd);

        if (!isDepartmentPresent(departmentList, hd.getDepartmentName()) && departmentList.isPresent()) {
            return departmentList.get().add(new Department(hd.getDepartmentName(), new ArrayList<>()));
        }
        return false;
    }

    @Override
    public boolean remove(List<Department> dep, HierarchyDetails hd) {
        Optional<List<Department>> departmentList = getObjectsList(dep, hd);

        if (isDepartmentPresent(departmentList, hd.getDepartmentName())) {
            List<Department> departments = departmentList.get();
            for (int i = 0; i < departments.size(); i++) {
                if (departments.get(i).getName().equals(hd.getDepartmentName())) {
                    departments.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Optional getAll(List<Department> dep, HierarchyDetails hd) {
        return getObjectsList(dep, hd);
    }

    @Override
    public Department get(List<Department> dep, HierarchyDetails hd) {
        Optional<List<Department>> departments = getObjectsList(dep, hd);

        if (isDepartmentPresent(departments, hd.getDepartmentName())) {
            return departments.get().stream()
                    .filter(department -> department.getName().equals(hd.getDepartmentName()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private boolean isDepartmentPresent(Optional list, String objName) {
        if (list.isPresent()) {
            List<Department> departments = (List<Department>) list.get();
            return departments.stream()
                    .anyMatch(department -> department.getName().equals(objName));
        }
        return false;
    }

    @Override
    public Optional<List<Department>> getObjectsList(List<Department> dep, HierarchyDetails hd) {
        return Optional.ofNullable(dep);
    }
}
