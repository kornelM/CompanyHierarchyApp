package com.mycompany.controllers.pojo;

public class HierarchyDetails {
    private String department;
    private String location;
    private String team;

    public HierarchyDetails(){}

    public HierarchyDetails(String department, String location, String team) {
        this.department = department;
        this.location = location;
        this.team = team;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
