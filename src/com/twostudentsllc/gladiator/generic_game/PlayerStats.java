package com.twostudentsllc.gladiator.generic_game;


import java.util.HashMap;
import java.util.Map;

public class PlayerStats {

    private Map<String, Object> stats;
    
    //Default stats keys
    private String deathsKey = "deaths";
    private String killsKey = "kills";
    private String eliminatedKey = "eliminated";

    public PlayerStats() {
        HashMap<String, Object> defaultStates = new HashMap<>();

        //Stats that are being tracked have to be initialized by default
        defaultStates.put(deathsKey, 0);
        defaultStates.put(killsKey, 0);
        defaultStates.put(eliminatedKey, false);

        stats = defaultStates;
    }

    //Increases kill statistic by 1 (method made for readability)
    public void incrementKills() {
        stats.put(killsKey, (Integer)stats.get(killsKey)+1);
    }

    //Increases death statistic by 1 (method made for readability)
    public void incrementDeaths() {
        stats.put(deathsKey, (Integer)stats.get(deathsKey)+1);
    }
 
    /**
     * Sets deaths back to zero. Also resets the players eliminated status
     */
    public void resetDeaths()
    {
    	stats.put(deathsKey, 0);
    }
    
    public int getDeaths()
    {
    	return (int)stats.get(deathsKey);
    }
    
    public int getKills()
    {
    	return (int)stats.get(killsKey);
    }
    
    /**
     * Gets if the player is eliminated
     * @return True if the player is eliminated
     */
    public boolean isEliminated()
    {
    	return (boolean)stats.get(eliminatedKey);
    }
    
    /**
     * Eliminates a player
     */
    public void eliminatedPlayer()
    {
    	stats.put(eliminatedKey, true);
    }

}
