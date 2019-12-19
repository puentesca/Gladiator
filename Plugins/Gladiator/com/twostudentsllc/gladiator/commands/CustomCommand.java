package com.twostudentsllc.gladiator.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A common command interface
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public interface CustomCommand extends CommandExecutor {
	
	/**
	 *  Gets the name of the command
	 * @return The name of the command
	 */
	public String getName();
	
	/**
	 *  Gets the description of the command
	 * @return The description of the command
	 */
	public String getDescription();
	
	/**
	 *  Gets the correct usage of the command
	 * @return The correct usage of the command
	 */
	public String getUsage();
	
	/**
	 * Gets a commands required permissions. Having any of these permissions will allow the 
	 * command to be executed.
	 * @return All permissions that would allow the execution of the command
	 */
	public String[] getPermissions();
	
	/**
	 * Checks if the entity that called the command has permission to use it
	 * @param sender The sender of the command
	 * @return True if the sender has any permissions required to execute the command
	 */
	public boolean canRunCommand(CommandSender sender);
	
	/**
	 * Checks if the Player that called the command has permission to use it
	 * @param sender The player who called the command
	 * @return True if the player has any permissions required to execute the command
	 */
	public boolean canRunCommand(Player sender);
	
	/**
	 * Gets the minimum arguments the command must have to function
	 * @return The minimum arguments the command requires
	 */
	public int getMinArgs();
	
	/**
	 * Gets the maximum arguments the command can have to function
	 * @return The maximum arguments the command allows
	 */
	public int getMaxArgs();
	
	/**
	 * Returns whether or not this command only allows a player to execute it
	 * @return True if only a player can execute this command
	 */
	public boolean onlyPlayer();
	
}
