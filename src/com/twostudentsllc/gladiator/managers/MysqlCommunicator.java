package com.twostudentsllc.gladiator.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;

public class MysqlCommunicator {
	private Main plugin;
	public MysqlCommunicator(Main plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Checks the database to see if a player exists 
	 * @param uuid The UUID of the player
	 * @return True if the player exists
	 */
	public boolean playerExists(UUID uuid)
	{
		String stringStatement = "SELECT * FROM " + plugin.getMysqlManager().table +" WHERE UUID=?";
		try {
			PreparedStatement statement = plugin.getMysqlManager().getConnection().prepareStatement(stringStatement);
			statement.setString(1, uuid.toString());
			
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				System.out.println("The player who just joined exists in the database!");
				return true;
			}
			System.out.println("The player who just joined was not found in the database!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Checks to see if a player exists in the database. If not, it creates the player.
	 * @param uuid The UUID of the player
	 * @param player The player object
	 */
	public void createPlayer(final UUID uuid, Player player)
	{
		String stringStatement = "SELECT * FROM " + plugin.getMysqlManager().table +" WHERE UUID=?";
		try
		{
			PreparedStatement statement = plugin.getMysqlManager().getConnection().prepareStatement(stringStatement);
			statement.setString(1, uuid.toString());
			
			ResultSet results = statement.executeQuery();
			results.next();
			if(!(playerExists(uuid)))
			{
				String stringStatement2 = "INSERT INTO " + plugin.getMysqlManager().table + " (UUID, NAME) VALUE (?,?)";
				PreparedStatement insert = plugin.getMysqlManager().getConnection().prepareStatement(stringStatement2);
				//Date date = new Date();
				//Timestamp ts = new Timestamp(date.getTime());
				insert.setString(1, uuid.toString());
				insert.setString(2, player.getName());
				//insert.setTimestamp(3, ts);
				insert.executeUpdate();
				System.out.println("Player inserted!");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the player has minigame stats in the database
	 * @param uuid The UUID of the player
	 * @param minigameName the name of the minigame (Not display name)
	 * @return True if the player has stats for gladiator
	 */
	public boolean minigameStatsExists(UUID uuid, String minigameName)
	{
		String stringStatement = "SELECT * FROM " + plugin.getMysqlManager().getMinigameStatsTableName(minigameName) + " WHERE UUID=?";
		try {
			PreparedStatement statement = plugin.getMysqlManager().getConnection().prepareStatement(stringStatement);
			statement.setString(1, uuid.toString());
			
			ResultSet results = statement.executeQuery();
			if(results.next())
			{
				System.out.println("The player has minigame stats.");
				return true;
			}
			System.out.println("The player who joined the minigame does not have stats!");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Creates a new minigame stats entry for a player if one does not exist
	 * @param uuid The UUID of the player
	 * @param minigameName the name of the minigame (Not display name)
	 */
	public void createMinigameStats(UUID uuid, String minigameName)
	{
		if(!minigameStatsExists(uuid, minigameName))
		{
			String stringStatement = "INSERT INTO " + plugin.getMysqlManager().getMinigameStatsTableName(minigameName) + " (UUID) VALUE (?)";
			try {
				PreparedStatement insert = plugin.getMysqlManager().getConnection().prepareStatement(stringStatement);
				insert.setString(1, uuid.toString());
				insert.executeUpdate();
				System.out.println("Minigame stats created!");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void updateMinigameStat(UUID uuid, String minigameName, String statName, int toAdd)
	{
		//If there is nothing to add, save memory and do not execute the update
		if(toAdd == 0)
			return;
		
		int currentVal = 0;
		String table = plugin.getMysqlManager().getMinigameStatsTableName(minigameName);
		String stringStatement = "SELECT " + statName + " FROM " + table +" WHERE UUID=?";
		try
		{
			PreparedStatement statement = plugin.getMysqlManager().getConnection().prepareStatement(stringStatement);
			statement.setString(1, uuid.toString());
			ResultSet results = statement.executeQuery();
			results.next();
			currentVal = results.getInt(statName);
			
			currentVal += toAdd;
			
			String stringStatement2 = "UPDATE " + table + " SET " + statName + "=? WHERE UUID=?";
			PreparedStatement insert = plugin.getMysqlManager().getConnection().prepareStatement(stringStatement2);
			insert.setInt(1, currentVal);
			insert.setString(2, uuid.toString());
			insert.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
