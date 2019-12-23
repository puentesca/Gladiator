package com.twostudentsllc.gladiator.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A custom command manager that stores and calls all subcommands of /gladiator
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class CommandManager implements CommandExecutor {
	
	private HashMap<String, CustomCommand> subcommands;
	
	private String mainCommand = "gladiator";
	
	private Main plugin;
	
	public CommandManager(Main plugin)
	{
		this.plugin = plugin;
		subcommands = new HashMap<String, CustomCommand>();
		plugin.getCommand(mainCommand).setExecutor(this);
		plugin.getCommand(mainCommand).setTabCompleter(new MainCommandTabCompleter());
	}

	/**
	 * Class for handling tab autocompletion
	 */
	private class MainCommandTabCompleter implements TabCompleter {

		@Override
		public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

			if(args.length == 1){
				ArrayList<String> list = new ArrayList<>();
				for(String possibleCommand: subcommands.keySet()) {
					if(possibleCommand.contains(args[0])){
						list.add(possibleCommand);
					}
				}
				return list;
			}

			if(!subcommands.containsKey(args[0]))
				return null;

			//Remove the command name from the args passed in to the tabComplete function
			String[] truncatedArgs = new String[args.length-1];
			for(int i = 1 ; i < args.length; i++){
				truncatedArgs[i-1] = args[i];
			}


			//Match the command parts spelling as the sender types
			String stringToMatch = truncatedArgs[truncatedArgs.length - 1];
			List<String> possibleValues = subcommands.get(args[0]).tabComplete(commandSender, command, s, truncatedArgs);

			//Break out early if there are no possible values
			if(possibleValues == null)
				return null;

			ArrayList<String> matchValues = new ArrayList<>();

			for(String matchPart: possibleValues) {
				if(matchPart.contains(stringToMatch)) {
					matchValues.add(matchPart);
				}
			}

			return matchValues;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
				Utils.Error(sender, "You must be a player to execute this command!");
				return false;
			}
			//Verifies the entity has permissions to run the command
			if(!command.canRunCommand(sender))
			{
				Utils.Error((Player)sender, "You don't have permission to use this command!");
				return false;
			}
			//Verifies the amount of arguments passed in is valid
			if(!command.hasValidArgumentAmount(args))
			{
				Utils.Error((Player)sender, Utils.getUsageMessage(command.getUsage()));
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
