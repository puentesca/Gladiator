package com.twostudentsllc.gladiator.managers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.global.DatabaseManager;
import com.twostudentsllc.gladiator.global.Utils;

public class ClickableBlockManager {
	private Main plugin;

	HashMap<Location, String> blockLocationsAndCommands;

	public ClickableBlockManager(Main plugin) {
		this.plugin = plugin;
		loadClickableBlocks();
	}

	public void loadClickableBlocks() {
		blockLocationsAndCommands = new HashMap<Location, String>();
		HashMap<String, String> serializedClickables = DatabaseManager.loadClickables();
		for (String s : serializedClickables.keySet()) {
			String serializedLocation = s;
			String[] parts = serializedLocation.split(":");
			String worldName = parts[0];
			double x = Double.parseDouble(parts[1]);
			double y = Double.parseDouble(parts[2]);
			double z = Double.parseDouble(parts[3]);
			Location l = new Location(Bukkit.getServer().getWorld(worldName), x, y, z);
			blockLocationsAndCommands.put(l, serializedClickables.get(s));
		}
		System.out.println("Successfully loaded clickables.");
	}

	public void saveClickableBlocks() {
		HashMap<String, String> serializedClickables = new HashMap<String, String>();

		for (Location l : blockLocationsAndCommands.keySet()) {
			serializedClickables.put(Utils.serializeBlockLocation(l), blockLocationsAndCommands.get(l));
		}
		DatabaseManager.saveClickables(serializedClickables);
		System.out.println("Successfully saved clickables.");
	}

	/**
	 * Adds a clickable block location and the command
	 * 
	 * @param l       The location of the block
	 * @param command The command to be executed upon clicking
	 */
	public void addClickableBlock(Location l, String command) {
		blockLocationsAndCommands.put(l, command);
		saveClickableBlocks();
		System.out.println("Successfully added and saved clickable block.");
	}

	/**
	 * Removes a clickable blocks location and command
	 * 
	 * @param l The location of the clickable block
	 */
	public void removeClickableBlock(Location l) {
		blockLocationsAndCommands.remove(l);
		saveClickableBlocks();
		System.out.println("Successfully removed clickable block.");
	}

	/**
	 * Called when a player clicks a block and runs the command for them if they
	 * clicked a valid block
	 * 
	 * @param p The player who clicked the block
	 * @param l The location of the block that was clicked
	 */
	public void PlayerClickedBlock(Player p, Location l) {
		String command = getClickedBlockCommand(l);
		if (command == null)
			return;
		// TODO: Make player run command
	}

	/**
	 * Gets the command associated with a player clicking a block
	 * 
	 * @param l The location of the block clicked
	 * @return The string command that must be executed if a player right clicks the
	 *         block
	 */
	public String getClickedBlockCommand(Location l) {
		if (!clickedBlockExists(l))
			return null;
		return blockLocationsAndCommands.get(l);
	}

	public boolean clickedBlockExists(Location l) {
		return blockLocationsAndCommands.containsKey(l);
	}
}
