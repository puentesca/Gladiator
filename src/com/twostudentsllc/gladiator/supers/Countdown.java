package com.twostudentsllc.gladiator.supers;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.twostudentsllc.gladiator.Main;

public class Countdown implements Runnable {
	
	/**
	 * The original total time until completion
	 */
	private int seconds;
	/**
	 * The current time remaining, in seconds
	 */
	private int secondsCounter;
	
	private Main plugin;
	
	/**
	 * The unique Bukkit task ID. This is used to later cancel the countdown
	 */
	private int assignedTaskId;
	
	/**
	 * Holds tasks that have to be run after the task threshold. Needs to be sorted in order of execution
	 */
	private ArrayList<Runnable> tasks;
	/**
	 * Holds the amount of time(in seconds) that has to have passed for a task to take place. Index is linked with an index in the tasks arraylist
	 * Needs to be sorted in numerical order
	 */
	private ArrayList<Integer> taskThresholds;
	
	/**
	 * Initializes the countdown.
	 * @param plugin
	 * @param seconds
	 * @param tasks
	 * @param taskThresholds
	 * @param startCountdown True if you want it to auto start the countdown upon creation
	 */
	public Countdown(Main plugin, int seconds, ArrayList<Runnable> tasks, ArrayList<Integer> taskThresholds, boolean startCountdown)
	{
		this.plugin = plugin;
		this.seconds = seconds;
		this.tasks = tasks;
		this.taskThresholds = taskThresholds;
		secondsCounter = seconds;
		
		if(startCountdown)
			startCountdown();
	}

	@Override
	public void run() {
		secondsCounter -= 1;
		checkTaskList();
	}
	
	/**
	 * Checks to see if, at the current time elapsed, any tasks need to be executed. If so, it executes them and removes them from the tasks lists.
	 */
	private void checkTaskList()
	{
		//If all the time has elapsed
		if(secondsCounter == -1)
		{
			cancel();
		}
		//If all tasks have been completed
		if(taskThresholds.size() == 0)
			return;
		//The amount of time needed to have passed for the next task to be run
		int timeThreshold = taskThresholds.get(0);
		//The amount of seconds that the secondsCounter should be at when this threshold is met
		int timeElapsed = seconds - timeThreshold;
		
		//If the amount of time has passed by
		if(secondsCounter <= timeElapsed)
		{
			//Run the task
			tasks.get(0).run();
			//Complete the task and remove it from the list
			taskThresholds.remove(0);
			tasks.remove(0);
		}
		
	}
	
	/**
	 * Gets the amount of time left (in seconds)
	 * @return The amount of seconds left
	 */
	public int getSecondsLeft()
	{
		return secondsCounter;
	}
	
	/**
     * Gets the total seconds this timer was set to run for
     * @return Total seconds timer should run
     */
    public int getTotalSeconds() {
        return seconds;
    }
	
	/**
	 * Starts the countdown
	 */
	public void startCountdown() {
        // Initialize our assigned task's id, for later use so we can cancel. Runs every second.
        assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }
	
	/**
	 * Stops the countdown
	 */
	public void stopCountdown()
	{
		cancel();
	}
	
	/**
	 * Cancels the countdown. Called upon completion
	 */
	private void cancel()
	{
		Bukkit.getScheduler().cancelTask(assignedTaskId);
	}
	
}
