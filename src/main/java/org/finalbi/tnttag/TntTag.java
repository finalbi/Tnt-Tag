package org.finalbi.tnttag;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.finalbi.tnttag.commands.TntTagStart;
import org.finalbi.tnttag.commands.TntTagStop;

public final class TntTag extends JavaPlugin {


    public static TntTag INTANCE;
    @Override
    public void onEnable() {
        // Plugin startup logic
        INTANCE = this;
        getServer().getPluginManager().registerEvents(new TntTagListener(), this);
        registerCommands();
        Metrics metrics = new Metrics(this, 19103);
    }

    private void registerCommands() {
        getCommand("tntstart").setExecutor(new TntTagStart());
        getCommand("tntstop").setExecutor(new TntTagStop());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
