package com.twostudentsllc.gladiator.commands;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_game.handlers.Game;
import com.twostudentsllc.gladiator.generic_game.handlers.GameMap;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class AutoSuggest {

    //Functional interface for making lambda expressions
    public interface SuggestionHandler {
        public abstract ArrayList<String> handleSuggestion(Main plugin, CommandSender commandSender, Command command, String s, String[] args);
    }

    private ArrayList<SuggestionHandler> activeHandlers;
    private static Map<String, SuggestionHandler> handlerMap = new HashMap<>();

    static {

        System.out.println("Loading suggestion handlers!");

        //Handler that suggests a list of games
        handlerMap.put("games", (Main plugin, CommandSender commandSender, Command command, String s, String[] args) -> {
            return new ArrayList<>(plugin.getGameManager().getGames().keySet());
        });

        //Handler that suggests a list of maps for a game
        handlerMap.put("maps", (Main plugin, CommandSender commandSender, Command command, String s, String[] args) -> {

            if(args.length < 1)
                return null;

            Game targetGame = plugin.getGameManager().getGame(args[args.length-2]);

            if(targetGame == null)
                return null;

            return new ArrayList<>(targetGame.getMapNames());
        });

        //Handler that suggests a list of locations for a map in a game
        handlerMap.put("locations", (Main plugin, CommandSender commandSender, Command command, String s, String[] args) -> {

            if(args.length < 2)
                return null;

            Game targetGame = plugin.getGameManager().getGame(args[args.length-3]);

            if(targetGame == null)
                return null;

            GameMap targetMap = targetGame.getGameMap(args[args.length-2]);

            if(targetMap == null)
                return null;

            return new ArrayList<>(targetMap.getLocations().keySet());
        });

        //Handler that suggests a list of kits for a game
        handlerMap.put("kits", (Main plugin, CommandSender commandSender, Command command, String s, String[] args) -> {

            if(args.length < 1)
                return null;

            Game targetGame = plugin.getGameManager().getGame(args[args.length-2]);

            if(targetGame == null)
                return null;

            return new ArrayList<>(targetGame.getInventoryManager().listInventories());
        });

        //Handler that suggests a list of worlds on a server
        handlerMap.put("worlds", (Main plugin, CommandSender commandSender, Command command, String s, String[] args) -> {

            List<World> worlds = plugin.getServer().getWorlds();
            ArrayList<String> names = new ArrayList<>();
            for(World world: worlds){
                names.add(world.getName());
            }

            return new ArrayList<String>(names);

        });

    }

    private Main plugin;

    public AutoSuggest(Main plugin) {
        this.plugin = plugin;
        activeHandlers = new ArrayList<>();
    }

    public AutoSuggest suggestGame() {
        activeHandlers.add(handlerMap.get("games"));
        return this;
    }

    public AutoSuggest suggestMap() {
        activeHandlers.add(handlerMap.get("maps"));
        return this;
    }

    public AutoSuggest suggestLocation() {
        activeHandlers.add(handlerMap.get("locations"));
        return this;
    }

    public AutoSuggest suggestKit() {
        activeHandlers.add(handlerMap.get("kits"));
        return this;
    }

    public AutoSuggest suggestWorld() {
        activeHandlers.add(handlerMap.get("worlds"));
        return this;
    }


    /**
     * Used to skip command arguments that are between custom suggestion lists
     * (will break suggestions that require more than 1 argument)
     */
    public AutoSuggest skip() {
        activeHandlers.add(null);
        return this;
    }


    /**
     * Create a custom list of suggestions
     * @param suggestions String[] array that represents the suggestions you want to offer
     * @return
     */
    public AutoSuggest suggestCustom(String[] suggestions) {
        activeHandlers.add((Main plugin, CommandSender commandSender, Command command, String s, String[] args) -> {
            return new ArrayList<String>(Arrays.asList(suggestions));
        });
        return this;
    }

    public AutoSuggest suggestCustom(Collection<String> suggestions) {
        activeHandlers.add((Main plugin, CommandSender commandSender, Command command, String s, String[] args)->{
            return new ArrayList<String>(suggestions);
        });
        return this;
    }


    /**
     * Executes a list of suggestion handlers
     * @return
     */
    public ArrayList<String> run(CommandSender commandSender, Command command, String s, String[] args) {

        //If there are more args than handlers give no suggestions
        if(args.length > activeHandlers.size())
            return null;

        return activeHandlers.get(args.length-1).handleSuggestion(plugin, commandSender, command, s, args);
    }
}
