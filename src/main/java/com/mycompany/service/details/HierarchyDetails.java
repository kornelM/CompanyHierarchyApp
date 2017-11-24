package com.mycompany.service.details;

public class HierarchyDetails {
    private String departmentName;
    private String locationName;
    private String teamName;

    public HierarchyDetails(){}

    public HierarchyDetails(String departmentName, String locationName, String teamName) {
        this.departmentName = departmentName;
        this.locationName = locationName;
        this.teamName = teamName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "HierarchyDetails{" +
                "departmentName='" + departmentName + '\'' +
                ", locationName='" + locationName + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}
