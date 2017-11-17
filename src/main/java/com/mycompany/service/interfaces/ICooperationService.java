package com.mycompany.service.interfaces;

import com.mycompany.container.pojos.Team;

import java.util.List;
import java.util.Map;

public interface ICooperationService {
    boolean removeConnection(String teamId, List<Team> teams);

    Map<String, List<Team>> setCoworkers(int idParent, Integer[] coworkersIds, List<Team> teams);

    List<Team> checkTeamList(List<Team> teamsToAdd, List<Team> existingList);

    boolean checkListIfIdPresent(List<Team> teamList, int id);

    List<Integer> removeIncorrectIds(Integer[] intTable, List<Team> teamList);

    Team getTeamFromList(int teamId, List<Team> teamList);

    Team getTeamByName(String teamName, List<Team> teamList);
}
