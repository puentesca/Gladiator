package com.twostudentsllc.gladiator.games;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Game;

public class Gladiator extends Game{
	
	public Gladiator(Main plugin, String game, String displayName) {
		super(plugin, game, displayName);
		registerGame(this);
	}

	@Override
	public void createGameMap(String[] args) {
		String minigameName = args[1];
		String mapName = args[2];
		String mapDisplayName = args[3];
		int minTeams = Integer.parseInt(args[4]);
		int maxTeams = Integer.parseInt(args[5]);
		int minPlayers = Integer.parseInt(args[6]);
		int maxPlayers = Integer.parseInt(args[7]);
	}

}
