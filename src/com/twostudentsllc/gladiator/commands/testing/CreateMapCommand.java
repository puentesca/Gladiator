package com.twostudentsllc.gladiator.commands.testing;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.generic_classes.Game;
import com.twostudentsllc.gladiator.global.Utils;

/**
 * A command
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class CreateMapCommand extends CustomCommand {
	
	private Main plugin;
	
	public CreateMapCommand(Main plugin)
	{
		
		super.setName("createmap")
			 .setDescription("Creates a new map")
			 .setUsage("/gladiator createmap <minigameName> <mapName> <mapDisplayName> <minTeams> <maxTeams> <minPlayers> <maxPlayers>")
			 .setPermissions(new String[] {"gladiator.createmap"})
			 .setMinArgs(8)
			 .setMaxArgs(8)
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
		Game game = plugin.getGameManager().getGame(minigameName);
		game.createGameMap(args);
	}
	

}
