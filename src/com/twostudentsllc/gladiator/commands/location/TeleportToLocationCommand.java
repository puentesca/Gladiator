package com.twostudentsllc.gladiator.commands.location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;

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
		 .setPermissions(new String[0])
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
		//Execute command logic
		plugin.getLocationManager().teleportToLocation((Player)sender, args);
	}
	

}
