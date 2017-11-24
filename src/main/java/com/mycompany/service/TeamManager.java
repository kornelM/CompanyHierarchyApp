package com.mycompany.service;

import com.mycompany.service.details.HierarchyDetails;
import com.mycompany.service.interfaces.ListCrawler;
import com.mycompany.hierarchyObjects.Department;
import com.mycompany.hierarchyObjects.Location;
import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.interfaces.CompanyObject;
import com.mycompany.service.interfaces.ObjectManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class TeamManager implements ObjectManager, ListCrawler {
    private final Logger logger = Logger.getLogger(TeamManager.class);

    @Override
    public boolean add(List<Department> d, HierarchyDetails hd) {
        Optional<List<Team>> teamList = getObjectsList(d, hd);

        if (!isTeamPresent(teamList, hd.getTeamName()) && teamList.isPresent()) {
            return teamList.get().add(new Team(hd.getTeamName()));
        }
        return false;

    }

    @Override
    public boolean remove(List<Department> d, HierarchyDetails hd) {
        Optional<List<Team>> teamList = getObjectsList(d, hd);

        if (isTeamPresent(teamList, hd.getTeamName())) {
            List<Team> teams = teamList.get();
            for (int i = 0; i < teams.size(); i++) {
                if (teams.get(i).getName().equals(hd.getTeamName())) {
                    teams.remove(i);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Optional getAll(List<Department> d, HierarchyDetails hd) {
        return getObjectsList(d, hd);
    }

    @Override
    public CompanyObject get(List<Department> d, HierarchyDetails hd) {
        Optional<List<Team>> teams = getObjectsList(d, hd);

        if (isTeamPresent(teams, hd.getTeamName())) {
            List<Team> teamList = teams.get();
            for (Team t : teamList) {
                if (t.getName().equals(hd.getTeamName())) {
                    return t;
                }
            }
        }
        return null;
    }


    private boolean isTeamPresent(Optional list, String objName) {
        if (list.isPresent()) {
            List<Team> teams = (List<Team>) list.get();
            return teams.stream()
                    .anyMatch(team -> team.getName().equals(objName));
        }
        return false;
    }

    @Override
    public Optional<List<Team>> getObjectsList(List<Department> dep, HierarchyDetails hd) {
        for (Department d : dep) {
            if (d.getName().equals(hd.getDepartmentName())) {
                for (Location l : d.getLocations()) {
                    if (l.getName().equals(hd.getLocationName())) {
                        return Optional.ofNullable(l.getTeams());
                    }
                }

            }
        }
        return Optional.empty();
    }
}
