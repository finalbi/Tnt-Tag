package org.finalbi.tnttag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.finalbi.tnttag.commands.TntTagStart;
import org.finalbi.tnttag.commands.TntTagStop;

import java.awt.event.ItemEvent;
import java.util.*;

public class TntTagListener implements Listener {

    ItemStack darkPotion;
    PotionMeta darkPotionMeta;
    PotionData darkPotionData;
    PotionEffectType effectType;
    int durationTicks = 10 * 20; // 10 seconds
    boolean ambient = true;
    PotionEffect effect;
    ItemStack slowPotion;
    PotionMeta slowPotionMeta;
    PotionData slowPotionData;
    PotionEffectType sloweffectType;
    PotionEffect sloweffect;
    ItemStack glowPotion;
    PotionMeta glowPotionMeta;
    PotionData glowPotionData;
    PotionEffectType gloweffectType;
    PotionEffect gloweffect;

    ItemStack freezePotion;
    PotionMeta freezePotionMeta;
    PotionData freezePotionData;
    PotionEffect freezeEffect;


    PotionEffectType speedType;
    PotionEffectType invisType;
    int durationSeconds = 10;
    int amplifier = 1;
    PotionEffect speed;
    PotionEffect invis;

    ItemStack socialDistanceStick;
    ItemMeta stickMeta;
    ItemStack swap;
    ItemMeta swapMeta;

    ItemStack shield;
    ItemMeta shieldMeta;

    List<Player> sheilded;


    public TntTagListener() {
        List<String> lore = new ArrayList<>();
        sheilded = new ArrayList<>();
        speedType = PotionEffectType.SPEED;
        invisType = PotionEffectType.INVISIBILITY;
            speed = new PotionEffect(speedType, durationSeconds * 20, amplifier);
        invis = new PotionEffect(invisType, durationSeconds * 20, amplifier);
        darkPotion = new ItemStack(Material.SPLASH_POTION);
        darkPotionMeta = (PotionMeta) darkPotion.getItemMeta();
        darkPotionData = new PotionData(PotionType.WATER);
        darkPotionMeta.setBasePotionData(darkPotionData);
        effectType = PotionEffectType.DARKNESS;
        effect = new PotionEffect(effectType, durationTicks, 10, ambient);
        darkPotionMeta.addCustomEffect(effect, true);
        darkPotionMeta.setDisplayName("Splash Potion Of Darkness");
        darkPotion.setItemMeta(darkPotionMeta);
        slowPotion = new ItemStack(Material.SPLASH_POTION);
        slowPotionMeta = (PotionMeta) slowPotion.getItemMeta();
        slowPotionData = new PotionData(PotionType.SLOWNESS);
        slowPotionMeta.setBasePotionData(slowPotionData);
        slowPotionMeta.setDisplayName("Splash Potion Of Slowness");
        slowPotion.setItemMeta(slowPotionMeta);
        glowPotion = new ItemStack(Material.SPLASH_POTION);
        glowPotionMeta = (PotionMeta) glowPotion.getItemMeta();
        glowPotionData = new PotionData(PotionType.WATER);
        glowPotionMeta.setBasePotionData(glowPotionData);
        gloweffectType = PotionEffectType.GLOWING;
        gloweffect = new PotionEffect(gloweffectType, durationTicks, 10, ambient);
        glowPotionMeta.addCustomEffect(gloweffect, true);
        glowPotionMeta.setDisplayName("Splash Potion Of Glowing");
        glowPotion.setItemMeta(glowPotionMeta);
        freezePotion = new ItemStack(Material.SPLASH_POTION);
        freezePotionMeta = (PotionMeta) freezePotion.getItemMeta();
        freezePotionData = new PotionData(PotionType.WATER);
        freezePotionMeta.setBasePotionData(freezePotionData);
        freezeEffect = new FreezePotionEffect(4 * 20);
        freezePotionMeta.addCustomEffect(freezeEffect, true);
        freezePotionMeta.setDisplayName("Freeze Potion");
        freezePotion.setItemMeta(freezePotionMeta);
        socialDistanceStick = new ItemStack(Material.STICK);
        stickMeta = socialDistanceStick.getItemMeta();
        stickMeta.addEnchant(Enchantment.KNOCKBACK, 100, true);
        stickMeta.setDisplayName("Social Distancing Stick");
        stickMeta.addEnchant(Enchantment.DURABILITY, 3, true);
        socialDistanceStick.setItemMeta(stickMeta);
        swap = new ItemStack(Material.BLAZE_ROD);
        swapMeta = swap.getItemMeta();
        swapMeta.setDisplayName("Swap Stick");
        lore.clear();
        lore.add("Swaps Places with a Random Player");
        swapMeta.setLore(lore);
        swap.setItemMeta(swapMeta);
        shield = new ItemStack(Material.AMETHYST_SHARD);
        shieldMeta = shield.getItemMeta();
        shieldMeta.setDisplayName("Shield Shard");
        lore.clear();
        lore.add("Gives you a shield for 5 seconds");
        shieldMeta.setLore(lore);
    }

