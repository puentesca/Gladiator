package com.twostudentsllc.gladiator.commands.location;

import com.twostudentsllc.gladiator.global.AutoSuggest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;

import java.util.List;

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
		Player p = (Player)sender;

		String minigameName = args[1];
		String mapName = args[2];
		String key = args[3];
		System.out.println("Setting location for '" + key + "' for map '" + mapName + "' and minigame '" + minigameName + "'.");

		//Handle input argument validation
		if(!LocationCommandHelper.validateArguments(plugin, p, args))
			return;

		//Gets the game from the game manager, then gets the GameMap from the game, and adds the location.
		//Will throw IllegalArgumentException if any of them do not exist
		plugin.getGameManager().getGame(minigameName).getGameMap(mapName).addLocation(key, p.getLocation());
		System.out.println("Location '" + key + "' for map '" + mapName + "' and minigame '" + minigameName + "' successfully set.");
	}

	public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

		return new AutoSuggest(plugin)
				.suggestGame()
				.suggestMap()
				.suggestLocation()
				.run(commandSender, command, s, args);
	}
	

}
