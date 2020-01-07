package com.twostudentsllc.gladiator;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.twostudentsllc.gladiator.commands.location.SetLocationCommand;
import com.twostudentsllc.gladiator.commands.location.TeleportToLocationCommand;
import com.twostudentsllc.gladiator.commands.maps.CreateMapCommand;
import com.twostudentsllc.gladiator.commands.misc.BossBarCommand;
import com.twostudentsllc.gladiator.commands.misc.HelpCommand;
import com.twostudentsllc.gladiator.commands.misc.ScoreboardCommand;
import com.twostudentsllc.gladiator.commands.testing.CreateWorldCommand;
import com.twostudentsllc.gladiator.commands.testing.DeleteWorldCommand;
import com.twostudentsllc.gladiator.commands.testing.GetKitCommand;
import com.twostudentsllc.gladiator.commands.testing.JoinCommand;
import com.twostudentsllc.gladiator.commands.testing.ListCommand;
import com.twostudentsllc.gladiator.commands.testing.RemoveClickableCommand;
import com.twostudentsllc.gladiator.commands.testing.SetClickableCommand;
import com.twostudentsllc.gladiator.commands.testing.SetKitCommand;
import com.twostudentsllc.gladiator.commands.testing.TeleportToWorld;
import com.twostudentsllc.gladiator.commands.testing.TestInventoryCommand;
import com.twostudentsllc.gladiator.games.Gladiator;
import com.twostudentsllc.gladiator.generic_classes.Vote;
import com.twostudentsllc.gladiator.generic_classes.VoteHost;
import com.twostudentsllc.gladiator.listeners.BlockClickedListener;
import com.twostudentsllc.gladiator.listeners.PlayerJoinListener;
import com.twostudentsllc.gladiator.managers.CommandManager;
import com.twostudentsllc.gladiator.managers.GameManager;
import com.twostudentsllc.gladiator.managers.MysqlManager;

/**
 * Gladiator plugin main class. Initializes the plugin. Copyright 2019 Casey
 * Puentes. All rights reserved.
 * 
 * @author Casey Puentes
 *
 */
public class Main extends JavaPlugin implements VoteHost {

	// Notes:
	// Game super class must have an unimplemented version of saveInformation() and
	// must have a list of all maps that it loops through, saving all custom info
	// Game super class must have saveInformation() and onShutdown() methods that
	// save all maps. MUST call GameMap's saveLocations() method when shutting down.
	// Also must have loadMaps() or something related to that and loadInformation()

	private MysqlManager sqlMan;
	private CommandManager cmdMan;
	private GameManager gameMan;
	private Vote curVote;

	private World hubWorld;

	@Override
	public void onEnable() {
		loadConfig(true);
		initializeManagers();
		initializeCommands();
		initializeGames();
		registerListeners();
		
		//Creating a test vote
		ArrayList<String> choices = new ArrayList<String>();
		
		choices.add("Choice1");
		choices.add("Choice2");
		choices.add("Choice3");
		
		Vote vote = new Vote(this, this, "TEST VOTING", choices, Material.PAPER, 15);
		curVote = vote;
		System.out.println("[GLADIATOR]: Successfully loaded!");


		//Insure that there is a main hub world that players will spawn in
		hubWorld = getServer().getWorld("world");
		if(hubWorld == null)
			throw new Error("No hub world defined!");
	}

	//PART OF VOTING TESTING
	public Inventory getVoteInventory()
	{
		return curVote.getVotingInventory();
	}
	//PART OF VOTING TESTING
	//PART OF VOTING TESTING
	public void votingFinished(String winner)
	{
		
	}
	//PART OF VOTING TESTING
	
	@Override
	public void onDisable() {
		gameMan.saveAllData();
		loadConfig(true);
		System.out.println("[GLADIATOR]: Successfully shut down!");
	}

	private void initializeManagers() {
		cmdMan = new CommandManager(this);
		gameMan = new GameManager(this);
		sqlMan = new MysqlManager(this);
	}

	private void initializeCommands() {
		new SetLocationCommand(this);
		new TeleportToLocationCommand(this);
		new HelpCommand(this);
		new CreateMapCommand(this);
		new SetClickableCommand(this);
		new RemoveClickableCommand(this);

		// TEST COMMANDS
		new JoinCommand(this);
		new TeleportToWorld(this);
		new DeleteWorldCommand(this);
		new ListCommand(this);
		new CreateWorldCommand(this);
		new SetKitCommand(this);
		new GetKitCommand(this);
		new BossBarCommand(this);
		new ScoreboardCommand(this);
		new TestInventoryCommand(this);
	}

	private void initializeGames() {
		new Gladiator(this, "gladiator", "Gladiator");
	}

	private void registerListeners() {
		new PlayerJoinListener(this);
		new BlockClickedListener(this);
	}

	private void loadConfig(boolean save) {
		getConfig().options().copyDefaults(true);
		if (save)
			saveConfig();
	}

	public CommandManager getCommandManager() {
		return cmdMan;
	}

	public GameManager getGameManager() {
		return gameMan;
	}

	public MysqlManager getMysqlManager() {
		return sqlMan;
	}

	public World getHubWorld() {
		return hubWorld;
	}
}
