package com.twostudentsllc.gladiator.managers;

import com.twostudentsllc.gladiator.supers.Game;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class GameManager {

    private HashMap<String, Game> games;
    private QueueManager queues;

    public GameManager() {
        games = new HashMap<>();
        queues = new QueueManager();
    }

    public boolean addGame(String gameName, Game game, boolean createQueue) {
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

    public Game removeGame(String gameName) {
        if(!games.containsKey(gameName))
            return null;

        deleteGameQueue(gameName);

        return games.remove(gameName);
    }

    public boolean createGameQueue(Game game) {
        //TODO: Figure out how whether GameMap tracks # of players needed or Game
       return queues.addGameQueue(game.getGameName(),10);
    }

    public boolean deleteGameQueue(String gameName) {
        return queues.removeGameQueue(gameName);
    }

    public boolean addPlayerToQueue(String gameName, Player toAdd) {
        if(!games.containsKey(gameName))
            return false;

        return queues.addPlayerToQueue(gameName, toAdd);
    }

    public boolean removePlayerFromQueue(String gameName, Player toAdd) {
        if(!games.containsKey(gameName))
            return false;

        return queues.removePlayerFromQueue(gameName, toAdd);
    }

    public boolean canStartGame(String gameName) {
        if(!games.containsKey(gameName))
            return false;

        //TODO: More checks to insure that game is in fact able to start?
        Game targetGame = games.get(gameName);

        //Check if there are maps that players can be put into
        if(!targetGame.hasOpenMaps()) {
            return false;
        }

        return queues.canMakeGame(gameName);
    }

    public void startGame(String gameName) {
        if(!games.containsKey(gameName))
            return;

        if(canStartGame(gameName)) {
            //TODO: Implement start logic
            ArrayList<Player> players = queues.getPlayers(gameName);

        }
    }

}
