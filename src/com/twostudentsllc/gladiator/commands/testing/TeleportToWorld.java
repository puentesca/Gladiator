package com.twostudentsllc.gladiator.commands.testing;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class TeleportToWorld extends CustomCommand {
	
	private Main plugin;
	
	public TeleportToWorld(Main plugin)
	{
		
		super.setName("teleporttoworld")
			 .setDescription("Teleports you to a world")
			 .setUsage("/gladiator teleporttoworld <worldname> <x> <y> <z>")
			 .setPermissions(new String[] {"gladiator.template"})
			 .setMinArgs(5)
			 .setMaxArgs(5)
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
		String worldName = args[1];
		int x = Integer.parseInt(args[2]);
		int y = Integer.parseInt(args[3]);
		int z = Integer.parseInt(args[4]);
		Location l = new Location(Bukkit.getWorld(worldName),x,y,z);
		p.teleport(l);
	}
	

}
