package com.twostudentsllc.gladiator.commands.misc;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;
/**
 * A command that displays all necessary help information
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class HelpCommand implements CustomCommand {
	
	private Main plugin;

	/**
	 * The name of the command that would be used to execute
	 */
	private String commandName = "help";
	/**
	 * The description of the command
	 */
	private String commandDescription = "Provides you with all types of good information!";
	/**
	 * The correct usage of the command
	 */
	private String commandUsage = "/gladiator help";
	/**
	 * The player can run the command if they have any of these permissions
	 */
	private String[] commandPermissions;
	/**
	 * The minimum arguments the command must have to properly function
	 */
	private int minArgs = 1;
	/**
	 * The maximum arguments the command may have to properly function
	 */
	private int maxArgs = 1;
	/**
	 * If only the player should be able to execute this command
	 */
	private boolean onlyPlayer = false;
	
	public HelpCommand(Main plugin)
	{
		this.plugin = plugin;
		plugin.getCommandManager().registerCommand(getName(), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length > maxArgs || args.length < minArgs)
		{
			Utils.Error((Player)sender, Utils.getUsageMessage(getUsage()));
			return false;
		}
		
		String commandName = args[0];
		
		gladiatorHelp(sender);
		
		return false;
	}
	
	/**
	 * Default gladiator command. Sends message to user informing them of help command.
	 * @param sender The exector of the command.
	 */
	private void gladiatorHelp(CommandSender sender)
	{
		String msg = Utils.getGladiatorHeader() + "&bGladiator help section\n";
		HashMap<String, CustomCommand> commands = plugin.getCommandManager().getRegisteredCommands();
		int lineNum = 1;
		for(String s : commands.keySet())
		{
			CustomCommand cmd = commands.get(s);
			msg += Utils.boxText("&b" + lineNum, "&8") + " &b" + cmd.getUsage() + ":&f " + cmd.getDescription() + "\n";
			lineNum++;
		}
		sender.sendMessage(Utils.chatMessage(msg, false));
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
