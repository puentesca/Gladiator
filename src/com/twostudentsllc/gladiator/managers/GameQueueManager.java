package com.twostudentsllc.gladiator.managers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Game;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.generic_classes.Team;

/**
 * Manages a queue of players for a Game
 */
public class GameQueueManager {
	
	private Main plugin;
	//String key is the name of the map
	private Map<String, PlayerQueue> queueList;

	//Game the gamequeue is registered to
	private Game game;

	//Maintains a group of players (used for group queuing together)
	private class PlayerGroup {

		private int groupLeader; //Tracks a group leader
		private ArrayList<Player> groupMembers;

		public PlayerGroup(Player player) {
			groupMembers = new ArrayList<Player>();
			groupMembers.add(player);
			groupLeader = 0;
		}

		public PlayerGroup(ArrayList<Player> members) {
			groupMembers = members;
			assignGroupLeader();
		}

		public PlayerGroup(ArrayList<Player> members, int groupLeader) {
			groupMembers = members;
			this.groupLeader = groupLeader;
		}

		/**
		 * Randomly assign a group leader
		 */
		private void assignGroupLeader() {
			groupLeader = (int)Math.floor((Math.random() * groupMembers.size())) - 1;
		}

		public ArrayList<Player> getMembers()
		{
			return groupMembers;
		}
		
		/**
		 * Removes all disconnected players from a playerGroup
		 * Assigns a new groupLeader
		 */
		public void removeInvalidMembers() {
			for(Player p: groupMembers) {
				if(!p.isOnline()) { //TODO: Can add extra conditions here for different removal checks
					groupMembers.remove(p);
				}
			}
			assignGroupLeader();
		}

	}
	//Maintains a queue of PlayerGroups
	private class PlayerQueue {
		
		private GameMap map;
		
		//Minimum number of teams
		private int teamMinimum;

		//Max number of teams
		private int teamMaximum;

		//The expected size of each team
		private int teamSize;

		private Queue<PlayerGroup> playerList;
		
		public PlayerQueue(GameMap map, int teamMinimum, int teamMaximum, int teamSize) {
			this.map = map;
			this.teamMinimum = teamMinimum;
			this.teamMaximum = teamMaximum;
			this.teamSize = teamSize;
			this.playerList = new LinkedList<PlayerGroup>();
			System.out.println("Created queue for map " + map.getMapName() + " with minTeams: " + teamMinimum + " maxTeams: " + teamMaximum + " teamSize: " + teamSize);
		}

