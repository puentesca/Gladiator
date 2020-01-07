package com.twostudentsllc.gladiator.managers.GameLobby;


import com.twostudentsllc.gladiator.generic_classes.GameMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

//Maintains a queue of PlayerGroups
public class PlayerQueue {

    private GameMap map;

    //Minimum number of teams
    private int teamMinimum;

    //Max number of teams
    private int teamMaximum;

    //The expected size of each team
    private int teamSize;

    private Queue<PlayerGroup> playerList;

    public PlayerQueue(GameMap map, int teamMinimum, int teamMaximum, int teamSize) {
        this.map = map;
        this.teamMinimum = teamMinimum;
        this.teamMaximum = teamMaximum;
        this.teamSize = teamSize;
        this.playerList = new LinkedList<PlayerGroup>();
        System.out.println("Created queue for map " + map.getMapName() + " with minTeams: " + teamMinimum + " maxTeams: " + teamMaximum + " teamSize: " + teamSize);
    }

    /**
     * Checks if a playerQueue has a player in it
     *
     * @param toFind target player you are looking for
     * @return true/false depending on whether the player is found
     */
    public boolean contains(Player toFind) {
        for (PlayerGroup group : playerList) {
            if (group.getMembers().contains(toFind)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds player to queue
     *
     * @param toAdd target player to add
     * @return true/false depending on whether adding them was successful
     */
    public boolean addPlayer(Player toAdd) {
        if (contains(toAdd))
            return false;
        playerList.add(new PlayerGroup(toAdd));
        System.out.println("PlayerQueue added player '" + toAdd.getName() + "' to queue");
        System.out.println("Total groups: " + playerList.size());
        return true;
    }

    /**
     * Adds a group of players to a queue
     *
     * @param group ArrayList of players that are queuing together
     * @return true/false depending on whether adding them was successful
     */
    public boolean addGroup(ArrayList<Player> group) {
        for (Player p : group) {
            if (contains(p)) {
                return false;
            }
        }

        playerList.add(new PlayerGroup(group));
        return true;
    }

    /**
     * NOTICE: Shouldn't be used typically (all players are in groups)
     * Removes a player from the queue
     *
     * @param toRemove player to remove
     * @return true/false depending on whether removing them was successful
     */
    public boolean removePlayer(Player toRemove) {
        for (PlayerGroup group : playerList) {
            if (group.getMembers().contains(toRemove)) {
                return group.getMembers().remove(toRemove);
            }
        }
        return false;
    }

    /**
     * Removes a group from the queue if the player is a group leader
     *
     * @param toRemove player in group that forces full group removal
     * @return true/false depending on whether removing them was successful
     */
    public boolean removeGroupByPlayer(Player toRemove) {
        for (PlayerGroup group : playerList) {
            if (group.getMembers().contains(toRemove) && group.getGroupLeader() == group.getMembers().indexOf(toRemove)) {
                playerList.remove(group);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the total number of players in queue
     *
     * @return int value for the total number of players in queue
     */
    public int getTotalPlayersInQueue() {
        int total = 0;
        for (PlayerGroup group : playerList) {
            total += group.getMembers().size();
        }
        return total;
    }


    /**
     * Tries to see if there is a group combination that produces the required number of players
     *
     * @return true/false depending on whether there is a group combination
     */
    public boolean canMakeGame() {

        if (!map.canStartMatch())
            return false;

        //O(n^2) method to find group combos TODO: Improve somehow?
        for (PlayerGroup group : playerList) {
            int sum = group.getMembers().size();

            System.out.println("Current sum: " + sum);
            if (sum >= teamMinimum * teamSize && sum <= teamMaximum * teamSize && sum % teamSize == 0) {
                System.out.println("Can make game! Sum: " + sum);
                return true;
            }

            for (PlayerGroup group2 : playerList) {
                if (!group2.equals(group)) {
                    sum += group2.getMembers().size();
                    System.out.println("Current inside loop sum: " + sum);
                    if (sum >= teamMinimum * teamSize && sum <= teamMaximum * teamSize && sum % teamSize == 0) {
                        System.out.println("Can make game! Sum: " + sum);
                        return true;
                    }
                }
            }
        }

        System.out.println("Cannot make game.");
        return false;
    }

    /** FIXME: Two O(n^2) operations is a bit overkill, i think there is some way to combine the getPlayers and canMakeGame functionality
     * Returns a list of players from the groups
     * @return an arraylist of players
     */
    public ArrayList<Player> getPlayers() {

        int totalPlayers = 0;
        int maxPlayers =  teamMaximum * teamSize;
        int minPlayer = teamMinimum * teamSize;
        ArrayList<Player> players = new ArrayList<Player>();
        //Runs while the amount of players being grabbed is less than the max and while there are players to be grabbed
        while(totalPlayers <= maxPlayers && !playerList.isEmpty())
        {
            //Gets and removes the next group in the queue
            PlayerGroup group = playerList.element();
            playerList.remove();

            //TODO: Add a check to see if the group that is about to be added will be too large for the game.
            //and if so, dont remove them but skip them somehow

            ArrayList<Player> groupMembers = group.getMembers();
            for(Player p : groupMembers)
            {
                players.add(p);
            }
        }
        return players;
    }

    /**
     * Removes all disconnected players from queue
     */
    public void removeDisconnectedPlayers() {
        for(PlayerGroup group: playerList) {
            group.removeInvalidMembers();
        }
    }

    //Returns team size
    public int getTeamSize() {
        return teamSize;
    }

    //Returns minimum number of teams allowed in map
    public int getTeamMinimum() {
        return teamMinimum;
    }

    //Returns maximum number of teams allowed in map
    public int getTeamMaximum() {
        return teamMaximum;
    }

}
