package org.finalbi.tnttag;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.finalbi.tnttag.commands.TntTagStart;
import org.finalbi.tnttag.commands.TntTagStop;

public final class TntTag extends JavaPlugin {


    public static TntTag INTANCE;
    FileConfiguration configuration;
    boolean powerupsDefault = true;
    public ProtocolManager manaager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        int pluginId = 19773; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        INTANCE = this;
        manaager = ProtocolLibrary.getProtocolManager();
        configuration = this.getConfig();
        configuration.addDefault("defaultPowerupsEnable", true);
        if (configuration.getBoolean("defaultPowerupsEnable")){
            powerupsDefault = configuration.getBoolean("defaultPowerupsEnable");
        }
        getServer().getPluginManager().registerEvents(new TntTagListener(), this);
        registerCommands();
    }

    private void registerCommands() {
        getCommand("tntstart").setExecutor(new TntTagStart(powerupsDefault));
        getCommand("tntstop").setExecutor(new TntTagStop());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