		/**
		 * Checks if a playerQueue has a player in it
		 * @param toFind target player you are looking for
		 * @return true/false depending on whether the player is found
		 */
		public boolean contains(Player toFind) {
			for(PlayerGroup group: playerList) {
				if(group.groupMembers.contains(toFind)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Adds player to queue
		 * @param toAdd target player to add
		 * @return true/false depending on whether adding them was successful
		 */
		public boolean addPlayer(Player toAdd) {
			if(contains(toAdd))
				return false;
			playerList.add(new PlayerGroup(toAdd));
			System.out.println("PlayerQueue added player '" + toAdd.getName() + "' to queue");
			System.out.println("Total groups: " + playerList.size());
			return true;
		}

		/**
		 * Adds a group of players to a queue
		 * @param group ArrayList of players that are queuing together
		 * @return true/false depending on whether adding them was successful
		 */
		public boolean addGroup(ArrayList<Player> group) {
			for(Player p: group) {
				if(contains(p)) {
					return false;
				}
			}

			playerList.add(new PlayerGroup(group));
			return true;
		}

		/**
		 * NOTICE: Shouldn't be used typically (all players are in groups)
		 * Removes a player from the queue
		 * @param toRemove player to remove
		 * @return true/false depending on whether removing them was successful
		 */
		public boolean removePlayer(Player toRemove) {
			for(PlayerGroup group: playerList) {
				if(group.groupMembers.contains(toRemove)) {
					return group.groupMembers.remove(toRemove);
				}
			}
			return false;
		}

		/**
		 * Removes a group from the queue if the player is a group leader
		 * @param toRemove player in group that forces full group removal
		 * @return true/false depending on whether removing them was successful
		 */
		public boolean removeGroupByPlayer(Player toRemove) {
			for(PlayerGroup group: playerList) {
				if(group.groupMembers.contains(toRemove) && group.groupLeader == group.groupMembers.indexOf(toRemove)) {
					playerList.remove(group);
					return true;
				}
			}
			return false;
		}

		/**
		 * Gets the total number of players in queue
		 * @return int value for the total number of players in queue
		 */
		public int getTotalPlayersInQueue() {
			int total =  0;
			for(PlayerGroup group: playerList) {
				total += group.groupMembers.size();
			}
			return total;
		}
		

		/**
		 * Tries to see if there is a group combination that produces the required number of players
		 * @return true/false depending on whether there is a group combination
		 */
		public boolean canMakeGame() {
			
			if(!map.canStartMatch())
				return false;
			
			//O(n^2) method to find group combos TODO: Improve somehow? 
			for(PlayerGroup group: playerList) {
				int sum = group.groupMembers.size();

				System.out.println("Current sum: " + sum);
				if(sum >= teamMinimum * teamSize && sum <= teamMaximum * teamSize && sum % teamSize == 0) {
					System.out.println("Can make game! Sum: " + sum);
					return true;
				}

				for(PlayerGroup group2: playerList) {
					if(!group2.equals(group)) {
						sum += group2.groupMembers.size();
						System.out.println("Current inside loop sum: " + sum);
						if(sum >= teamMinimum * teamSize && sum <= teamMaximum * teamSize && sum % teamSize == 0) {
							System.out.println("Can make game! Sum: " + sum);
							return true;
						}
					}
				}
			}

			System.out.println("Cannot make game.");
			return false;
		}

		/** FIXME: Two O(n^2) operations is a bit overkill, i think there is some way to combine the getPlayers and canMakeGame functionality
		 * Returns a list of players from the groups
		 * @return an arraylist of players
		 */
		public ArrayList<Player> getPlayers() {
			
			int totalPlayers = 0;
			int maxPlayers =  teamMaximum * teamSize;
			int minPlayer = teamMinimum * teamSize;
			ArrayList<Player> players = new ArrayList<Player>();
			//Runs while the amount of players being grabbed is less than the max and while there are players to be grabbed
			while(totalPlayers <= maxPlayers && !playerList.isEmpty())
			{
				//Gets and removes the next group in the queue
				PlayerGroup group = playerList.element();
				playerList.remove();
				
				//TODO: Add a check to see if the group that is about to be added will be too large for the game. 
				//and if so, dont remove them but skip them somehow
				
				ArrayList<Player> groupMembers = group.getMembers();
				for(Player p : groupMembers)
				{
					players.add(p);
				}
			}
			return players;
			
//			ArrayList<PlayerGroup> groupList = null;
//
//			for(PlayerGroup group: playerList) {
//
//				int sum = group.groupMembers.size();
//				groupList = new ArrayList<PlayerGroup>();
//
//				//If one group is already enough players for the required game size
//				if(sum >= teamMinimum * teamSize && sum <= teamMaximum * teamSize && sum % teamSize == 0) {
//					playerList.remove(group);
//					return group.groupMembers;
//				}
//
//				for(PlayerGroup group2: playerList) {
//					if(!group2.equals(group)) {
//
//						sum += group2.groupMembers.size();
//						groupList.add(group2);
//
//						//If the combined groupList has enough players for the game
//						if(sum >= teamMinimum * teamSize && sum <= teamMaximum * teamSize && sum % teamSize == 0) {
//
//							//Combine all the groups into a single list of players
//							ArrayList<Player> players = new ArrayList<Player>();
//
//							for(PlayerGroup g: groupList) {
//								players.addAll(g.groupMembers);
//								playerList.remove(g);
//							}
//
//							return players;
//						}
//					}
//				}
//			}
//
//			return null;
		}

	}
	
    public GameQueueManager(Main plugin, Game game) {
    	this.plugin = plugin;
		this.game = game;
		queueList = new TreeMap<>();
    }


	/**
	 * Get the game the queues is registered to
	 * @return Game instance
	 */
	public Game getGame() {
		return game;
	}
    
    /**
     * Adds player to a game queue
     * @return boolean whether the player was successfully added
     */
    public boolean addPlayerToQueue(String mapName, Player toAdd) {

    	String key = mapName;

    	if(!queueList.containsKey(key)) {
    		System.out.println("No queue exists for the map: " + mapName);
    		return false;
    	}

    	boolean result = queueList.get(key).addPlayer(toAdd);
    	checkQueueStatus(mapName);
    	if(result)
    		System.out.println("Successfully added player to the queue for map: " + mapName);
    	else
    		System.out.println("Unsuccessfully added player to the queue for map: " + mapName);
    	return result;
    }
    
    /**
     * Checks a queues status and sees if the queue has enough players to start. If so, it starts the countdown
     * @param mapName
     */
    public void checkQueueStatus(String mapName)
    {
    	if(canMakeGame(mapName)) {
    		game.startMapCountdown(mapName);
    	}
    	else
    	{
    		System.out.println("Not enough player to start a match for map: '" + mapName + "'. Current players: " + queueList.get(mapName).getTotalPlayersInQueue());
    	}
    	
    }

	/**
	 * Adds a group of players to a game queue
	 * @return boolean whether the group was successfully added
	 */
    public boolean addGroupToQueue(String mapName, ArrayList<Player> group) {

		String key = mapName;

		if(!queueList.containsKey(key)) {
			System.out.println("No queue exists for the map: " + mapName);
			return false;
		}

		boolean result = queueList.get(key).addGroup(group);
    	checkQueueStatus(mapName);
    	if(result)
    		System.out.println("Successfully added player to the queue for map: " + mapName);
    	else
    		System.out.println("Unsuccessfully added player to the queue for map: " + mapName);
    	
    	return result;
	}
    
    /**
     * Removes player or group from game queue (same remove method since removal dependant on group leaders)
     * @return boolean whether the player was successfully removed
     */
    public boolean removePlayerOrGroupFromQueue(String mapName, Player toRemove) {

		String key = mapName;
    	
    	if(!queueList.containsKey(key)) {
    		return false;
    	}
    	
    	return queueList.get(key).removeGroupByPlayer(toRemove);
    }
    
    /**
     * Creates a game queue with some base requirement of players per game
	 * @param mapName map to add game queue for
	 * @param minimumTeams Minimum number of teams allowed on map
	 * @param maximumTeams Maximum number of teams allowed on map
	 * @param teamSize Size of each team
     * @return boolean whether the player was successfully removed
     */
    public boolean addGameQueue(String mapName, int minimumTeams, int maximumTeams, int teamSize) {

		String key = mapName;

    	if(queueList.containsKey(key)) {
    		return false;
    	}

    	if(!game.hasGameMap(mapName))
    		return false;
    	
    	
    	queueList.put(key, new PlayerQueue(game.getGameMap(mapName), minimumTeams, maximumTeams, teamSize));
    	System.out.println("Successfuly created a queue for map: " + mapName);
    	return true;
    }

	/**
	 * Remove a game queue for a map
	 * @param mapName name of the map to remove the game queue for
	 * @return true/false whether removing queue was successful
	 */
	public boolean removeGameQueue(String mapName) {

		String key = mapName;

    	if(!queueList.containsKey(key))
    		return false;

    	queueList.remove(key);
    	return true;
	}
    
    
    /**
     * Removes all disconnected players from queue
     */
    public void removeDisconnectedPlayers(String mapName) {

		String key =  mapName;

    	if(!queueList.containsKey(key)) {
    		return;
    	}
    	
    	PlayerQueue targetQueue =  queueList.get(key);
    	for(PlayerGroup group: targetQueue.playerList) {
    		group.removeInvalidMembers();
    	}
    	
    }
    
    /**
     * Checks if you have enough players to make a new game
     * @return boolean if there are enough players to make new game
     */
    public boolean canMakeGame(String mapName) {

		String key = mapName;

       	if(!queueList.containsKey(key)) {
       		System.out.println("QueueList does not contain a queue for: " + mapName);
    		return false;
    	}

       	//Removes any disconnected players
       	removeDisconnectedPlayers(mapName);
       	PlayerQueue targetGame = queueList.get(key);
       	
       	return targetGame.canMakeGame();
    }
    
    /**
     * Creates a list of players for a new game
     * @return null if there are not enough players or if the game queue does not exist,
     * 				otherwise it returns an ArrayList of Player instances
     */
    public ArrayList<Team> getTeams(String mapName) {

		String key = mapName;

       	if(!queueList.containsKey(key)) {
    		return null;
    	}
       	
       	if(canMakeGame(mapName)) {

	       	PlayerQueue targetGame = queueList.get(key);
	       	ArrayList<Player> players = targetGame.getPlayers();
	       	System.out.println("Grabbed " + players.size() + " players from the queue");

	       	//Split players into teams
			//TODO: This version splits people into teams randomly
			ArrayList<Team> teamList = new ArrayList<Team>();

			int q = 0;
			while(!players.isEmpty()) {

				Team newTeam = new Team(q);
				teamList.add(newTeam);

				//Fill a team with the required number of players and add them to their team
				for(int z = 0; z < targetGame.teamSize; z++) {

					int i = (int) (Math.random() * players.size()); //FIXME: I removed this part as it was causing an indexoutofbounds exception. Is it significant?: ' - 1;'
					Player toAdd = players.remove(i);

					newTeam.addPlayer(toAdd);
				}

				q++;
			}
			System.out.println("Generated " + (q + 1) + " teams");
			System.out.println("Size of teams arraylist: " + teamList.size());
			return teamList;

       	}
       	
       	return null;
    }
    
}