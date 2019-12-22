package com.twostudentsllc.gladiator.managers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.bukkit.entity.Player;

public class QueueManager {

	//TODO: Group queues
	//TODO: Some way to distignuish the different maps along with minigame

	private Map<String, PlayerQueue> queueList;
	
	private class PlayerQueue {
		
		private int playersNeeded;
		private Queue<Player> playerList;
		
		public PlayerQueue(int playersNeeded, Queue<Player> playerList) {
			this.playersNeeded = playersNeeded;
			this.playerList = playerList;
		}
	}
	
    public QueueManager() {
    	queueList = new TreeMap<>();
    }
    
    /*
     * Adds player to a game queue
     * @return boolean whether the player was successfully added
     */
    public boolean addPlayerToQueue(String gameName, Player toAdd) {
    	if(!queueList.containsKey(gameName)) {
    		return false;
    	}
    	
    	if(queueList.get(gameName).playerList.contains(toAdd))
    		return false;
    	
    	queueList.get(gameName).playerList.add(toAdd);
    	return true;
    }
    
    /*
     * Removes player from game queue
     * @return boolean whether the player was successfully removed
     */
    public boolean removePlayerFromQueue(String gameName, Player toRemove) {
    	
    	if(!queueList.containsKey(gameName)) {
    		return false;
    	}
    	
    	return queueList.get(gameName).playerList.remove(toRemove);
    }
    
    /*
     * Creates a game queue with some base requirement of players per game
     * @return boolean whether the player was successfully removed
     */
    public boolean addGameQueue(String gameName, int playersNeeded) {
    	if(queueList.containsKey(gameName)) {
    		return false;
    	}
    	
    	queueList.put(gameName, new PlayerQueue(playersNeeded, new LinkedList<Player>()));
    	return true;
    }

    public boolean removeGameQueue(String gameName) {
    	if(!queueList.containsKey(gameName))
    		return false;

    	queueList.remove(gameName);
    	return true;
	}
    
    
    /*
     * Removes all disconnected players from queue
     */
    public void removeDisconnectedPlayers(String gameName) {
    	if(!queueList.containsKey(gameName)) {
    		return;
    	}
    	
    	PlayerQueue targetQueue =  queueList.get(gameName);
    	
    	for(Player player: targetQueue.playerList) {
    		if(!player.isOnline()) {
    			targetQueue.playerList.remove(player);
    		}
    	}
    	
    }
    
    /*
     * Checks if you have enough players to make a new game
     * @return boolean if there are enough players to make new game
     */
    public boolean canMakeGame(String gameName) {
       	if(!queueList.containsKey(gameName)) {
    		return false;
    	}
       	
       	removeDisconnectedPlayers(gameName);
       	
       	PlayerQueue targetGame = queueList.get(gameName);
       	
       	return targetGame.playerList.size() >= targetGame.playersNeeded;
    }
    
    /*
     * Creates a list of players for a new game
     * @return null if there are not enough players or if the game queue does not exist,
     * @return otherwise it returns an ArrayList of Player instances
     */
    public ArrayList<Player> getPlayers(String gameName) {
       	if(!queueList.containsKey(gameName)) {
    		return null;
    	}
       	
       	if(canMakeGame(gameName)) {
	       	
	       	PlayerQueue targetGame = queueList.get(gameName);
	       	
	       	ArrayList<Player> toReturn = new ArrayList<Player>();
	       	
	       	//Gets required number of players from the playerList for that queue
	       	for(int i = 0; i < targetGame.playersNeeded; i++)
	       		toReturn.add(targetGame.playerList.poll());
	       	
	       	return toReturn;
	       	
       	}
       	
       	return null;
    }
    
}