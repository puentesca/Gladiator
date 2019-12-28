package com.twostudentsllc.gladiator;

import java.io.IOException;

import com.twostudentsllc.gladiator.commands.misc.BossBarCommand;
import com.twostudentsllc.gladiator.commands.misc.ScoreboardCommand;
import com.twostudentsllc.gladiator.commands.testing.*;
import org.bukkit.plugin.java.JavaPlugin;

import com.twostudentsllc.gladiator.commands.location.SetLocationCommand;
import com.twostudentsllc.gladiator.commands.location.TeleportToLocationCommand;
import com.twostudentsllc.gladiator.commands.maps.CreateMapCommand;
import com.twostudentsllc.gladiator.commands.misc.HelpCommand;
import com.twostudentsllc.gladiator.games.Gladiator;
import com.twostudentsllc.gladiator.managers.CommandManager;
import com.twostudentsllc.gladiator.managers.GameManager;
import com.twostudentsllc.gladiator.managers.LocationManager;

/**
 * Gladiator plugin main class. Initializes the plugin.
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class Main extends JavaPlugin{
	
	//Notes:
	//Game super class must have an unimplemented version of saveInformation() and must have a list of all maps that it loops through, saving all custom info
	//Game super class must have saveInformation() and onShutdown() methods that save all maps. MUST call GameMap's saveLocations() method when shutting down.
	//Also must have loadMaps() or something related to that and loadInformation()
	
	
	private LocationManager locMan;
	private CommandManager cmdMan;
	private GameManager gameMan;
	@Override
	public void onEnable()
	{
		initializeManagers();
		initializeCommands();
		initializeGames();
		registerListeners();
		System.out.println("[GLADIATOR]: Successfully loaded!");
	}
	
	@Override
	public void onDisable()
	{
		gameMan.saveAllData();
		System.out.println("[GLADIATOR]: Successfully shut down!");
	}
	
	private void initializeManagers()
	{
		try {
			locMan = new LocationManager(this);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		cmdMan = new CommandManager(this);
		gameMan = new GameManager(this);
	}
	private void initializeCommands()
	{
		new SetLocationCommand(this);
		new TeleportToLocationCommand(this);
		new HelpCommand(this);
		new CreateMapCommand(this);
		
		//TEST COMMANDS
		new JoinCommand(this);
		new TeleportToWorld(this);
		new DeleteWorldCommand(this);
		new ListCommand(this);
		new CreateWorldCommand(this);
		new SetKitCommand(this);
		new GetKitCommand(this);
		new BossBarCommand(this);
		new ScoreboardCommand(this);
	}
	
	private void initializeGames()
	{
		new Gladiator(this, "gladiator", "Gladiator");
	}
	
	private void registerListeners()
	{
		
	}
	
	public LocationManager getLocationManager()
	{
		return locMan;
	}
	public CommandManager getCommandManager()
	{
		return cmdMan;
	}
	public GameManager getGameManager()
	{
		return gameMan;
	}
	
}
