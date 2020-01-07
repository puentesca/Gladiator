package com.twostudentsllc.gladiator.datastorage.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.twostudentsllc.gladiator.Main;


public class MysqlManager {
	private Main plugin;
	private Connection connection;
	public String host;
	public String database;
	public String username;
	public String password;
	public String table;
	public int port;
	private MysqlCommunicator communicator;
	
	public MysqlManager(Main plugin)
	{
		this.plugin = plugin;
		mysqlSetup();
		communicator = new MysqlCommunicator(plugin);
	}
	 
	public void mysqlSetup()
	{
		host = plugin.getConfig().getString("host");
		port = plugin.getConfig().getInt("port");
		username = plugin.getConfig().getString("username");
		password = plugin.getConfig().getString("password");
		database = plugin.getConfig().getString("database");
		table = plugin.getConfig().getString("table");
		try
		{
			synchronized(this)
			{
				if(getConnection()!= null && !getConnection().isClosed())
					return;
				Class.forName("com.mysql.jdbc.Driver");
				setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
				System.out.println("MYSQL Connected successfully.");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	public MysqlCommunicator getCommunicator()
	{
		return communicator;
	}
	
	public static String getMinigameStatsTableName(String minigameName)
	{
		return  minigameName + "_stats";
	}
	
}
