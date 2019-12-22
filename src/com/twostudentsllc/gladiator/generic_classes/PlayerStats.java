package com.twostudentsllc.gladiator.generic_classes;


import java.util.HashMap;
import java.util.Map;

public class PlayerStats {

    private Map<String, Object> stats;

    public PlayerStats() {
        HashMap<String, Object> defaultStates = new HashMap<>();

        //Stats that are being tracked have to be initialized by default
        defaultStates.put("deaths", 0);
        defaultStates.put("kills", 0);
        defaultStates.put("eliminated", false);

        stats = defaultStates;
    }

    //Increases kill statistic by 1 (method made for readability)
    public void incrementKills() {
        stats.put("kills", (Integer)stats.get("kills")+1);
    }

    //Increases death statistic by 1 (method made for readability)
    public void incrementDeaths() {
        stats.put("deaths", (Integer)stats.get("deaths")+1);
    }
    
    public int getDeaths()
    {
    	return (int)stats.get("deaths");
    }
    
    public int getKills()
    {
    	return (int)stats.get("kills");
    }
    
    /**
     * Gets if the player is eliminated
     * @return True if the player is eliminated
     */
    public boolean isEliminated()
    {
    	return (boolean)stats.get("eliminated");
    }
    
    /**
     * Eliminates a player
     */
    public void eliminatedPlayer()
    {
    	stats.put("eliminated", true);
    }

}
