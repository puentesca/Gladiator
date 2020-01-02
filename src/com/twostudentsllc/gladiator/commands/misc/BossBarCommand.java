package com.twostudentsllc.gladiator.commands.misc;

import com.twostudentsllc.gladiator.global.AutoSuggest;
import com.twostudentsllc.gladiator.ui.BossBarUI;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.generic_classes.CustomCommand;
import com.twostudentsllc.gladiator.global.Utils;

import java.util.ArrayList;
import java.util.List;

public class BossBarCommand extends CustomCommand{

    private Main plugin;

    public BossBarCommand(Main plugin)
    {
        super.setName("bossbar")
                .setDescription("Sets the boss bar")
                .setUsage("/gladiator bossbar <action> <text> <color>")
                .setPermissions(new String[] {"gladiator.bossbar"})
                .setMinArgs(2)
                .setMaxArgs(4)
                .setPlayerOnly(true);

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
        Player p = (Player)sender;
        ArrayList<Player> list = new ArrayList<Player>();
        list.add(p);

        //Execute command logic
        if(args[1].equals("create"))
            BossBarUI.createBossBar(plugin, args[2], args[3], BarColor.BLUE, BarStyle.SEGMENTED_6, list, BarFlag.CREATE_FOG);
        else if (args[1].equals("remove"))
            BossBarUI.removeBossBar(args[2]);
    }

    public List<String> tabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        return new AutoSuggest(plugin)
                .suggestCustom(new String[]{"create", "remove"})
                .suggestCustom(BossBarUI.getActiveBarNames())
                .run(commandSender, command, s, args);
    }
}
