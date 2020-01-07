package com.twostudentsllc.gladiator.runnables.game;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_game.handlers.GameLobby.GameLobby;

public class GameMapStartCountdown implements Runnable {

	private Main plugin;
	private GameLobby lobby;
	private String mapName;
	
	public GameMapStartCountdown(Main plugin, GameLobby lobby, String mapName)
	{
		this.plugin = plugin;
		this.lobby = lobby;
		this.mapName = mapName;
	}
	
	@Override
	public void run() {
		sendTimeLeft();

	}

	public void sendTimeLeft()
	{
		lobby.handleMapStartCountdown(mapName, lobby.getMapStartCountdown(mapName).getSecondsLeft());
	}
}
