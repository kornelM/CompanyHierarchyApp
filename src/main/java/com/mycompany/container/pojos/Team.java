package com.mycompany.container.pojos;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private static int idNumber;
    private static List<Integer> availableIds = new ArrayList<>();


    private int id;
    private String name;

    public Team() {
        this.id = idNumber;
        setAvailableIds();
    }

    public Team(String name) {
        this.id = idNumber;
        this.name = name;
        setAvailableIds();
    }

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
        setAvailableIds();
    }

    private void setAvailableIds() {
        availableIds.add(idNumber);
        idNumber++;
    }


    public static void clearData() {
        idNumber = 0;
        availableIds.clear();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;

        Team team = (Team) o;

        if (getId() != team.getId()) return false;
        return getName().equals(team.getName());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
