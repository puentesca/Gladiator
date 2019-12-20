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
public class TemplateCommand extends CustomCommand {
	
	private Main plugin;
<<<<<<< HEAD
=======
	/**
	 * The name of the command that would be used to execute
	 */
	private String commandName = "TEMPLATE_COMMAND";
	/**
	 * The description of the command
	 */
	protected String commandDescription = "";
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
>>>>>>> 25da9ed5ab5cb7b8cb001214bcf581907a0781ad
	
	public TemplateCommand(Main plugin)
	{
		
		super.setName("template")
			 .setDescription("Does template stuff")
			 .setUsage("/template")
			 .setPermissions(new String[] {"template.stuff"})
			 .setMinArgs(0)
			 .setMaxArgs(1)
			 .setPlayerOnly(false);
		
		//Initializes data
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
		
		Utils.commandCompletedMessage(sender, getName()); //Command completion confirmation
		return true;
	}
	
	/**
	 * Called when the command listed is called
	 */
	private void commandCalled(CommandSender sender, Command cmd, String label, String[] args)
	{
		
	}
	

}
