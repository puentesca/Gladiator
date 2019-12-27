package com.twostudentsllc.gladiator.commands.testing;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.generic_classes.Team;
import com.twostudentsllc.gladiator.global.Utils;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class GetKitCommand extends CustomCommand {
	
	private Main plugin;
	
	public GetKitCommand(Main plugin)
	{
		
		super.setName("getkit")
			 .setDescription("Sets your inventory as to specific kit for a minigame")
			 .setUsage("/gladiator getkit <minigameName> <kitName>")
			 .setPermissions(new String[] {"gladiator.getkit"})
			 .setMinArgs(3)
			 .setMaxArgs(3)
			 .setPlayerOnly(true);
		
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
		String minigameName = args[1];
		String kitName = args[2];
		plugin.getGameManager().getGame(minigameName).getInventoryManager().setPlayerInventory((Player)sender, kitName);
	}
	

}
