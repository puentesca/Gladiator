package com.twostudentsllc.gladiator.generic_classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {

    private int teamID;
    
    private HashMap<Player, PlayerStats> teamMembers;
    
    /**
     * Holds the players spawns
     */
    private HashMap<Player, Location> playerSpawns;


    /**
     * Default constructor with only known teamid
     * @param teamID
     */
    public Team(int teamID) {
        this.teamID = teamID;
        this.teamMembers = new HashMap<>();
    }

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

    /**
     * Adds a player to the team
     * @param toAdd player to add
     * @return returns whether adding was successful
     */
    public boolean addPlayer(Player toAdd) {
        if(teamMembers.containsKey(toAdd))
            return false;
        teamMembers.put(toAdd, new PlayerStats());
        return true;
    }

    /**
     * Gets whether or not this team is still in the game or not
     * @return False if the team is still in the game (Not eliminated)
     */
    public boolean isEliminated()
    {
    	for(Player p : teamMembers.keySet())
    	{
    		//If any player on the team is not eliminated, the team is still in game
    		if(!teamMembers.get(p).isEliminated())
    			return false;
    	}
    	return true;
    }
    
    public void setPlayerSpawns(HashMap<Player, Location> playerSpawns)
    {
    	this.playerSpawns = playerSpawns;
    }
    
    public HashMap<Player, Location> getPlayerSpawns()
    {
    	return playerSpawns;
    }
    /**
     * Checks to see if the given player is on the team
     * @param p The player to check
     * @return True if the player is on this team
     */
    public boolean containsPlayer(Player p)
    {
    	return teamMembers.containsKey(p);
    }
    
    public PlayerStats getPlayerStats(Player player) {
        return teamMembers.get(player);
    }

    public int getTeamID() {
        return teamID;
    }

    public ArrayList<Player> getPlayers() {
    	ArrayList<Player> players = new ArrayList<Player>();
    	for(Player p : teamMembers.keySet())
    	{
    		players.add(p);
    	}
        return players;
    }

    public int getTotalPlayers() {
        return teamMembers.size();
    }
}
