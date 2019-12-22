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
public class JoinCommand extends CustomCommand {
	
	private Main plugin;
	
	public JoinCommand(Main plugin)
	{
		
		super.setName("join")
			 .setDescription("Joins a queue for a minigames map")
			 .setUsage("/gladiator join <minigameName> <mapName>")
			 .setPermissions(new String[] {"gladiator.join"})
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
		String mapName = args[2];
		ArrayList<Player> p = new ArrayList<Player>();
		p.add((Player)sender);
		ArrayList<Team> team = Utils.assignTeams(1, p);
		plugin.getGameManager().getGame(minigameName).getGameMap(mapName).startMatch(team);
	}
	

}
