package com.twostudentsllc.gladiator.generic_classes;

import org.bukkit.entity.Player;

import java.util.*;

public class Team {

    private int teamID;
    private HashMap<Player, PlayerStats> teamMembers;

    /*
     * Takes in a team id and a list of players
     */
    public Team(int teamID, ArrayList<Player> teamMembers) {
        this.teamID = teamID;
        this.teamMembers = new HashMap<>();

        //Initializes each player with default playerStats
        for(Player player: teamMembers) {
            this.teamMembers.put(player, new PlayerStats());
        }
    }

    public PlayerStats getPlayerStats(Player player) {
        return teamMembers.get(player);
    }

    public int getTeamID() {
        return teamID;
    }

    public Set<Player> getPlayers() {
        return teamMembers.keySet();
    }

    public int getTotalPlayers() {
        return teamMembers.size();
    }
}
