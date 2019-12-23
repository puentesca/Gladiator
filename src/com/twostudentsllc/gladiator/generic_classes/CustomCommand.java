package com.twostudentsllc.gladiator.generic_classes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.global.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A common command super class
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public abstract class CustomCommand implements CommandExecutor {
	
	/**
	 * The name of the command that would be used to execute
	 */
	protected String commandName = "TEMPLATE_COMMAND";
	/**
	 * The description of the command
	 */
	protected String commandDescription = "";
	/**
	 * The correct usage of the command
	 */
	protected String commandUsage = "";
	/**
	 * The player can run the command if they have any of these permissions
	 */
	protected String[] commandPermissions;
	/**
	 * The minimum arguments the command must have to properly function
	 */
	protected int minArgs = Integer.MIN_VALUE;
	/**
	 * The maximum arguments the command may have to properly function
	 */
	protected int maxArgs = Integer.MAX_VALUE;
	/**
	 * If only the player should be able to execute this command
	 */
	protected boolean onlyPlayer = true;
	
	
	//Default constructor for setting up a class with only setters
	public CustomCommand() {}
	
	/**
	 * Initializes the CustomCommand with all necessary data
	 * @param name The name of the command that would be used to execute
	 * @param description The description of the command
	 * @param usage The correct usage of the command
	 * @param permissions The player can run the command if they have any of these permissions
	 * @param minArgs The minimum arguments the command must have to properly function
	 * @param maxArgs The maximum arguments the command may have to properly function
	 * @param onlyPlayer If only the player should be able to execute this command
	 */
	
	public CustomCommand(String name, String description, String usage, String[] permissions, int minArgs, int maxArgs, boolean onlyPlayer)
	{
		commandName = name;
		commandDescription = description;
		commandUsage = usage;
		commandPermissions = permissions;
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
		this.onlyPlayer = onlyPlayer;
	}
	
	/**
	 * Checks to see if the amount of arguments the sent command contains is valid.
	 * @return True if the amount of arguments is within the minArgs and maxArgs bounds (inclusive)
	 */
	public boolean hasValidArgumentAmount(String[] args)
	{
		return (args.length <= maxArgs && args.length >= minArgs);
	}
	
	public CustomCommand setName(String name) {
		commandName = name;
		return this;
	}
	
	public CustomCommand setDescription(String desc) {
		commandDescription = desc;
		return this;
	}
	
	public CustomCommand setUsage(String usage) {
		commandUsage = usage;
		return this;
	}
	
	public CustomCommand setPermissions(String[] perms) {
		commandPermissions = perms;
		return this;
	}
	
	public CustomCommand setMinArgs(int minArgs) {
		this.minArgs = minArgs;
		return this;
	}
	
	public CustomCommand setMaxArgs(int maxArgs) {
		this.maxArgs = maxArgs;
		return this;
	}
	
	public CustomCommand setPlayerOnly(boolean playerOnly) {
		this.onlyPlayer = playerOnly;
		return this;
	}
	
	/**
	 *  Gets the name of the command
	 * @return The name of the command
	 */
	public String getName()
	{
		return commandName;
	}
	
	/**
	 *  Gets the description of the command
	 * @return The description of the command
	 */
	public String getDescription()
	{
		return commandDescription;
	}
	
	/**
	 *  Gets the correct usage of the command
	 * @return The correct usage of the command
	 */
	public String getUsage()
	{
		return commandUsage;
	}
	
	/**
	 * Gets a commands required permissions. Having any of these permissions will allow the 
	 * command to be executed.
	 * @return All permissions that would allow the execution of the command
	 */
	public String[] getPermissions()
	{
		return commandPermissions;
	}
	
	/**
	 * Checks if the entity that called the command has permission to use it
	 * @param sender The sender of the command
	 * @return True if the sender has any permissions required to execute the command
	 */
	public boolean canRunCommand(CommandSender sender)
	{
		if(getPermissions() == null || sender.isOp())
			return true;
		for(String s : getPermissions())
		{
			if(sender.hasPermission(s))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if the Player that called the command has permission to use it
	 * @param sender The player who called the command
	 * @return True if the player has any permissions required to execute the command
	 */
	public boolean canRunCommand(Player sender)
	{
		if(getPermissions() == null || sender.isOp())
			return true;
		for(String s : getPermissions())
		{
			if(sender.hasPermission(s))
				return true;
		}
		return false;
	}

	/**
	 * Support for tabComplete (if you don't need autocomplete just don't override)
	 * @param commandSender person who sent command
	 * @param command command itself
	 * @param s
	 * @param args args
	 * @return List that contains suggested terms for that completion
	 */
	public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {
		return null;
	}
	
	/**
	 * Gets the minimum arguments the command must have to function
	 * @return The minimum arguments the command requires
	 */
	public int getMinArgs()
	{
		return minArgs;
	}
	
	/**
	 * Gets the maximum arguments the command can have to function
	 * @return The maximum arguments the command allows
	 */
	public int getMaxArgs()
	{
		return maxArgs;
	}
	
	/**
	 * Returns whether or not this command only allows a player to execute it
	 * @return True if only a player can execute this command
	 */
	public boolean onlyPlayer()
	{
		return onlyPlayer;
	}
	
}
