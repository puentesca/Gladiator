package com.twostudentsllc.gladiator.commands.testing;

import java.util.*;

import com.twostudentsllc.gladiator.generic_game.handlers.Game;
import com.twostudentsllc.gladiator.generic_game.handlers.GameMap;
import com.twostudentsllc.gladiator.commands.AutoSuggest;
import com.twostudentsllc.gladiator.global_managers.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;


public class ListCommand extends CustomCommand {

    private Main plugin;

    public ListCommand(Main plugin)
    {

        super.setName("list")
                .setDescription("lists various information for gladiator")
                .setUsage("/gladiator list <prop> <additional props> ...")
                .setPermissions(new String[] {"gladiator.list"})
                .setMinArgs(1)
                .setMaxArgs(4)
                .setPlayerOnly(false);

        //Initializes data
        this.plugin = plugin;
        plugin.getCommandManager().registerCommand(getName(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName = args[0];

        //Signifies the command has been called
        commandCalled(sender, cmd, label, args);

        Utils.commandCompletedMessage(sender, getName()); //Command completion confirmation
        return true;
    }

    /**
     * Called when the command listed is called
     */
    private void commandCalled(CommandSender sender, Command cmd, String label, String[] args)
    {

        switch(args[1]) {
            case "games": {
                GameManager manager = plugin.getGameManager();
                HashMap<String, Game> games = manager.getGames();

                sender.sendMessage(Utils.chatMessage(games.keySet().toString(), false));

                break;
            }

            case "maps": {
                String gameName = args[2];

                GameManager manager = plugin.getGameManager();
                HashMap<String, Game> games = manager.getGames();

                Game targetGame = games.get(gameName);

                sender.sendMessage(Utils.chatMessage(targetGame.getMaps().keySet().toString(), false));
                break;
            }

            case "locations": {
                String gameName = args[2];
                String mapName = args[3];

                GameManager manager = plugin.getGameManager();
                HashMap<String, Game> games = manager.getGames();

                Game targetGame = games.get(gameName);

                GameMap targetMap = targetGame.getGameMap(mapName);

                sender.sendMessage(Utils.chatMessage(targetMap.getLocations().keySet().toString(), false));
                break;
            }

            case "kits": {
                String gameName = args[2];

                GameManager manager = plugin.getGameManager();
                HashMap<String, Game> games = manager.getGames();

                Game targetGame = games.get(gameName);

                sender.sendMessage(Utils.chatMessage(targetGame.getInventoryManager().listInventories().toString(), false));
                break;
            }
        }

    }

    public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        return new AutoSuggest(plugin)
                .suggestCustom(new String[] {"games", "maps", "locations", "kits"})
                .suggestGame()
                .suggestMap()
                .run(commandSender, command, s, args);

        /*
        if(args.length == 1) {

            return Arrays.asList("games","maps","locations", "kits");

        } else if (args.length == 2) {

            if(args[0].equals("maps") || args[0].equals("locations") || args[0].equals("kits")) {

                //Suggest the games if they are looking for maps or locations
                GameManager manager = plugin.getGameManager();
                HashMap<String, Game> games = manager.getGames();
                ArrayList<String> list = new ArrayList<>();

                list.addAll(games.keySet());

                return list;
            }
        } else if (args.length == 3) {

            if(args[0].equals("locations")) {

                //Suggest the maps if they are looking for locations
                GameManager manager = plugin.getGameManager();
                HashMap<String, Game> games = manager.getGames();
                Game game = games.get(args[1]);
                ArrayList<String> list = new ArrayList<>();

                HashMap<String, GameMap> maps = game.getMaps();

                list.addAll(maps.keySet());

                return list;
            }
        }
        return new ArrayList<>();*/
    }
}
