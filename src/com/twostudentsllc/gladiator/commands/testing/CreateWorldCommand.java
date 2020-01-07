package com.twostudentsllc.gladiator.commands.testing;

import com.twostudentsllc.gladiator.utils.WorldUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.twostudentsllc.gladiator.Main;
import com.twostudentsllc.gladiator.commands.CustomCommand;
import com.twostudentsllc.gladiator.utils.Utils;

public class CreateWorldCommand extends CustomCommand {

    private Main plugin;

    public CreateWorldCommand(Main plugin)
    {

        super.setName("createworld")
                .setDescription("Creates a world")
                .setUsage("/gladiator createworld <worldName>")
                .setPermissions(new String[] {"gladiator.createworld"})
                .setMinArgs(2)
                .setMaxArgs(2)
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
        String worldName = args[1];
        WorldUtils.createVoidWorld(worldName);

    }
}
