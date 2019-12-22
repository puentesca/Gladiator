package com.twostudentsllc.gladiator.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Game;

public class GameManager {

	//TODO: Verify on shutdown that all appropriate saving methods are called
	
    private HashMap<String, Game> games;
    private QueueManager queues;
    
    private Main plugin;

    public GameManager(Main plugin) {
    	this.plugin = plugin;
        games = new HashMap<>();
        queues = new QueueManager();
    }
    
    public boolean registerGame(String gameName, Game game, boolean createQueue) {
        if(games.containsKey(gameName))
            return false;

        if(createQueue) {
            boolean createdGameQueue = createGameQueue(game);
            if(createdGameQueue)
                System.out.println("Created game queue for " + game.getDisplayName());
            else
                System.out.println("Failed to create game queue for " + game.getDisplayName());
        }

        games.put(gameName, game);
        return true;
    }

    public Game getGame(String gameName)
    {
    	if(!hasGame(gameName))
    		throw new IllegalArgumentException("Game '" + gameName + "' does not exist!");
    	return games.get(gameName);
    }
    
    /**
     * Attemps to save all maps and data
     */
    public void saveAllData()
    {
    	for(String s : games.keySet())
    	{
    		Game g = games.get(s);
    		if(g.saveAllData())
    		{
    			System.out.println("Game '" + g.getGameName() + "' saved all data successfully!");
    		}
    		else
    		{
    			System.out.println("Error when attempting to save data in game '" + g.getGameName() + "'!");
    		}
    	}
    }
    
    /**
     * Evaluates whether or not the game exists
     * @param gameName The name of the game (not display name)
     * @return True if the game exists
     */
    public boolean hasGame(String gameName)
    {
    	return games.containsKey(gameName);
    }
    

    //FIXME: Change methods to work with new QueueManager
    //FIXME: Figure out whether or not the methods should be here or in game
//    public Game removeGame(String gameName) {
//        if(!games.containsKey(gameName))
//            return null;
//
//        deleteGameQueue(gameName);
//
//        return games.remove(gameName);
//    }
    public boolean createGameQueue(Game game) {
        //TODO: Figure out how whether GameMap tracks # of players needed or Game
    	return false;
    	//FIXME: Adjust this so that it create a correct type of new queue
       //return queues.addGameQueue(game.getGameName(),10);
    }
//
//    public boolean deleteGameQueue(String gameName) {
//        return queues.removeGameQueue(gameName);
//    }
//
//    public boolean addPlayerToQueue(String gameName, Player toAdd) {
//        if(!games.containsKey(gameName))
//            return false;
//
//        return queues.addPlayerToQueue(gameName, toAdd);
//    }
//
//    public boolean removePlayerFromQueue(String gameName, Player toAdd) {
//        if(!games.containsKey(gameName))
//            return false;
//
//        return queues.removePlayerFromQueue(gameName, toAdd);
//    }
//
//    public boolean canStartGame(String gameName) {
//        if(!games.containsKey(gameName))
//            return false;
//
//        //TODO: More checks to insure that game is in fact able to start?
//        Game targetGame = games.get(gameName);
//
//        //Check if there are maps that players can be put into
//        if(!targetGame.hasOpenMaps()) {
//            return false;
//        }
//
//        return queues.canMakeGame(gameName);
//    }
//
//    public void startGame(String gameName) {
//        if(!games.containsKey(gameName))
//            return;
//
//        if(canStartGame(gameName)) {
//            //TODO: Implement start logic
//            ArrayList<Player> players = queues.getPlayers(gameName);
//
//        }
//    }

}
