package com.twostudentsllc.gladiator;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.twostudentsllc.gladiator.arenas.ArenaRound;
import com.twostudentsllc.gladiator.commands.location.SetLocationCommand;
import com.twostudentsllc.gladiator.commands.location.TeleportToLocationCommand;
import com.twostudentsllc.gladiator.commands.misc.HelpCommand;
import com.twostudentsllc.gladiator.generic_classes.MapRound;
import com.twostudentsllc.gladiator.managers.CommandManager;
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
	
	@Override
	public void onEnable()
	{
		initializeManagers();
		initialzeCommands();
		registerListeners();
		System.out.println("[GLADIATOR]: Successfully loaded!");
	}
	
	@Override
	public void onDisable()
	{
		//TODO: Convert to calling onShutdown() method on all games in the gamemanager or something along these lines
//		try {
//			locMan.saveLocationFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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
	}
	private void initialzeCommands()
	{
		new SetLocationCommand(this);
		new TeleportToLocationCommand(this);
		new HelpCommand(this);
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
	
}
