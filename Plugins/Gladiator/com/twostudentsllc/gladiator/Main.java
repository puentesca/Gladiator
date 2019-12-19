package com.twostudentsllc.gladiator;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.twostudentsllc.gladiator.arenas.ArenaRound;
import com.twostudentsllc.gladiator.arenas.GladiatorRound;
import com.twostudentsllc.gladiator.commands.location.SetLocationCommand;
import com.twostudentsllc.gladiator.commands.location.TeleportToLocationCommand;
import com.twostudentsllc.gladiator.commands.misc.HelpCommand;
import com.twostudentsllc.gladiator.managers.CommandManager;
import com.twostudentsllc.gladiator.managers.LocationManager;

/**
 * Gladiator plugin main class. Initializes the plugin.
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class Main extends JavaPlugin{
	
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
		try {
			locMan.saveLocationFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
