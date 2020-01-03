package com.twostudentsllc.gladiator.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;

public class MysqlCommunicator {
	private Main plugin;
	public MysqlCommunicator(Main plugin)
	{
		this.plugin = plugin;
	}
	
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
				insert.setString(1, uuid.toString());
				insert.setString(2, player.getName());
				insert.executeUpdate();
				System.out.println("Player inserted!");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
