package com.twostudentsllc.gladiator.commands.testing;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class DeleteWorldCommand extends CustomCommand {
	
	private Main plugin;
	
	public DeleteWorldCommand(Main plugin)
	{
		
		super.setName("deleteworld")
			 .setDescription("Deletes a world")
			 .setUsage("/gladiator deleteworld <worldName>")
			 .setPermissions(new String[] {"gladiator.deleteworlds"})
			 .setMinArgs(2)
			 .setMaxArgs(2)
			 .setPlayerOnly(false);
		
		//Initializes data
		this.plugin = plugin;
		plugin.getCommandManager().registerCommand(getName(), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
		String worldName = args[1];
		Bukkit.unloadWorld(worldName, false);
		try {
			FileUtils.deleteDirectory(new File(worldName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
