package com.twostudentsllc.gladiator.commands.location;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.Game;
import com.twostudentsllc.gladiator.generic_classes.GameMap;
import com.twostudentsllc.gladiator.global.Utils;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationCommandHelper {

    private static String[] possibleLocationKeys = {"spawn", "lobby", "spectate", "hub", "objective"};

    public static boolean validateArguments(Main plugin, Player sender, String[] args) {

        String minigameName = args[1];
        String mapName = args[2];
        String key = args[3];

        System.out.println(minigameName + ":" + mapName + ":" + key);

        //Validate that the mini-game exists
        Game targetGame = plugin.getGameManager().getGame(minigameName);
        if(plugin.getGameManager().getGame(minigameName) == null) {
            Utils.Error(sender, "Minigame does not exist!");
            return false;
        }



        //Validate that the map exists
        GameMap targetMap = targetGame.getGameMap(mapName);
        if(targetGame.getGameMap(mapName) == null) {
            Utils.Error(sender, "Map does not exist!");
            return false;
        }

        //If the location is one of the valid location keys
        boolean invalidKey = true;
        for(String possibleKey: possibleLocationKeys) {
            if(!key.contains(possibleKey)) {
                invalidKey = false;
            }
        }

        if(invalidKey) {
            Utils.Error(sender, "Invalid Location");
            return false;
        }

        //Custom validation for whether the location is a spawn-point
        if(key.contains("spawn")) {
            Pattern spawnValidate = Pattern.compile("spawn\\d+_\\d+"); //Check if format is spawn#_#
            Matcher matcher = spawnValidate.matcher(key);
            boolean spawnValid = matcher.matches();

            if(!spawnValid) {
                Utils.Error(sender, "Spawn-point configuration invalid, the format is spawn#_#");
                return false;
            }
        }

        //Custom validation for whether the location is an objective
        if(key.contains("objective")) {
            Pattern spawnValidate = Pattern.compile("objective\\d+"); //Check if format is objective#
            Matcher matcher = spawnValidate.matcher(key);
            boolean spawnValid = matcher.matches();

            if(!spawnValid) {
                Utils.Error(sender, "Objective configuration invalid, the format is objective#");
                return false;
            }
        }

        return true;
    }
}
