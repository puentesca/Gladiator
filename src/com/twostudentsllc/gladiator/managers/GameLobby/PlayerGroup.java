package com.twostudentsllc.gladiator.managers.GameLobby;

import org.bukkit.entity.Player;

import java.util.ArrayList;

//Maintains a group of players (used for group queuing together)
public class PlayerGroup {

    private int groupLeader; //Tracks a group leader
    private ArrayList<Player> groupMembers;

    public PlayerGroup(Player player) {
        groupMembers = new ArrayList<Player>();
        groupMembers.add(player);
        groupLeader = 0;
    }

    public PlayerGroup(ArrayList<Player> members) {
        groupMembers = members;
        assignGroupLeader();
    }

    public PlayerGroup(ArrayList<Player> members, int groupLeader) {
        groupMembers = members;
        this.groupLeader = groupLeader;
    }

    /**
     * Randomly assign a group leader
     */
    private void assignGroupLeader() {
        groupLeader = (int)Math.floor((Math.random() * groupMembers.size())) - 1;
    }

    public ArrayList<Player> getMembers()
    {
        return groupMembers;
    }

    public int getGroupLeader() { return groupLeader; };

    /**
     * Removes all disconnected players from a playerGroup
     * Assigns a new groupLeader
     */
    public void removeInvalidMembers() {
        for(Player p: groupMembers) {
            if(!p.isOnline()) { //TODO: Can add extra conditions here for different removal checks
                groupMembers.remove(p);
            }
        }
        assignGroupLeader();
    }

}
