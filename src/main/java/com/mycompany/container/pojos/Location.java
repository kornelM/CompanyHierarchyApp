package com.mycompany.container.pojos;

import java.util.List;

public class Location {
    private String name;
    private List<Team> teams;

    public Location(){}

    public Location(String name, List<Team> teams) {
        this.name = name;
        this.teams = teams;
    }

    public String getName() {
        return name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;

        Location location = (Location) o;

        return getName().equals(location.getName()) && getTeams().equals(location.getTeams());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getTeams().hashCode();
        return result;
    }
}
