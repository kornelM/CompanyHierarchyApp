package com.mycompany.service.details;

public class TeamDetails {
    private String department;
    private String location;

    public TeamDetails() {
    }

    public TeamDetails(String department, String location) {
        this.department = department;
        this.location = location;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamDetails)) return false;

        TeamDetails that = (TeamDetails) o;

        if (!getDepartment().equals(that.getDepartment())) return false;
        return getLocation().equals(that.getLocation());
    }

    @Override
    public int hashCode() {
        int result = getDepartment().hashCode();
        result = 31 * result + getLocation().hashCode();
        return result;
    }
}


