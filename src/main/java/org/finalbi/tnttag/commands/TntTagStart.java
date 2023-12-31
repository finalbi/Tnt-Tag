package org.finalbi.tnttag.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.finalbi.tnttag.TntTag;

import java.util.*;

public class TntTagStart implements CommandExecutor {

    public static boolean hasStarted;

    public static List<Player> tntIt;
    public static List<String> powerups;
    public static List<Player> players;
    public static int round = 1;

    public static boolean enabledPowerups;
    public boolean defaultpowerups;

    public TntTagStart(boolean defaultpowerups){
        this.defaultpowerups = defaultpowerups;
    }

    public static int amountOfIt = 0;



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0){
            if (args[0].equals("True")){
                enabledPowerups = true;
            } else if (args[0].equals("False")){
                enabledPowerups = false;
            } else {
                enabledPowerups = defaultpowerups;
            }
        } else {
            enabledPowerups = defaultpowerups;
        }
        powerups = new ArrayList<>();
        if (enabledPowerups) {
            setupPowerUps();
        }
        Random random = new Random();
        players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player: players) {
            player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        }

        startTimer("TNT TAG", 60);
        hasStarted = true;
        Bukkit.broadcastMessage("Tnt Tag Round " + round + " Starting");
        return true;
    }

    private void setupPowerUps() {
        powerups = new ArrayList<>();
        powerups.add("speed");
        powerups.add("invis");
        powerups.add("darkness");
        powerups.add("slowness");
        powerups.add("glowing");
        powerups.add("stick");
        powerups.add("swap");
        powerups.add("shield");
    }

    public static void Tag(Player tagger,Player tagged) {
        tagger.getInventory().remove(new ItemStack(Material.TNT));
        tntIt.add(tagged);
        tagged.getInventory().setItemInMainHand(new ItemStack(Material.TNT));
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "/title " + tagged.getDisplayName() + " title [\"\",{\"text\":\"YOUR IT\",\"color\":\"red\"}]");

    }
    public void startTimer(String sidebarTitle, int durationSeconds) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("sidebar", "dummy", sidebarTitle);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Add scores to the objective\
           Score score2 = objective.getScore("Round:");
        score2.setScore(round);
        Score score = objective.getScore("Time:");
        score.setScore(durationSeconds);

        new BukkitRunnable() {
            int secondsLeft = durationSeconds;

            @Override
            public void run() {
                if (secondsLeft <= 0) {
                    for (Player player : tntIt) {
                        player.getInventory().remove(new ItemStack(Material.TNT));
                        player.setHealth(0.0);
                        players.remove(player);
                    }
                    tntIt.clear();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        removeSidebar(player);
                    }
                    cancel();
                    NextRound();
                    return;
                }

                // Update the sidebar with the remaining time
                score.setScore(secondsLeft);
                secondsLeft--;
            }
        }.runTaskTimer(TntTag.INTANCE, 0L, 20L); // Run every second (20 ticks)

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
        Bukkit.broadcastMessage("STARTED");
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    public void removeSidebar(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) {
            objective.unregister();
        }
    }

    public void NextRound(){
        if (players.size() <= 1){
            end();
        }
        round++;
        Random random = new Random();
        double unRoundItNumber = (16.67 / 100) * players.size();
        amountOfIt = (int)Math.ceil(unRoundItNumber);
        for (int i = 0;i <= amountOfIt;i++) {
            int randomNumber = random.nextInt(players.size());
            tntIt.add(players.get(randomNumber));
        }
        for (Player player : tntIt) {
            Bukkit.broadcastMessage(player.getDisplayName() + " is It");
            player.getInventory().setItemInMainHand(new ItemStack(Material.TNT));
        }
        startTimer("TNT TAG", 60);
        hasStarted = true;
        Bukkit.broadcastMessage("Tnt Tag Round " + round + " Started");
    }
    public void end(){
        tntIt.clear();
        hasStarted = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeSidebar(player);
        }
    }

}
