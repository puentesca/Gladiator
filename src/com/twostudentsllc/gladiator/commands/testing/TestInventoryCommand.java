package com.twostudentsllc.gladiator.commands.testing;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class TestInventoryCommand extends CustomCommand {
	
	private Main plugin;
	
	public TestInventoryCommand(Main plugin)
	{
		
		super.setName("testinventory")
			 .setDescription("Opens the testing GUI inventory")
			 .setUsage("/gladiator testinventory")
			 .setPermissions(new String[] {"gladiator.testinventory"})
			 .setMinArgs(1)
			 .setMaxArgs(1)
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
		((Player)sender).openInventory(plugin.getVoteInventory());

		//((Player)sender).openInventory(plugin.getGameManager().getMinigameChoosingInventory());
	}
	

}
