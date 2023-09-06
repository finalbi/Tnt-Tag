package org.finalbi.tnttag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TntTagStop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TntTagStart.tntIt = null;
        TntTagStart.hasStarted = false;
        Bukkit.broadcastMessage("Stoping the tnt Game");

        return true;
    }
}
