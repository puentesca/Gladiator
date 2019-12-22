package com.twostudentsllc.gladiator.managers;

import java.util.*;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QueueManager {

	//TODO: Some way to distignuish the different maps along with minigame

	/**
	 * Format for the String references in the map is
	 * minigameName:map
	 */
	private Map<String, PlayerQueue> queueList;

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
		
		private int playersNeeded;
		private Queue<PlayerGroup> playerList;
		
		public PlayerQueue(int playersNeeded) {
			this.playersNeeded = playersNeeded;
			this.playerList = new LinkedList<PlayerGroup>();
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

			//O(n^2) method to find group combos TODO: Improve somehow?
			for(PlayerGroup group: playerList) {
				int sum = group.groupMembers.size();

				if(sum == playersNeeded) {
					return true;
				}

				for(PlayerGroup group2: playerList) {
					if(!group2.equals(group)) {
						sum += group2.groupMembers.size();

						if(sum == playersNeeded) {
							return true;
						}
					}
				}
			}

			return false;
		}

		/** FIXME: Two O(n^2) operations is a bit overkill, i think there is some way to combine the getPlayers and canMakeGame functionality
		 * Returns a list of players from the groups
		 * @return an arraylist of players
		 */
		public ArrayList<Player> getPlayers() {

			ArrayList<PlayerGroup> groupList = null;

			for(PlayerGroup group: playerList) {

				int sum = group.groupMembers.size();
				groupList = new ArrayList<PlayerGroup>();

				//If one group is already enough players for the required game size
				if(sum == playersNeeded) {
					playerList.remove(group);
					return group.groupMembers;
				}

				for(PlayerGroup group2: playerList) {
					if(!group2.equals(group)) {

						sum += group2.groupMembers.size();
						groupList.add(group2);

						//If the combined groupList has enough players for the game
						if(sum == playersNeeded) {

							//Combine all the groups into a single list of players
							ArrayList<Player> players = new ArrayList<Player>();

							for(PlayerGroup g: groupList) {
								players.addAll(g.groupMembers);
								playerList.remove(g);
							}

							return players;
						}
					}
				}
			}

			return null;
		}

	}
	
    public QueueManager() {
    	queueList = new TreeMap<>();
    }
    
    /**
     * Adds player to a game queue
     * @return boolean whether the player was successfully added
     */
    public boolean addPlayerToQueue(String gameName, String mapName, Player toAdd) {

    	String key = gameName + ":" + mapName;

    	if(!queueList.containsKey(key)) {
    		return false;
    	}

    	return queueList.get(key).addPlayer(toAdd);
    }

	/**
	 * Adds a group of players to a game queue
	 * @return boolean whether the group was successfully added
	 */
    public boolean addGroupToQueue(String gameName, String mapName, ArrayList<Player> group) {

		String key = gameName + ":" + mapName;

		if(!queueList.containsKey(key)) {
			return false;
		}

		return queueList.get(key).addGroup(group);
	}
    
    /**
     * Removes player or group from game queue (same remove method since removal dependant on group leaders)
     * @return boolean whether the player was successfully removed
     */
    public boolean removePlayerOrGroupFromQueue(String gameName, String mapName, Player toRemove) {

		String key = gameName + ":" + mapName;
    	
    	if(!queueList.containsKey(key)) {
    		return false;
    	}
    	
    	return queueList.get(key).removeGroupByPlayer(toRemove);
    }
    
    /**
     * Creates a game queue with some base requirement of players per game
     * @return boolean whether the player was successfully removed
     */
    public boolean addGameQueue(String gameName, String mapName, int playersNeeded) {

		String key = gameName + ":" + mapName;

    	if(queueList.containsKey(key)) {
    		return false;
    	}

    	queueList.put(key, new PlayerQueue(playersNeeded));
    	return true;
    }

    public boolean removeGameQueue(String gameName, String mapName) {

		String key = gameName + ":" + mapName;

    	if(!queueList.containsKey(key))
    		return false;

    	queueList.remove(key);
    	return true;
	}
    
    
    /**
     * Removes all disconnected players from queue
     */
    public void removeDisconnectedPlayers(String gameName, String mapName) {

		String key = gameName + ":" + mapName;

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
    public boolean canMakeGame(String gameName, String mapName) {

		String key = gameName + ":" + mapName;

       	if(!queueList.containsKey(key)) {
    		return false;
    	}

       	//Removes any disconnected players
       	removeDisconnectedPlayers(gameName, mapName);
       	PlayerQueue targetGame = queueList.get(key);
       	
       	return targetGame.canMakeGame();
    }
    
    /**
     * Creates a list of players for a new game
     * @return null if there are not enough players or if the game queue does not exist,
     * 				otherwise it returns an ArrayList of Player instances
     */
    public ArrayList<Player> getPlayers(String gameName, String mapName) {

		String key = gameName + ":" + mapName;

       	if(!queueList.containsKey(key)) {
    		return null;
    	}
       	
       	if(canMakeGame(gameName, mapName)) {

	       	PlayerQueue targetGame = queueList.get(key);
	       	return targetGame.getPlayers();

       	}
       	
       	return null;
    }
    
}