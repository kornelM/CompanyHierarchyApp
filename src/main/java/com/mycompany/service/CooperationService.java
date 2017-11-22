package com.mycompany.service;

import com.mycompany.hierarchyObjects.Team;
import com.mycompany.service.interfaces.ICooperationService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class CooperationService implements ICooperationService {
    private Map<String, List<Team>> teamCo;
    private final Logger logger = Logger.getLogger(CooperationService.class);

    public CooperationService() {
        this.teamCo = new HashMap<>();
    }

    public void clearData() {
        this.teamCo.clear();
    }


    @Override
    public boolean removeConnection(String teamName, List<Team> teams) {
        int sizeBefore = teamCo.size();
        int sizeAfter;

        Team teamToRemove = getTeamByName(teamName, teams);
        if (teamToRemove == null) {
            logger.error("Team: " + teamName + " not found.");
            return false;
        }

        List<Team> teamCoworkers = teamCo.get(teamName);

        teamCoworkers
                .forEach(team -> teamCo.get(team.getName()).remove(teamToRemove));

        sizeAfter = teamCo.remove(teamToRemove.getName()).size();
        logger.info("Size before: " + sizeBefore + ", sizeAfter: " + sizeAfter);

        return sizeAfter < sizeBefore;
    }


    @Override
    public Map<String, List<Team>> setCoworkers(int idParent, Integer[] coworkersIds, List<Team> teams) {
        List<Integer> availableTeamIds;
        if (checkListIfIdPresent(teams, idParent)) {
            availableTeamIds = removeIncorrectIds(coworkersIds, teams);
            availableTeamIds.add(idParent);
            return setConnections(availableTeamIds, teams);
        }
        logger.error("Team-parent with id: " + idParent + "does not exists!");
        return null;
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


    @Override
    public List<Team> checkTeamList(List<Team> teamsToAdd, List<Team> existingList) {
        List<Team> returnList = new ArrayList<>();

        if (existingList != null) {
            teamsToAdd.removeIf(existingList::contains);
            returnList.addAll(existingList);
        }else {
            logger.error("List of coworkers empty!");
        }
        returnList.addAll(teamsToAdd);
        return returnList;
    }


    @Override
    public boolean checkListIfIdPresent(List<Team> teamList, int id) {
        return teamList.stream().anyMatch(team -> id == team.getId());
    }


    @Override
    public List<Integer> removeIncorrectIds(Integer[] intTable, List<Team> teamList) {
        List<Integer> teamIds = teamList.stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        return Arrays.stream(intTable)
                .filter(teamIds::contains)
                .collect(Collectors.toList());
    }


    @Override
    public Team getTeamFromList(int teamId, List<Team> teamList) {
        return teamList.stream()
                .filter(team -> team.getId() == teamId)
                .findAny()
                .orElse(null);
    }


    @Override
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
