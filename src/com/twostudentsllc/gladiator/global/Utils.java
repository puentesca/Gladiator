package com.twostudentsllc.gladiator.global;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Team;

import net.md_5.bungee.api.ChatColor;

/**
 * A common utility class
 * Copyright 2019 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class Utils {

	private Main plugin;
	
	public Utils(Main plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Converts colors referenced (Using '&') to server-side color codes. 
	 * @param s The string to convert
	 * @param addHeader True if you want the [Gladiator] in front of the message
	 * @return The converted string
	 */
	public static String chatMessage(String s, boolean addHeader)
	{
		if(addHeader)
		{
			s = getGladiatorHeader() + s;
		}
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	/**
	 * Gets the [Gladiator]: header in chat with color
	 * @return The header with color
	 */
	public static String getGladiatorHeader()
	{
		return "&8[&cGladiator&8]: " ;
	}
	
	/**
	 * Gets the [GladiatorError]: header in chat with color
	 * @return The header with color
	 */
	public static String getGladiatorErrorHeader()
	{
		return "&8[&cGladiatorError&8]: &c" ;
	}
	
	/**
	 * Boxes any string inbetween custom colored brackets
	 * @param msg The message to box
	 * @param colorCode The color you want the boxes to be
	 * @return The boxed string
	 */
	public static String boxText(String msg, String color)
	{
		msg = color + "[" + msg + color + "]";
		return msg;
	}
	
	/**
	 * Creates a serialized version of a location that can be easily saved and read.
	 * @param The location to serialize
	 * @return String of serialized location
	 */
	public static String serializeLocation(Location l)
	{
		if(l == null)
			throw new NullPointerException("The location you want to serialize is null!");
		
		String string = l.getWorld().getName() + ":" + l.getX() + ":" + l.getY() + ":" + l.getZ() + ":" + l.getYaw() + ":" + l.getPitch();
		return string;
	}
	
	/**
	 * Creates a serialized version of a location of a block that can be easily saved and read.(Omits yaw, pitch as well as gets block locations)
	 * @param The location to serialize
	 * @return String of serialized location
	 */
	public static String serializeBlockLocation(Location l)
	{
		if(l == null)
			throw new NullPointerException("The location you want to serialize is null!");
		
		String string = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
		return string;
	}
	
	/**
	 * Creates a location using the serialized string version of a stored location.
	 * Stores world, x, y, z, yaw, and pitch.
	 * @param The string to deserialize
	 * @return The deserialized location
	 */
	public static Location deserializeLocation(String s)
	{
		Location l;
		
		String[] parts = s.split(":");
		if(parts.length != 6)
		{
			System.err.println("Attempted to deserialize a location that did not have only the location, x, y, z, yaw, and pitch variables!");
			return null;
		}
		World world = Bukkit.getServer().getWorld(parts[0]);
		Double x = Double.parseDouble(parts[1]);
		Double y = Double.parseDouble(parts[2]);
		Double z = Double.parseDouble(parts[3]);
		Float yaw = Float.parseFloat(parts[4]);
		Float pitch = Float.parseFloat(parts[5]);
		
		l = new Location(world, x, y, z, yaw, pitch);
		
		return l;	
	}
	
	/**
	 * Checks to see if a string is in a string array.
	 * @param ray is the array in which you wish to search for the target in
	 * @param target is the string you wish to search for in the ray
	 * @return True if the ray contains the target
	 */
	public static boolean arrayContains(String[] ray, String target)
	{
		for(String s : ray)
		{
			if(s.equals(target))
				return true;
		}
		return false;
	}
	
	/**
	 * Sends a custom error message to a player
	 * @param p The player to recieve the message
	 * @param message The message to send
	 */
	public static void Error(Player p, String message)
	{
		String msg = getGladiatorErrorHeader() + message;
		p.sendMessage(chatMessage(msg, false));
	}
	/**
	 * Sends a custom error message to a CommandSender
	 * @param sender The CommandSender to receive the message
	 * @param message The message to send
	 */
	public static void Error(CommandSender sender, String message)
	{
		String msg = getGladiatorErrorHeader() + message;
		sender.sendMessage(chatMessage(msg, false));
	}
	
	/**
	 * Returns a correct usage of a command message
	 * @param usage The correct usage string
	 */
	public static String getUsageMessage(String usage)
	{
		String msg = "&bCommand Usage: " + usage;
		return msg;
	}
	
	/**
	 * Sends a message notifying the player a command has been completed successfully.
	 * @param p The player to receive the message
	 * @param name The name of the command
	 */
	public static void commandCompletedMessage(Player p, String name)
	{
		String msg = "&aCommand '" + name + "' completed.";
		p.sendMessage(chatMessage(msg, true));
	}
	
	/**
	 * Sends a message notifying the CommandSender a command has been completed successfully.
	 * @param p The CommandSender to receive the message
	 * @param name The name of the command
	 */
	public static void commandCompletedMessage(CommandSender p, String name)
	{
		String msg = "&aCommand '" + name + "' completed.";
		p.sendMessage(chatMessage(msg, true));
	}


	public static ArrayList<Team> assignTeams(int numberOfTeams, ArrayList<Player> playerList) {

		if(numberOfTeams > playerList.size())
			throw new IllegalArgumentException("Number of teams exceeds playerList size!");

		int players = playerList.size();
		int playersPerTeam = players / numberOfTeams;

		//Return null if players can't be split into teams equally
		if(players % numberOfTeams != 0)
			throw new IllegalArgumentException("Players cant be split into equal teams!");

		ArrayList<Team> teamList = new ArrayList<Team>();

		//Create the required number of teams
		for(int i = 0; i < numberOfTeams; i++) {

			ArrayList<Player> teamMembers = new ArrayList<Player>();

			//Add players according to number of teams required
			for(int q = 0; q < playersPerTeam; q++) {
				teamMembers.add(playerList.get(i * playersPerTeam + q));
			}

			teamList.add(new Team(i, teamMembers));
		}

		return teamList;
	}
	
	/**
	 * Gets the string mapped to a players spawn
	 * @param teamNum The number of the team
	 * @param playerNums The number of the player on the team
	 * @return The string mapped to a players spawn
	 */
	public static String getPlayerSpawnLocationString(int teamNum, int playerNum)
	{
		String msg = "";
		msg = "spawn" + teamNum + "_" + playerNum;
		return msg;
	}
	
	/**
	 * Returns the block a player is looking at
	 * @param player The player who is looking
	 * @param range The range for the search
	 * @return The block the player is looking at
	 */
	public static Block getTargetBlock(Player player, int range) {
        BlockIterator iter = new BlockIterator(player, range);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }
	
}
