package com.twostudentsllc.gladiator.commands.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.twostudentsllc.gladiator.generic_classes.Game;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.managers.GameManager;
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

	public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

		if(args.length == 1) {

			ArrayList<String> list = new ArrayList<>();
			list.addAll(plugin.getGameManager().getGames().keySet());
			return list;

		} else if (args.length == 2) {

			//Suggest the games if they are looking for maps or locations
			GameManager manager = plugin.getGameManager();
			Game targetGame = manager.getGame(args[0]);

			if(targetGame==null)
				return null;

			ArrayList<String> list = new ArrayList<>();
			list.addAll(targetGame.getInventoryManager().listInventories());

			return list;

		}

		return null;
	}
	

}
