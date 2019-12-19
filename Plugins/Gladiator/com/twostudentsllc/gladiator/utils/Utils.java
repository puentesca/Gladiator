package com.twostudentsllc.gladiator.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.arenas.Arena;

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
	 * Creates a Arena from a serialized arena string
	 * @param s The serialized Arena 
	 * @return The deserialized Arena
	 */
	//TODO: Move to Gladiator game class and change plugin to not null
	public static Arena deserializeArena(String s)
	{
		Arena a;
		
		String[] parts = s.split(":");
		
		Main plugin = null;
		String minigameName = parts[0];
		String mapName = parts[1];
		String mapDisplayName = parts[2];
		int minTeams = Integer.parseInt(parts[3]);
		int maxTeams = Integer.parseInt(parts[4]);
		int minPlayers = Integer.parseInt(parts[5]);
		int maxPlayers = Integer.parseInt(parts[6]);
		
		a = new Arena(plugin, minigameName, mapName, mapDisplayName, minTeams, maxTeams, minPlayers, maxPlayers);
		
		return a;
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
	
}
