package com.twostudentsllc.gladiator.managers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.entity.Player;

public class TeamManager {
	
	private ArrayList<Player> playerList;
	private ArrayList<Team> teamList;
	
	//Team class manages a list of players for a team
	class Team {
		
		private int teamID;
		private Map<Player, Map<String, Object>> teamMembers;
		
		/*
		 * Takes in a team id and a list of players
		 */
		public Team(int teamID, ArrayList<Player> teamMembers) {
			this.teamID = teamID;
			this.teamMembers = new TreeMap<>();
			
			//Each player has a state tracker tied to them
			for(Player player: teamMembers) {
				this.teamMembers.put(player, new TreeMap<String, Object>());
			}
		}
		
		/*
		 * Overridden constructor with a default state option
		 */
		public Team(int teamID, ArrayList<Player> teamMembers, Map<String, Object> defaultState) {
			this(teamID, teamMembers);
			
			for(Player player: teamMembers) {
				this.teamMembers.put(player, defaultState);
			}
		}
		
		public int getTeamID() {
			return teamID;
		}
		
		public Set<Player> getPlayers() {
			return teamMembers.keySet();
		}
		
		public Map<String, Object> getPlayerState(Player target) {
			
			if(!teamMembers.containsKey(target))
				return null;
			
			return teamMembers.get(target);
		}
	}
	
	//Constructor takes in a playerlist from the QueueManager
	public TeamManager(ArrayList<Player> playerList) {
		this.playerList = playerList;
		teamList = new ArrayList<Team>();
	}
	
	/*
	 * Creates teams of players according to the numberOfTeams specified
	 * @return null if invalid, ArrayList<Team> if everything worked fine
	 */
	public ArrayList<Team> getTeams(int numberOfTeams) {
		if(numberOfTeams > playerList.size())
			return null;
		
		//Return previously made team if correct size
		if(teamList.size() == numberOfTeams)
			return teamList;
		
		int players = playerList.size();
		int playersPerTeam = players/numberOfTeams;
		
		//Return null if players can't be split into teams equally
		if(players%numberOfTeams != 0) {
			return null;
		}
		
		teamList = new ArrayList<Team>();
		
		//Create the required number of teams
		for(int i = 0; i < numberOfTeams; i++) {
			
			ArrayList<Player> teamMembers = new ArrayList<Player>();
			
			//Add players according to number of teams required
			for(int q = 0; q < playersPerTeam; q++) {
				teamMembers.add(playerList.get(i * playersPerTeam + q));
			}
			
			teamList.add(new Team(i, teamMembers));
		}
		
		return teamList;
		
	}
	
	/*
	 * Moves players from one team to another
	 * @return boolean whether or not a player was successfully moved
	 */
	public boolean movePlayerFromTeam(Player toMove, int teamId1, int teamId2) {
		try {
			
			Team originalTeam = teamList.get(teamId1);
			Team targetTeam = teamList.get(teamId2);
			
			originalTeam.getPlayers().remove(toMove);
			
			targetTeam.getPlayers().add(toMove);
			
			return true;
			
		} catch(IndexOutOfBoundsException e) {
			return false;
		}
		
	}
	
	public int getTotalTeams() {
		return teamList.size();
	}
	
	public int getTotalPlayers() {
		return playerList.size();
	}
	

}
