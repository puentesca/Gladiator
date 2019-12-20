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
	
	public SetLocationCommand(Main plugin)
	{
		super.setName("setlocation")
			 .setDescription("Sets the location of a minigames map using a key")
			 .setUsage("/gladiator setlocation <minigameName> <mapName> <locationKey>")
			 .setPermissions(new String[] {"gladiator.setlocations", "gladiator.admin"})
			 .setMinArgs(4)
			 .setMaxArgs(4)
			 .setPlayerOnly(true);
		
		this.plugin = plugin;
		plugin.getCommandManager().registerCommand(getName(), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
