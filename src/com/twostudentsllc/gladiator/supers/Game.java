package com.twostudentsllc.gladiator.supers;

import java.util.HashMap;

public abstract class Game {

    private String gameName;
    private String displayName;
    private HashMap<String, GameMap> maps;

    public Game(String game, String displayName) {
        gameName = game;
        this.displayName = displayName;
        maps = new HashMap<String,GameMap>();
    }



    public abstract GameMap getGameMap();
}
