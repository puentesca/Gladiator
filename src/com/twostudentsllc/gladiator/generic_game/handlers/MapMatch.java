package com.twostudentsllc.gladiator.generic_game.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.twostudentsllc.gladiator.generic_game.Team;
import com.twostudentsllc.gladiator.generic_game.listeners.MinigameListener;
import com.twostudentsllc.gladiator.runnables.Countdown;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.utils.Utils;
import com.twostudentsllc.gladiator.datastorage.mysql.MysqlManager;

/**
 * An interface for each round in an arena. Copyright 2019 Casey Puentes. All
 * rights reserved.
 * 
 * @author Casey Puentes
 *
 */

public abstract class MapMatch {

	/**
	 * Contains the status of the current round
	 * 
	 * @author Casey Puentes
	 *
	 */
	public static enum STATUS {
		WAITING, WARMUP, IN_PROGRESS, COOLDOWN, COMPLETED, ERROR
	};

	protected Main plugin;

	/**
	 * The map that this current round is taking place on
	 */
	protected GameMap map;

	/**
	 * Holds the starting time limit of the round, in seconds
	 */
	protected int timeLimit;

	/**
	 * Holds the countdown for a warmup. Null when there is no warmup countdown
	 * currently happening
	 */
	protected Countdown warmupTimelimitCountdown;
	/**
	 * Holds the countdown for a cooldown. Null when there is no cooldown countdown
	 * currently happening
	 */
	protected Countdown cooldownTimelimitCountdown;

	/**
	 * How long should the warm up last
	 */
	protected int warmupTimeLimit;

	/**
	 * How long should the cooldown period last
	 */
	protected int cooldownTimeLimit;

	/**
	 * Number of rounds this Match has
	 */
	protected int totalRounds;

	/**
	 * What round the Match is currently on
	 */
	protected int currentRoundCount;

	/**
	 * The current round that is happening on the match
	 */
	protected MatchRound currentRound;

	/**
	 * The amount of lives every player in the round starts with
	 */
	protected int playerLives;

	/**
	 * The teams currently participating in the round
	 */
	protected ArrayList<Team> teams;

	/**
	 * Holds the listeners that are present for the entire match
	 */
	protected ArrayList<MinigameListener> matchListeners;
	
	/**
	 * Holds all of the listeners that are registered during warmup
	 */
	protected ArrayList<MinigameListener> warmupListeners;

	// TODO: Check for method that ends the match and timelimit countdown based on an event like team elim

	public MapMatch(Main plugin, GameMap map, int timeLimit, int warmupTimeLimit, int cooldownTimeLimit,
			int totalRounds, ArrayList<Team> teams) {
		setStatus(STATUS.WAITING);
		this.plugin = plugin;
		this.map = map;
		this.timeLimit = timeLimit;
		this.teams = teams;
		this.warmupTimeLimit = warmupTimeLimit;
		this.cooldownTimeLimit = cooldownTimeLimit;
		this.totalRounds = totalRounds;
		playerLives = 3; // TODO: Add ability to customize this
		this.currentRoundCount = 0;
		matchListeners = new ArrayList<MinigameListener>();
		warmupListeners = new ArrayList<MinigameListener>();
		startMatch();
	}

	/**
	 * Starts a match. Must call giveAllPlayersKits();
	 */
	public abstract void startMatch();

	/**
	 * Ends a match. Must save winners and losers stats to MySQL.
	 */
	public abstract void endMatch();

	// Method is called by Java when an object is going to be deleted
	@Override
	protected void finalize() {
		// Unloads loaded chunks in the world when the object is removed
		map.unloadChunks();
	}

	/**
	 * Gives all players in the match their selected kits
	 */
	public void giveAllPlayersKits()
	{
		for(Team t : teams)
		{
			for(Player p : t.getPlayers())
			{
				//Get the equippedkit
				String tableName = MysqlManager.getMinigameStatsTableName(map.getGame().getGameName());
				String dataName = "equippedkit";
				String dataIdentifierName = "UUID";
				String dataIdentifierValue = "'" + p.getUniqueId().toString() + "'";
				String equippedKit = plugin.getMysqlManager().getCommunicator().getSQLStrings(tableName, dataName, dataIdentifierName, dataIdentifierValue).get(0);
				//Sets the kit
				map.getGame().getInventoryManager().setPlayerInventory(p, equippedKit);
			}
		}
	}

	/**
	 * Gets the winning players of the match
	 * 
	 * @return
	 */
	public abstract ArrayList<Player> getWinningPlayers();

	/**
	 * Gets the current status of the round
	 * 
	 * @return The status of the round
	 */
	public abstract STATUS getStatus();

	public abstract void setStatus(STATUS status);

	/**
	 * In charge of handling the way the warmup time left is handled. Should
	 * instantiate a MatchRound when its zero. Should also nullify the instance of
	 * warmupTimelimitCounter when the countdown is finished. Should unregister warmup listeners
	 * 
	 * @param time
	 */
	public abstract void handleWarmupTimeRemaining(int time);

	/**
	 * In charge of handling the way the cooldown time left is handled. Should also
	 * nullify the instance of cooldownTimelimitCounter when the countdown is
	 * finished and stop warmup counter.
	 * 
	 * @param time
	 */
	public abstract void handleCooldownTimeRemaining(int time);

	/**
	 * Starts a round with the players and teams assigned, also needs to increment
	 * round counter and stop cooldown counter. Must reset playerstats and unfreeze
	 * all players
	 * 
	 * @return True if the round was successfully started
	 */
	public abstract boolean startRound();

