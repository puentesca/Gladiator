package com.twostudentsllc.gladiator.commands.location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;
/**
 * Command that allows admins to set the location of spawnpoints
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class SetLocationCommand extends CustomCommand{
	
	private Main plugin;

	/**
	 * The name of the command that would be used to execute
	 */
	private String commandName = "setlocation";
	/**
	 * The description of the command
	 */
	private String commandDescription = "Sets the location of a minigames map using a key";
	/**
	 * The correct usage of the command
	 */
	private String commandUsage = "/gladiator setlocation <minigameName> <mapName> <locationKey>";
	/**
	 * The player can run the command if they have any of these permissions
	 */
	private String[] commandPermissions = {"gladiator.setlocations", "gladiator.admin"};
	/**
	 * The minimum arguments the command must have to properly function
	 */
	private int minArgs = 4;
	/**
	 * The maximum arguments the command may have to properly function
	 */
	private int maxArgs = 4;
	/**
	 * If only the player should be able to execute this command
	 */
	private boolean onlyPlayer = true;
	
	public SetLocationCommand(Main plugin)
	{
		super.initializeData(commandName, commandDescription, commandUsage, commandPermissions, minArgs, maxArgs, onlyPlayer);
		this.plugin = plugin;
		plugin.getCommandManager().registerCommand(getName(), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		//If the amount of arguments is invalid
		if(args.length > maxArgs || args.length < minArgs)
		{
			Utils.Error((Player)sender, Utils.getUsageMessage(getUsage()));
			return false;
		}
		
		String commandName = args[0];
		
		//Signifies the command has been called
		commandCalled(sender, cmd, label, args);
		Utils.commandCompletedMessage(sender, getName());
		return true;
	}
	
	/**
	 * Called when the command listed is called
	 */
	private void commandCalled(CommandSender sender, Command cmd, String label, String[] args)
	{
		//Execute command logic
		plugin.getLocationManager().setLocation(((Player)sender), args, ((Player)sender).getLocation());
	}
	

}