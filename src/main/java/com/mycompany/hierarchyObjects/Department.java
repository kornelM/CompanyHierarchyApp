package com.mycompany.hierarchyObjects;

import java.util.List;

public class Department {


    private String name;
    private List<Location> locations;

    public Department(){}

    public Department(String name, List<Location> locations) {
        this.name = name;
        this.locations = locations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;

        Department that = (Department) o;

        if (!getName().equals(that.getName())) return false;
        return getLocations().equals(that.getLocations());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getLocations().hashCode();
        return result;
    }
}
