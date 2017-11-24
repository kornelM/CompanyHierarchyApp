package com.mycompany.service.interfaces;

import com.mycompany.hierarchyObjects.Department;
import com.mycompany.service.details.HierarchyDetails;

import java.util.List;
import java.util.Optional;

public interface ListCrawler {
     Optional getObjectsList(List<Department> departments, HierarchyDetails hd);
}
