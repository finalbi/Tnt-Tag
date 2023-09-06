package org.finalbi.tnttag;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class FreezePotionEffect extends PotionEffect {
    public FreezePotionEffect(int duration) {
        super(PotionEffectType.SLOW, duration, 1, false, true);
    }

    @Override
    public boolean apply(LivingEntity entity) {
        if (entity instanceof Player){
            Player player = (Player) entity;
            player.setWalkSpeed(0f);
            Location location = player.getLocation();
            Material material = location.getBlock().getType();
            location.getBlock().setType(Material.POWDER_SNOW);
            new BukkitRunnable() {

                @Override
                public void run() {
                    location.getBlock().setType(material);
                }
            }.runTaskLater(TntTag.INTANCE, getDuration());
        }
        return entity.addPotionEffect(this);
    }


}
