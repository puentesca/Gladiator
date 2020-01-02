package com.twostudentsllc.gladiator.commands.misc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;


public class ScoreboardCommand extends CustomCommand{

    private Main plugin;

    public ScoreboardCommand(Main plugin)
    {
        super.setName("scc")
                .setDescription("Sets scoreboard")
                .setUsage("/gladiator scoreboard <action> <text> <color>")
                .setPermissions(new String[] {"gladiator.score"})
                .setMinArgs(2)
                .setMaxArgs(4)
                .setPlayerOnly(false);

        this.plugin = plugin;
        plugin.getCommandManager().registerCommand(getName(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String commandName = args[0];

        //Signifies the command has been called
        commandCalled(sender, cmd, label, args);
        Utils.commandCompletedMessage(sender, getName());
        return true;
    }

    /**
     * Called when the command listed is called
     */
    private void commandCalled(CommandSender sender, Command cmd, String label, String[] args)
    {
        //ScoreboardUI b = new ScoreboardUI(plugin);
        //b.createRow((Player) sender);
    }

/*    public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        return new AutoSuggest(plugin)
                .suggestCustom(new String[]{"create", "remove"})
                .suggestCustom(UI.getActiveBarNames())
                .run(commandSender, command, s, args);
    }*/
}
