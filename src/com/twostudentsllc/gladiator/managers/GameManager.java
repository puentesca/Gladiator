package com.twostudentsllc.gladiator.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.GUIInventory;
import com.twostudentsllc.gladiator.generic_classes.Game;
import com.twostudentsllc.gladiator.generic_classes.InventoryItem;
import com.twostudentsllc.gladiator.generic_classes.InventoryRunnable;
import com.twostudentsllc.gladiator.runnables.inventory.GladiatorJoinInventoryRunnable;

public class GameManager {
	
    private HashMap<String, Game> games;

    private Main plugin;
    
    private ClickableBlockManager clickableBlocks;

    private GUIInventory chooseGameUI;
    
    public GameManager(Main plugin) {
    	this.plugin = plugin;
        games = new HashMap<>();
        clickableBlocks = new ClickableBlockManager(plugin);
        createChooseGameUI();
    }
    
    /**
     * Creates the GUI for whenever players want to choose a minigame to join
     */
    public void createChooseGameUI()
    {
    	HashMap<InventoryRunnable, InventoryItem> inventoryStuff = new HashMap<InventoryRunnable, InventoryItem>();
    	
    	InventoryRunnable gladiatorJoinRunnable = new GladiatorJoinInventoryRunnable(plugin);
    	
    	ArrayList<String> gladiatorLore = new ArrayList<String>();
    	gladiatorLore.add("FIGHT TO THE DEATH AND WIN YOUR FREEDOM");
    	gladiatorLore.add("GOOD LUCK WARRIOR!");
    	InventoryItem gladiatorItem = new InventoryItem(plugin, Material.SHIELD, "GLADIATOR", gladiatorLore, 1, true, 1);
    	inventoryStuff.put(gladiatorJoinRunnable, gladiatorItem);
    	
    	chooseGameUI = new GUIInventory(plugin, "MINIGAMES", inventoryStuff, 3);
    }
    
    public boolean registerGame(String gameName, Game game) {
        if(games.containsKey(gameName))
            return false;

        games.put(gameName, game);
        return true;
    }

    public Game getGame(String gameName)
    {
        if(!hasGame(gameName))
            return null;
    	/*if(!hasGame(gameName))
    		throw new IllegalArgumentException("Game '" + gameName + "' does not exist!");*/
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
    public Game removeGame(String gameName) {
        if(!games.containsKey(gameName))
            return null;
        return games.remove(gameName);
    }


    public boolean startGame(String gameName, String mapName) {
        if(!games.containsKey(gameName))
            return false;

        Game targetGame = games.get(gameName);

        //TODO: Implement start logic
        return targetGame.startGame(mapName);
    }


    /**
     * Returns a list of all the games
     * @return a hashmap containing games
     */
    public HashMap<String, Game> getGames() {
        return games;
    }
    
    public ClickableBlockManager getClickableBlockManager()
    {
    	return clickableBlocks;
    }
    
    public Inventory getMinigameChoosingInventory()
    {
    	return chooseGameUI.getInventory();
    }
}
