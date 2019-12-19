package com.twostudentsllc.gladiator.commands.location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;

/**
 * Command that teleports a player to a spawnpoint
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class TeleportToLocationCommand extends CustomCommand{
	
	private Main plugin;

	/**
	 * The name of the command that would be used to execute
	 */
	private String commandName = "teleporttolocation";
	/**
	 * The description of the command
	 */
	private String commandDescription = "Teleports a player to a specified spawn location";
	/**
	 * The correct usage of the command
	 */
	private String commandUsage = "/gladiator teleporttolocation <locationkey>";
	/**
	 * The player can run the command if they have any of these permissions
	 */
	private String[] commandPermissions;
	/**
	 * The minimum arguments the command must have to properly function
	 */
	private int minArgs = 2;
	/**
	 * The maximum arguments the command may have to properly function
	 */
	private int maxArgs = 2;
	/**
	 * If only the player should be able to execute this command
	 */
	private boolean onlyPlayer = true;
	
	public TeleportToLocationCommand(Main plugin)
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
		plugin.getLocationManager().teleportToLocation((Player)sender, args[1]);
	}
	

}
