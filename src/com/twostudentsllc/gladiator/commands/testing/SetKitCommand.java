package com.twostudentsllc.gladiator.commands.testing;

import java.util.List;

import com.twostudentsllc.gladiator.commands.AutoSuggest;
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
public class SetKitCommand extends CustomCommand {
	
	private Main plugin;
	
	public SetKitCommand(Main plugin)
	{
		
		super.setName("setkit")
			 .setDescription("Sets your inventory as a specific kit for a minigame")
			 .setUsage("/gladiator setkit <minigameName> <kitName>")
			 .setPermissions(new String[] {"gladiator.setkit"})
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
		plugin.getGameManager().getGame(minigameName).getInventoryManager().saveInventory((Player)sender, kitName);
	}

	public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

		return new AutoSuggest(plugin)
					.suggestGame()
					.suggestKit()
					.run(commandSender, command, s, args);

	}
	

}
