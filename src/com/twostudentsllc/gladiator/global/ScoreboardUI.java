package com.twostudentsllc.gladiator.global;

import com.twostudentsllc.gladiator.Main;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardUI {

    private Scoreboard board;

    private Map<String, Objective> objectives;
    private Map<String, ArrayList<ScoreGroup>> trackedScores;

    private ArrayList<Player> playerList;
    private Map<String, Team> teamMap;

    private Main plugin;

    private class ScoreGroup {

        private String label;
        private Score score;
        private Team scoreTeam;

        public ScoreGroup(String label, String displayName, Objective toAdd, int value) {
            score = toAdd.getScore(label);
            score.setScore(value);

            this.label = label;
            scoreTeam = board.registerNewTeam(label);
        }
    }

    public ScoreboardUI(Main plugin) {
        this.plugin = plugin;
        ScoreboardManager b = plugin.getServer().getScoreboardManager();

        System.out.println("=================================================\n\n\n" + b);


        board = plugin.getServer().getScoreboardManager().getMainScoreboard();


        playerList = new ArrayList<>();
        teamMap = new HashMap<>();
        objectives = new HashMap<>();
        trackedScores = new HashMap<>();
    }

    private Objective createObjective(String objectiveName, String type, String displayName, DisplaySlot displayStyle) {

        Objective obj = board.registerNewObjective(objectiveName, type, displayName);
        obj.setDisplaySlot(displayStyle);
        trackedScores.put(objectiveName, new ArrayList<>());

        return objectives.put(objectiveName, obj);
    }

    public void createSideBar(String name, String displayName) {
        createObjective(name, "dummy", displayName, DisplaySlot.SIDEBAR);
    }

    public Objective getObjective(String name){
        return objectives.get(name);
    }

    private boolean createScoreForObjective(String objectiveName, String label, String displayName, int value) {

        Objective targetView = objectives.get(objectiveName);

        if(targetView == null)
            return false;

        trackedScores.get(objectiveName).add(new ScoreGroup(label, displayName, objectives.get(objectiveName), value));

        return true;
    }

    public ScoreGroup getScoreForObjective(String objectiveName, String label) {

        Objective targetView = objectives.get(objectiveName);

        if(targetView == null)
            return null;

        ArrayList<ScoreGroup> targetScores = trackedScores.get(objectiveName);

        for(ScoreGroup group: targetScores) {
            if(group.label.equals(label)) {
                return group;
            }
        }

        return null;
    }

    public boolean addRow(String objectiveName, String scoreLabel, String text) {
        ScoreGroup targetGroup = getScoreForObjective(objectiveName, scoreLabel);
        Objective targetObjective = getObjective(objectiveName);

        if(targetGroup == null)
            return false;

        targetGroup.scoreTeam.addEntry(""); //There might need to be something here for formatting

        //We have to update prefix because updating the real value does not automatically refresh the scoreboard (idk)
        targetGroup.scoreTeam.setPrefix(text);

        targetObjective.getScore("").setScore(14);

        return true;
    }

    public boolean assignPlayersToScoreboard(String objectiveName, ArrayList<Player> players) {

        Objective targetObjective = getObjective(objectiveName);

        if(targetObjective == null)
            return false;

        for(Player player: players)
            player.setScoreboard(board);

        //Track players to make it easier to remove them later
        playerList.addAll(players);

        return true;

    }

    public void removePlayersFromScoreboard() {
        //Switches all players to the default scoreboard which should be empty
        for(Player player: playerList) {
            player.setScoreboard(plugin.getServer().getScoreboardManager().getMainScoreboard());
        }
    }

    //Have to remove players from the scoreboard when being destroyed otherwise the scoreboard statys in memory
    @Override
    public void finalize() {
        removePlayersFromScoreboard();
    }




    /*public Team attachPlayersToScoreboard(String groupName, ArrayList<Player> players) {
        Team newTeam = display.registerNewTeam(groupName);

        for(Player p: players) {
            newTeam.addEntry(p.);
        }

        return teamMap.put(groupName, newTeam);
    }

    public boolean addPlayerToTeam(String groupName, Player toAdd) {
        if(!teamMap.containsKey(groupName))
            return false;

        teamMap.get(groupName).hasEntry()

    }*/

    //FIXME: Crashes client for some reason (seems like null pointer exception in constructor)
    public boolean createRow(Player toAdd) {

       /* Objective obj = board.registerNewObjective("ServerName", "dummy", "");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score onlineName = obj.getScore(ChatColor.GRAY + "» Online");
        onlineName.setScore(1);

        Team onlineCounter = board.registerNewTeam("onlineCounter");

        // Note: The ' + "" + ' is there to convert the colors into strings without manually calling ChatColor#toString
        onlineCounter.addEntry("w");*/

/*        if (plugin.getServer().getOnlinePlayers().size() == 0) {
            onlineCounter.setPrefix(ChatColor.DARK_RED + "0" + ChatColor.RED + "/" + ChatColor.DARK_RED + plugin.getServer().getMaxPlayers());
        } else {
            onlineCounter.setPrefix("" + ChatColor.DARK_RED + plugin.getServer().getOnlinePlayers().size()
                    + ChatColor.RED + "/" + ChatColor.DARK_RED + plugin.getServer().getMaxPlayers());
        }*/

        //obj.getScore("w").setScore(14);


        //toAdd.setScoreboard(board);

/*
        Scoreboard playerBoard = toAdd.getScoreboard();

        if (plugin.getServer().getOnlinePlayers().size() == 0) {
            playerBoard.getTeam("onlineCounter").setPrefix(ChatColor.DARK_RED + "0" + ChatColor.RED + "/" + ChatColor.DARK_RED
                    + plugin.getServer().getMaxPlayers());
        } else {
            playerBoard.getTeam("onlineCounter").setPrefix(ChatColor.DARK_RED + "" +
                    plugin.getServer().getOnlinePlayers().size() + ChatColor.RED + "/" + ChatColor.DARK_RED + plugin.getServer().getMaxPlayers());
        }*/

        return true;
    }
}
