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
		plugin.getGameManager().getGame(minigameName).addPlayerToQueue(mapName, (Player)sender);
	}

	public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

		return new AutoSuggest(plugin)
				.suggestGame()
				.suggestMap()
				.run(commandSender, command, s, args);
	}
	

}
