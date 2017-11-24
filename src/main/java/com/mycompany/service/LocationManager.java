package com.mycompany.service;

import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.service.interfaces.ListCrawler;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.service.interfaces.CompanyObject;
import com.mycompany.service.interfaces.ObjectManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationManager implements ObjectManager, ListCrawler {
    private final Logger logger = Logger.getLogger(LocationManager.class);


    @Override
    public boolean add(List<Department> dep, HierarchyDetails hd) {
        if (hd.getDepartmentName() == null) {
            return false;
        }
        if (hd.getLocationName() == null) {
            return false;
        }

        Optional<List<Location>> locationList = getObjectsList(dep, hd);

        if (!isLocationPresent(locationList, hd.getLocationName()) && locationList.isPresent()) {
            return locationList.get().add(new Location(hd.getLocationName(), new ArrayList<>()));
        }
        return false;
    }

    @Override
    public boolean remove(List<Department> dep, HierarchyDetails hd) {
        Optional<List<Location>> locationList = getObjectsList(dep, hd);

        if (isLocationPresent(locationList, hd.getLocationName())) {
            List<Location> locations = locationList.get();
            for (int i = 0; i < locations.size(); i++) {
                if (locations.get(i).getName().equals(hd.getLocationName())) {
                    locations.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Optional<List<Location>> getAll(List<Department> dep, HierarchyDetails hd) {
        return getObjectsList(dep, hd);
    }

    @Override
    public CompanyObject get(List<Department> dep, HierarchyDetails hd) {
        Optional<List<Location>> locations = getObjectsList(dep, hd);

        if (isLocationPresent(locations, hd.getLocationName())) {
            List<Location> locationList = locations.get();
            for (Location l : locationList) {
                if (l.getName().equals(hd.getLocationName())) {
                    return l;
                }
            }
        }
        return null;
    }

    @Override
    public Optional<List<Location>> getObjectsList(List<Department> dep, HierarchyDetails hd) {
        for (Department d : dep) {
            if (d.getName().equals(hd.getDepartmentName())) {
                return Optional.ofNullable(d.getLocations());
            }
        }
        return Optional.empty();
    }


    private boolean isLocationPresent(Optional list, String objName) {
        if (list.isPresent()) {
            List<Location> locations = (List<Location>) list.get();
            return locations.stream()
                    .anyMatch(location -> location.getName().equals(objName));
        }
        return false;
    }
}
