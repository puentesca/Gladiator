package com.twostudentsllc.gladiator.supers;


import java.util.HashMap;
import java.util.Map;

public class PlayerStats {

    private Map<String, Object> stats;

    public PlayerStats() {
        HashMap<String, Object> defaultStates = new HashMap<>();

        //Stats that are being tracked have to be initialized by default
        defaultStates.put("deaths", 0);
        defaultStates.put("kills", 0);

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

}
