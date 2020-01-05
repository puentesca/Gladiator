package com.twostudentsllc.gladiator.commands.testing;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;
import com.twostudentsllc.gladiator.managers.ClickableBlockManager;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class SetClickableCommand extends CustomCommand {
	
	private Main plugin;
	
	public SetClickableCommand(Main plugin)
	{
		
		super.setName("setclickable")
			 .setDescription("Sets a the block you're looking at to execute a command when a player clicks it. Up to 25 arguments!")
			 .setUsage("/gladiator setclickable <command>")
			 .setPermissions(new String[] {"gladiator.setclickable"})
			 .setMinArgs(2)
			 .setMaxArgs(27)
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
		String command = "";
		for(int i = 1; i < args.length; i++)
		{
			if(i != 1)
				command += " ";
			command += args[i];
		}
		System.out.println("Saved command: " + command);
		
		ClickableBlockManager clickMan = plugin.getGameManager().getClickableBlockManager();
		clickMan.addClickableBlock(blockClicked.getLocation(), command);
		System.out.println("Clickable block added at location: " + blockClicked.getLocation() + " with command: '" + command + "'");
	}
	

}
