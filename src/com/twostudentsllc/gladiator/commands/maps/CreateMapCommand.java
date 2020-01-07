package com.twostudentsllc.gladiator.commands.maps;

import com.twostudentsllc.gladiator.commands.AutoSuggest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.generic_game.handlers.Game;
import com.twostudentsllc.gladiator.utils.Utils;

import java.util.List;

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
			 .setUsage("/gladiator createmap <minigameName> <mapName> <mapDisplayName> <minTeams> <maxTeams> <teamSize> <warmupTime> <cooldownTime> <totalRounds>")
			 .setPermissions(new String[] {"gladiator.createmap"})
			 .setMinArgs(10)
			 .setMaxArgs(10)
			 .setPlayerOnly(false);
		
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

	public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

		return new AutoSuggest(plugin)
				.suggestGame()
				.run(commandSender, command, s, args);
	}


}