	/**
	 * Ends a round. Responsible for starting cooldown.
	 * @return True if the round was successfully ended
	 */
	public abstract boolean endRound();

	/**
	 * Handles the warmup period, also in charge of setting state to WARMUP. Must also register warmup listeners
	 * freeze all players
	 */
	public abstract void doWarmup();
	
	/**
	 * Registers all match listeners
	 */
	public abstract void registerMatchListeners();
	
	/**
	 * Unregisters all match listeners
	 */
	public void unregisterMatchListeners()
	{
		for(MinigameListener ml : matchListeners)
		{
			ml.unregisterEvent();
		}
		matchListeners = new ArrayList<MinigameListener>();
	}
	
	/**
	 * Registers all warmup listeners
	 */
	public abstract void registerWarmupListeners();
	
	/**
	 * Unregisters all warmup listeners
	 */
	public void unregisterWarmupListeners()
	{
		for(MinigameListener ml : warmupListeners)
		{
			ml.unregisterEvent();
		}
		warmupListeners = new ArrayList<MinigameListener>();
	}

	/**
	 * Gets a players team if they are in this match. If they are not, it returns null.
	 * @param p The player whose team you need to get
	 * @return The team of the player
	 */
	public Team getPlayersTeam(Player p)
	{
		for(Team t : teams)
		{
			if(t.getPlayers().contains(p))
				return t;
		}
		return null;
	}
	
	/**
	 * Removes a player from a team in game and checks to see if it makes a team win
	 * @param p The player who disconnected
	 */
	public void playerDisconnected(Player p)
	{
		Team playersTeam = getPlayersTeam(p);
		if(currentRound != null && playersTeam != null)
		{
			playersTeam.removePlayer(p);
			currentRound.checkForWinner();
		}
	}
	
	/**
	 * Handles the cooldown period, also in charge of setting state to COOLDOWN
	 */
	public abstract void doCooldown();

	/**
	 * Handles what happens after the cooldown is completed. If there is another
	 * round to be played, start the warmup for it
	 */
	public void cooldownCompleted() {
		cooldownTimelimitCountdown = null;
		if (hasAnotherRound())
			doWarmup();
		else {
			endMatch();
		}
	}

	/**
	 * Whether or not there are any rounds remaining
	 * 
	 * @return true/false
	 */
	public boolean hasAnotherRound() {
		return currentRoundCount < totalRounds;
	}

	/**
	 * Teleports all players to the spawn
	 */
	public void sendAllPlayersToSpawn() {
		for (Team t : teams) {
			HashMap<Player, Location> playerSpawns = t.getPlayerSpawns();
			for (Player p : playerSpawns.keySet()) {
				p.teleport(playerSpawns.get(p));
				p.setGameMode(GameMode.ADVENTURE);
			}
		}
	}

	// TODO: Move all teleporting methods to one class (MapMatch class?)
	/**
	 * Sends a player to their assigned spawn
	 * 
	 * @param p The player to send to spawn
	 * @param includePitchYaw True if you want the Yaw and Pitch of the location to be transferred to the player as well as x,y,z
	 */
	public void sendPlayerToSpawn(Player p, boolean includeYawPitch) {
		for (Team t : teams) {
			if (t.containsPlayer(p)) {
				//Gets the players spawnpoint
				Location l = t.getPlayerSpawns().get(p);
				if(includeYawPitch)
					p.teleport(l);
				else
				{
					//Creates a new location with the players current yaw pitch
					Location noPitchYaw = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
					p.teleport(noPitchYaw);
				}
				return;
			}
		}
		throw new IllegalArgumentException("Player '" + p.getName() + "' is not part of a team in this round on map: '"
				+ map.getMapDisplayName() + "'!");
	}

	public Countdown getWarmupCountdown() {
		return warmupTimelimitCountdown;
	}

	public Countdown getCooldownCountdown() {
		return cooldownTimelimitCountdown;
	}

	/**
	 * Resets all players stats to default state at the beginning of a round
	 */
	public void resetPlayerStats() {
		for (Team t : teams) {
			t.resetAllPlayerStats();
		}
	}

	/**
	 * Resets all players lives to the defined amount and eliminated status to false
	 */
	public void resetPlayerLives() {
		for (Team t : teams) {
			t.resetAllPlayerLives();
		}
	}

	/**
	 * Maps every player a spawnpoint
	 */
	public void assignPlayerSpawnpoints() {
		HashMap<String, Location> locations = map.getLocations();
		// Loops through every team
		for (Team t : teams) {
			HashMap<Player, Location> playerSpawns = new HashMap<Player, Location>();
			int teamNum = t.getTeamID();
			ArrayList<Player> teamMembers = t.getPlayers();
			// Every player in every team
			for (int playerNum = 0; playerNum < t.getTotalPlayers(); playerNum++) {
				String spawnString = Utils.getPlayerSpawnLocationString(teamNum, playerNum);
				// If the spawn doesn't exist, there arent enough spawns set for players
				if (!locations.containsKey(spawnString))
					throw new IllegalArgumentException(
							"This map has not been assigned enough player spawns! Confirmed missing spawn: '"
									+ spawnString + "' and possibly others.");

				Location spawn = locations.get(spawnString);
				Player p = teamMembers.get(playerNum);

				playerSpawns.put(p, spawn);
			}
			t.setPlayerSpawns(playerSpawns);
		}
	}

	public GameMap getMap() {
		return map;
	}
	
	public MatchRound getCurrentRound()
	{
		return currentRound;
	}
}