    @EventHandler
    public void OnPlayerHit(EntityDamageByEntityEvent event){
        if (TntTagStart.hasStarted){
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
                Entity entity2 = event.getEntity();
                Entity entity = event.getDamager();
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Social Distancing Stick")) {
                        ItemStack stick = player.getInventory().getItemInMainHand();
                        ItemMeta stickymeta = stick.getItemMeta();
                        int level = stickymeta.getEnchantLevel(Enchantment.DURABILITY);
                        level--;
                        stickymeta.removeEnchant(Enchantment.DURABILITY);
                        if (level <= 0) {
                            player.getInventory().removeItem(stick);
                            return;
                        }
                        stickymeta.addEnchant(Enchantment.DURABILITY, level, true);
                        stick.setItemMeta(stickymeta);

                    } else {
                            if (TntTagStart.tntIt.contains(player)) {
                                if (player.getInventory().getItemInMainHand().equals(new ItemStack(Material.TNT))) {
                                    if (entity2 instanceof Player) {
                                        Player player2 = (Player) entity2;
                                        if (!sheilded.contains(player2)) {
                                            event.setCancelled(true);
                                            TntTagStart.Tag(player, player2);

                                            Bukkit.broadcastMessage(player2.getDisplayName() + " Was Tagged By " + player.getDisplayName());
                                        }else {
                                            event.setCancelled(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    @EventHandler
    public void OnBlockPlace(BlockPlaceEvent event){
        String name = event.getItemInHand().getType().name();

        if (name.equals("TNT") && TntTagStart.hasStarted){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        if (event.getItemDrop().getItemStack().equals(new ItemStack(Material.TNT)) && TntTagStart.hasStarted || event.getItemDrop().getItemStack().equals(new ItemStack(Material.LEATHER_CHESTPLATE)) && TntTagStart.hasStarted ){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Collection<ItemStack> becon = new ArrayList<>();
        becon.add(new ItemStack(Material.BEACON));
        Block block = event.getClickedBlock();
        if (block.getType() == Material.BEACON&& TntTagStart.hasStarted){
            powerups(event);
            Bukkit.broadcastMessage("Beacon");
            event.getClickedBlock().setType(Material.DRIED_KELP_BLOCK);
            new BukkitRunnable() {
                int secondsLeft = 10;
                @Override
                public void run() {
                    if (secondsLeft <= 0) {
                        event.getClickedBlock().setType(Material.BEACON);
                        cancel();
                        return;
                    }
                    secondsLeft--;
                }
            }.runTaskTimer(TntTag.INTANCE, 0L, 20L);
            event.setCancelled(true);
        }else if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Swap Stick")) {
            List<Player> players = new ArrayList<>(TntTagStart.players);
            players.remove(event.getPlayer());
            Random random = new Random();
            int randomNumber = random.nextInt(players.size());
            Player selectedPlayer = players.get(randomNumber);
            Location ourLocation = event.getPlayer().getLocation();
            event.getPlayer().teleport(selectedPlayer.getLocation());
            selectedPlayer.teleport(ourLocation);
        }else if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Shield Shard")){
            Player player = event.getPlayer();
            player.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
            player.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
            player.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
            sheilded.add(player);
            new BukkitRunnable() {
                int secondsLeft = 5;

                @Override
                public void run(){
                    if (secondsLeft <= 0) {
                        sheilded.remove(player);

                        player.getInventory().removeItem(new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE),new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS));
                        player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                        cancel();
                        return;
                    }
                    secondsLeft--;
                }
            };

        }
    }
    public void powerups(PlayerInteractEvent event){
        Random random = new Random();
        int rand = random.nextInt(TntTagStart.powerups.size());
        String string = TntTagStart.powerups.get(rand);
        switch (string) {
            default:
                break;
            case "speed":
                event.getPlayer().addPotionEffect(speed);
                break;
            case "invis":
                event.getPlayer().addPotionEffect(invis);
                break;
            case "darkness":
                event.getPlayer().getInventory().addItem(darkPotion);
                break;
            case "slowness":
                event.getPlayer().getInventory().addItem(slowPotion);
                break;
            case "glowing":
                event.getPlayer().getInventory().addItem(glowPotion);
                break;
            case "freeze":
                event.getPlayer().getInventory().addItem(freezePotion);
                break;
            case "stick":
                event.getPlayer().getInventory().addItem(socialDistanceStick);
                break;
            case "swap":
                event.getPlayer().getInventory().addItem(swap);
                break;
            case "shield":
                event.getPlayer().getInventory().addItem(shield);
                break;

        }

    }





}

