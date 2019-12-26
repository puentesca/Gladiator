package com.twostudentsllc.gladiator.runnables;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Game;
import com.twostudentsllc.gladiator.generic_classes.MapMatch;

public class GameMapStartCountdown implements Runnable {

	private Main plugin;
	private Game game;
	private String mapName;
	
	public GameMapStartCountdown(Main plugin, Game game, String mapName)
	{
		this.plugin = plugin;
		this.game = game;
		this.mapName = mapName;
	}
	
	@Override
	public void run() {
		sendTimeLeft();

	}

	public void sendTimeLeft()
	{
		game.handleMapStartCountdown(mapName, game.getMapStartCountdown(mapName).getSecondsLeft());
	}
}
