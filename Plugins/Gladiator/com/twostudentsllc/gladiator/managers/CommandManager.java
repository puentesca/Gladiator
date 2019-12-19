package com.twostudentsllc.gladiator.managers;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;

public class CommandManager implements CommandExecutor {
	
	private HashMap<String, CustomCommand> subcommands;
	
	private String mainCommand = "gladiator";
	
	private Main plugin;
	
	public CommandManager(Main plugin)
	{
		this.plugin = plugin;
		subcommands = new HashMap<String, CustomCommand>();
		plugin.getCommand(mainCommand).setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//If for some reason this is triggered by a different command
		if(!cmd.getName().equals(mainCommand))
			return false;
		
		//If there are no command arguments
		if(args == null || args.length == 0)
		{
			sender.sendMessage(Utils.chatMessage("Welcome to the Gladiator Plugin! Type '/gladiator help' for help.", true));
			return true;
		}
		
		String key = args[0];
		
		System.out.println("Checking for command: " + key);
		//If the command is registered, execute it.
		if(subcommands.containsKey(key))
		{
			System.out.println("Found command: " + key);
			CustomCommand command = subcommands.get(key);
			//Verifies that, if a player is required to execute the command, only the player is able to execute it.
			if(command.onlyPlayer() && !(sender instanceof Player))
			{
				Utils.Error(sender, "You must be a player to exectue this command!");
			}
			//Verifies the entity has permissions to run the command
			if(!command.canRunCommand(sender))
			{
				Utils.Error((Player)sender, "You don't have permission to use this command!");
				return false;
			}
			subcommands.get(key).onCommand(sender, cmd, label, args);
		}
		
		return false;
	}
	
	
	/**
	 * Registers a new subcommand
	 * @param name The name of the command, also the key.
	 * @param cmdEx The class to execute the command
	 */
	public void registerCommand(String name, CustomCommand cmdEx)
	{
		//If its the template command
		if(name.equals("TEMPLATE_COMMAND"))
			return;
		
		if(subcommands.containsKey(name))
		{
			System.out.println("Command '" + name + "' already registered!");
			return;
		}
		
		subcommands.put(name, cmdEx);
		System.out.println("Registered command: " + name);
	}
	
	/**
	 * Gets the hashmap containing all registered commands
	 * @return The hashmap of registered commands.
	 */
	public HashMap<String, CustomCommand> getRegisteredCommands()
	{
		return subcommands;
	}
	
}
