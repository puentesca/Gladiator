package com.twostudentsllc.gladiator.commands.testing;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;
import com.twostudentsllc.gladiator.global_managers.ClickableBlockManager;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class RemoveClickableCommand extends CustomCommand {
	
	private Main plugin;
	
	public RemoveClickableCommand(Main plugin)
	{
		
		super.setName("removeclickable")
			 .setDescription("Deletes the clickable functionality of thethe block you're looking at")
			 .setUsage("/gladiator removeclickable")
			 .setPermissions(new String[] {"gladiator.removeclickable"})
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
		Player p = (Player) sender;
		Block blockClicked = Utils.getTargetBlock(p, 100);
		
		ClickableBlockManager clickMan = plugin.getGameManager().getClickableBlockManager();
		clickMan.removeClickableBlock(blockClicked.getLocation());
		System.out.println("Clickable block removed at location: " + blockClicked.getLocation());
	}
	

}
