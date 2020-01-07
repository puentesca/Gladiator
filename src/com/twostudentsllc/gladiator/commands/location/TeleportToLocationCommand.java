package com.twostudentsllc.gladiator.commands.location;

import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.global.AutoSuggest;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;

import java.util.List;

/**
 * Command that teleports a player to a spawnpoint
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class TeleportToLocationCommand extends CustomCommand{
	
	private Main plugin;

	public TeleportToLocationCommand(Main plugin)
	{
		super.setName("teleporttolocation")
		 .setDescription("Teleports a player to a specified spawn location")
		 .setUsage("/gladiator teleporttolocation <minigameName> <mapName> <locationKey>")
		 .setPermissions(new String[] {"minigames.teleporttolocation"})
		 .setMinArgs(4)
		 .setMaxArgs(4)
		 .setPlayerOnly(true);
		
		this.plugin = plugin;
		plugin.getCommandManager().registerCommand(getName(), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String commandName = args[0];
		
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

		//Handle input argument validation
		if(!LocationCommandHelper.validateArguments(plugin, p, args))
			return;

		GameMap targetMap = plugin.getGameManager().getGame(minigameName).getGameMap(mapName);

		//Check if the location exists before trying to teleport the player
		Location targetLoc = targetMap.getLocation(key);
		if(targetLoc == null) {
			Utils.Error(p, key + " does not exist as a stored location for " + targetMap.getMapDisplayName());
			return;
		}

		p.teleport(plugin.getGameManager().getGame(minigameName).getGameMap(mapName).getLocation(key));
	}

	public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

		return new AutoSuggest(plugin)
				.suggestGame()
				.suggestMap()
				.suggestLocation()
				.run(commandSender, command, s, args);
	}
	

}
