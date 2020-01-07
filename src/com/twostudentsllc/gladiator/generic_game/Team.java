package com.twostudentsllc.gladiator.generic_game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {

    private int teamID;
    
    private HashMap<Player, PlayerStats> teamMembers;
    
    /**
     * Holds the players spawns
     */
    private HashMap<Player, Location> playerSpawns;
    
    private int wins = 0;


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

    public void incrementWins()
    {
    	wins++;
    }
    
    public int getWins()
    {
    	return wins;
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
    
    /**
     * Increments a players deaths
     * @param p The player that died
     */
    public void incrementPlayerDeaths(Player p)
    {
    	getPlayerStats(p).incrementDeaths();
    }
    
    /**
     * Increments a players kills
     * @param p The player that earned a kill
     */
    public void incrementPlayerKills(Player p)
    {
    	getPlayerStats(p).incrementKills();
    }
    
    public void setPlayerSpawns(HashMap<Player, Location> playerSpawns)
    {
    	this.playerSpawns = playerSpawns;
    }
    
    /**
     * Sets all player stats back to their default states and erases all new stats.
     */
    public void resetAllPlayerStats()
    {
    	HashMap<Player, PlayerStats> resetMembersStats = new HashMap<Player, PlayerStats>();
    	for(Player p : teamMembers.keySet())
    	{
    		resetMembersStats.put(p, new PlayerStats());
    	}
    	teamMembers = resetMembersStats;
    }
    
    /**
     * Resets playerDeaths and elimination status while leaving other status untouched
     */
    public void resetAllPlayerLives()
    {
    	for(Player p : teamMembers.keySet())
    	{
    		PlayerStats stats = teamMembers.get(p);
    		stats.resetDeaths();
    	}
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
