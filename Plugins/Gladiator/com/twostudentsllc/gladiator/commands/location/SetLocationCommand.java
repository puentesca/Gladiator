package com.twostudentsllc.gladiator.commands.location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;

public class SetLocationCommand implements CustomCommand {
	
	private Main plugin;

	/**
	 * The name of the command that would be used to execute
	 */
	private String commandName = "setlocation";
	/**
	 * The description of the command
	 */
	private String commandDescription = "Sets the location of a spawnpoint using a key";
	/**
	 * The correct usage of the command
	 */
	private String commandUsage = "/gladiator setlocation <locationkey>";
	/**
	 * The player can run the command if they have any of these permissions
	 */
	private String[] commandPermissions = {"gladiator.setlocations", "gladiator.admin"};
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
	
	public SetLocationCommand(Main plugin)
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
		
		//If the command sent is the command listed here
		if(commandName.equals(getName()))
		{
			plugin.getLocationManager().setLocation(((Player)sender), args[1], ((Player)sender).getLocation());
			Utils.commandCompletedMessage(sender, getName());
			return true;
		}
		else
		{
			Utils.Error((Player)sender, Utils.getUsageMessage(getUsage()));
		}
		return false;
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
