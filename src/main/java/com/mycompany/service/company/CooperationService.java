package com.mycompany.service.company;

import com.mycompany.hierarchyObjects.Team;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class CooperationService {
    private Map<String, List<Team>> teamCo;
    private final Logger logger = Logger.getLogger(CooperationService.class);

    public CooperationService() {
        this.teamCo = new HashMap<>();
    }

    public void clearData() {
        this.teamCo.clear();
    }


    public boolean removeConnection(String teamName, List<Team> teams) {
        int sizeBefore = teamCo.size();
        int sizeAfter;

        Team teamToRemove = getTeamByName(teamName, teams);
        if (teamToRemove == null) {
            logger.error("Team: " + teamName + " not found.");
            return false;
        }
        removeTeamFromCooperatorsList(teamToRemove, teamName);
        sizeAfter = teamCo.remove(teamToRemove.getName()).size();

        return sizeAfter < sizeBefore;
    }

    private List<Team> removeTeamFromCooperatorsList(Team teamToRemove, String teamName){
        List<Team> teamCoworkers = teamCo.get(teamName);
        teamCoworkers.forEach(team -> teamCo.get(team.getName()).remove(teamToRemove));
        return teamCoworkers;
    }


    public Map<String, List<Team>> setCoworkers(int idParent, Integer[] coworkersIds, List<Team> teams) {
        if (checkListIfIdPresent(teams, idParent)) {
            return setConnections(evaluateTeamId(idParent,coworkersIds,teams), teams);
        }
        logger.error("Team-parent with id: " + idParent + "does not exists!");
        return null;
    }

    private List<Integer> evaluateTeamId(int parentId, Integer[] coworkersIds, List<Team> teams) {
        List<Integer> availableTeamIds = removeIncorrectIds(coworkersIds, teams);
        availableTeamIds.add(parentId);
        return availableTeamIds;
    }


    private Map<String, List<Team>> setConnections(List<Integer> availableTeamIds, List<Team> teamList) {
        for (Integer i : availableTeamIds) {
            List<Team> tmpList = new ArrayList<>();
            for (Integer j : availableTeamIds) {
                if (j != i) {
                    tmpList.add(getTeamFromList(j, teamList));
                }
            }
            List<Team> teamListTmp = teamCo.get(getTeamFromList(i, teamList));

            teamCo.put(getTeamFromList(i, teamList).getName(), checkTeamList(tmpList, teamListTmp));
            tmpList.clear();
        }
        return teamCo;
    }


    public List<Team> checkTeamList(List<Team> teamsToAdd, List<Team> existingList) {
        List<Team> returnList = new ArrayList<>();

        if (existingList != null) {
            teamsToAdd.removeIf(existingList::contains);
            returnList.addAll(existingList);
        }
        returnList.addAll(teamsToAdd);
        return returnList;
    }

    public boolean checkListIfIdPresent(List<Team> teamList, int id) {
        return teamList.stream().anyMatch(team -> id == team.getId());
    }

    public List<Integer> removeIncorrectIds(Integer[] intTable, List<Team> teamList) {
        List<Integer> teamIds = teamList.stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        return Arrays.stream(intTable)
                .filter(teamIds::contains)
                .collect(Collectors.toList());
    }

    public Team getTeamFromList(int teamId, List<Team> teamList) {
        return teamList.stream()
                .filter(team -> team.getId() == teamId)
                .findAny()
                .orElse(null);
    }


    public Team getTeamByName(String teamName, List<Team> teamList) {
        return teamList.stream()
                .filter(team -> team.getName().equals(teamName))
                .findAny()
                .orElse(null);
    }



    public Map<String, List<Team>> getTeamCo() {
        return teamCo;
    }
}
