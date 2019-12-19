package com.twostudentsllc.gladiator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.utils.Utils;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class TemplateCommand implements CustomCommand {
	
	private Main plugin;

	/**
	 * The name of the command that would be used to execute
	 */
	private String commandName = "TEMPLATE_COMMAND";
	/**
	 * The description of the command
	 */
	private String commandDescription = "";
	/**
	 * The correct usage of the command
	 */
	private String commandUsage = "";
	/**
	 * The player can run the command if they have any of these permissions
	 */
	private String[] commandPermissions;
	/**
	 * The minimum arguments the command must have to properly function
	 */
	private int minArgs = Integer.MIN_VALUE;
	/**
	 * The maximum arguments the command may have to properly function
	 */
	private int maxArgs = Integer.MAX_VALUE;
	/**
	 * If only the player should be able to execute this command
	 */
	private boolean onlyPlayer = true;
	
	public TemplateCommand(Main plugin)
	{
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
		
		//Write logic here
		Utils.commandCompletedMessage(sender, getName()); //Command completion confirmation
		return true;
	}
	
	
	//
	//Interface methods
	//
	public String getName()
	{
		return commandName;
	}
	
	public String getDescription()
	{
		return commandDescription;
	}
	
	public String getUsage()
	{
		return commandUsage;
	}
	
	public String[] getPermissions()
	{
		return commandPermissions;
	}
	
	public boolean canRunCommand(CommandSender sender)
	{
		if(getPermissions() == null)
			return true;
		for(String s : getPermissions())
		{
			if(sender.hasPermission(s))
				return true;
		}
		return false;
	}
	
	public boolean canRunCommand(Player sender)
	{
		if(getPermissions() == null)
			return true;
		for(String s : getPermissions())
		{
			if(sender.hasPermission(s))
				return true;
		}
		return false;
	}
	
	public int getMinArgs()
	{
		return minArgs;
	}
	
	public int getMaxArgs()
	{
		return maxArgs;
	}
	
	public boolean onlyPlayer() {
		return onlyPlayer;
	}
	

}
