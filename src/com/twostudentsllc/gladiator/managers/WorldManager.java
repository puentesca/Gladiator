package com.twostudentsllc.gladiator.managers;

import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import com.twostudentsllc.gladiator.global.CustomChunkGenerator;

public class WorldManager {

    /*
     * Gets world by the name {worldname}. If one does not exist, it creates one
     * @return the World object if valid world, null if invalid world or doesn't exist
     */
    public static World getWorld(Server currentServer, String worldName) {
//    	World worldToGet = currentServer.getWorld(worldName);
//    	if(worldToGet == null)
//    	{
//    		worldToGet = createVoidWorld(worldName);
//    	}
        return currentServer.getWorld(worldName);
    }
    
//    public static World createVoidWorld(String worldName)
//    {
//    	WorldCreator w = new WorldCreator("Void creator");
//    	ChunkGenerator g = new CustomChunkGenerator();
//    	w.generator(g);
//    }

    /*
     * Creates a world under the name worldName, if it already exists it just loads from disk
     * @return World object associated with worldName
     */
    public static World createWorld(Server currentServer, String worldName) {
        WorldCreator generator = WorldCreator.name(worldName);
        return generator.createWorld();
    }

    /*
     * Unloads world with name worldName
     * @param worldName name of world
     * @param save whether or not to save chunks before unloading
     * @return true/false if was successfully unloaded
     */
    public static boolean unloadWorld(Server currentServer, String worldName, boolean save) {
        return currentServer.unloadWorld(worldName, save);
    }

    /*
    * Unloads all loaded chunks on a world
    * @param currentServer the server the plugin is operating on
    * @param worldName the name of the world to have chunks unloaded
     */
    public static void unloadLoadedChunks(Server currentSever, String worldName) {
        World targetWorld = getWorld(currentSever, worldName);

        Chunk[] loadedChunks = targetWorld.getLoadedChunks();

        for(Chunk chunk: loadedChunks) {
            targetWorld.unloadChunkRequest(chunk.getX(), chunk.getZ());
        }
    }

    public static void unloadLoadedChunks(Server currentSever, World world) {
        Chunk[] loadedChunks = world.getLoadedChunks();

        for(Chunk chunk: loadedChunks) {
            world.unloadChunkRequest(chunk.getX(), chunk.getZ());
        }
    }

    /*
     * Unload all the worlds for the whole server
     * @param boolean for whether to save or not save chunk data
     */
    public static void unloadAllWorlds(Server currentServer, boolean save) {
        List<World> worlds = currentServer.getWorlds();

        for(World world: worlds) {
            currentServer.unloadWorld(world, save);
        }
    }

    /*
     * Creates a copy of a world file under a new name
     * @param otherWorld name of world you want to copy
     * @param newWorld name of new world copy
     */
    public static World createWorldCopy(Server currentServer, String otherWorld, String newWorld) {
        World worldToCopy = currentServer.getWorld(otherWorld);

        if(worldToCopy == null)
            throw new IllegalArgumentException(otherWorld + " does not exist!");

        WorldCreator generator = new WorldCreator(newWorld);
        return generator.copy(worldToCopy).createWorld();
    }

}

