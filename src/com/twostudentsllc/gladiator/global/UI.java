package com.twostudentsllc.gladiator.global;

import com.twostudentsllc.gladiator.Main;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UI {

    private static Map<String, BossBar> activeBars = new HashMap<String, BossBar>();

    /**
     * Creates a boss bar UI element
     * @param plugin main plugin
     * @param name Name given to bar to later remove it
     * @param message Message boss bar should say
     * @param color BarColor
     * @param style BarStyle
     * @param visibleForPlayers Players that should see the bar
     * @param flags extra fancy flags
     */
    public static void createBossBar(Main plugin, String name, String message, BarColor color, BarStyle style, ArrayList<Player> visibleForPlayers, BarFlag ...flags) {
        BossBar bar = plugin.getServer().createBossBar(message, color, style, flags);
        bar.removeAll();
        bar.setVisible(false);

        //Adds all the players that should see the boss bar
        for(Player player: visibleForPlayers)
            bar.addPlayer(player);
        bar.setVisible(true);
        activeBars.put(name, bar);
    }

    /**
     * Removes a boss bar and hides from players
     * @param name Name that was used to define the boss bar
     */
    public static void removeBossBar(String name) {
        BossBar bar = activeBars.remove(name);

        if(bar == null)
            return;

        bar.setVisible(false);
    }

    public static Map<String, BossBar> getActiveBars() {
        return activeBars;
    }
    public static Set<String> getActiveBarNames() {
        return activeBars.keySet();
    }
}
