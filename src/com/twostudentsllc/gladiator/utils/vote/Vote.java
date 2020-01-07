package com.twostudentsllc.gladiator.utils.vote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import com.twostudentsllc.gladiator.runnables.Countdown;
import com.twostudentsllc.gladiator.ui.inventory.GUIInventory;
import com.twostudentsllc.gladiator.ui.inventory.InventoryItem;
import com.twostudentsllc.gladiator.runnables.inventory.InventoryRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.utils.Utils;
import com.twostudentsllc.gladiator.runnables.vote.VoteCounterInventoryRunnable;
import com.twostudentsllc.gladiator.runnables.vote.VotingCountdown;
/**
 * A vote that auto creates a GUI and handles clicking per item. Ends after a countdown and sends the winner to the VoteHost
 * Copyright 2020 Casey Puentes. All rights reserved.
 * @author Casey Puentes
 *
 */
public class Vote {

	private Main plugin;
	/**
	 * Holds the votes for all of the choices by mapping the players UUID to their choice
	 */
	private HashMap<UUID, String> votes;
	
	private ArrayList<String> voteOptions;
	private GUIInventory inventory;
	/**
	 * The object that is hosting the vote. Used to send the winner after the countdown is done
	 */
	private VoteHost voteHost;
	
	private Countdown votingCountdown;
	
	/**
	 * Creates an inventory with the voting options and commands
	 * @param inventoryName The name of the inventory
	 * @param choices The available choices
	 * @param itemMaterial The material each choice should be displayed as
	 */
	public Vote(Main plugin, VoteHost voteHost, String inventoryName, ArrayList<String> choices, Material itemMaterial, int countdownTime)
	{
		this.plugin = plugin;
		this.voteHost = voteHost;
		votes = new HashMap<UUID, String>();
		createVotingInventory(inventoryName, choices, itemMaterial);
		startCountdown(countdownTime);
	}
	
	/**
	 * Starts the voting countdown
	 * @param countdownTime
	 */
	public void startCountdown(int countdownTime)
	{
		Runnable r = new VotingCountdown(plugin, this);
		Countdown c = new Countdown(plugin, countdownTime, r, true);
		votingCountdown = c;
	}
	
	/**
	 * Handles the time remaining in the vote. If the vote is finished, it sends the VoteHost the winner
	 * @param time
	 */
	public void handleVotingTimeLeft(int time)
	{
		Bukkit.broadcastMessage("Voting has " + time + " seconds remaining");
		if(time <= 0)
		{
			String winner = getWinner();
			Bukkit.broadcastMessage("Voting has concluded. The winning choice was: " + winner);
			voteHost.votingFinished(winner);
		}
	}
	
	/**
	 * Creates an inventory with the voting options and commands
	 * @param inventoryName The name of the inventory
	 * @param choices The available choices
	 * @param itemMaterial The material each choice should be displayed as
	 */
	public void createVotingInventory(String inventoryName, ArrayList<String> choices, Material itemMaterial)
	{
		HashMap<InventoryRunnable, InventoryItem> items = new HashMap<InventoryRunnable, InventoryItem>();
		
		int rows = choices.size()/9 + 1;
		voteOptions = (ArrayList<String>) choices.clone();
		
		int count = 0;
		//Creates all of the item choices
		for(String itemName : choices)
		{
			InventoryRunnable runnable = new VoteCounterInventoryRunnable(plugin, this);
			InventoryItem item = new InventoryItem(plugin, itemMaterial, itemName, null, 1, false, count);
			items.put(runnable, item);
			System.out.println("Created listener at idx: " + count);
			count++;
		}
		
		inventory = new GUIInventory(plugin, inventoryName, items, rows);
	}
	
	/**
	 * Adds a players vote
	 * @param p The player who voted
	 * @param vote Their choice
	 */
	public void vote(Player p, String vote)
	{
		votes.put(p.getUniqueId(), vote);
		Utils.confirmationMessage(p, "You successfully voted for '" + vote + "'!");
	}
	
	/**
	 * Calculates all of the votes in the hashmap and returns a winner. Returns a random choice if no one votes
	 * @return The string of the winning option
	 */
	public String getWinner()
	{
		//If no one voted, return a random choice
		if(votes.size() == 0)
			return Utils.chooseOneRandomly(voteOptions);
		
		System.out.println("Counting votes");
		int[] voteCount = new int[voteOptions.size()];
		//Counts the votes
		for(UUID id : votes.keySet())
		{
			String vote = votes.get(id);
			//Checks to see which index to add their vote to
			for(int i = 0; i < voteOptions.size(); i++)
			{
				//If the person voted for the current index
				if(voteOptions.get(i).equals(vote))
				{
					voteCount[i]++;
					System.out.println("Vote added for option: " + i);
					break;
				}
			}
		}
		
		int winnerIdx = 0;
		int winnerVotes = 0;
		//Finds the choice with the most votes
		for(int i = 0; i < voteCount.length; i++)
		{
			int curVotes = voteCount[i];
			if(curVotes > winnerVotes)
			{
				winnerVotes = curVotes;
				winnerIdx = i;
			}
		}
		Arrays.toString(voteCount);
		
		return voteOptions.get(winnerIdx);
	}
	
	public Inventory getVotingInventory()
	{
		return inventory.getInventory();
	}
	
	public Countdown getVotingCountdown()
	{
		return votingCountdown;
	}
	
}
