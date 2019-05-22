package com.mcspacecraft.itemmagnet.util;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class WorldUtils {
    /**
     * Stops fire from spreading in all worlds.
     */
    public static void stopFireSpread() {
        for (World world : Bukkit.getServer().getWorlds()) {
            world.setGameRuleValue("doFireTick", "false");
        }
    }

    /**
     * Allows fire to spread in all worlds.
     */
    public static void startFireSpread() {
        for (World world : Bukkit.getServer().getWorlds()) {
            world.setGameRuleValue("doFireTick", "true");
        }
    }

    /**
     * Removes all items, projectiles and experience orbs from the ground on a specified world.
     *
     * @param world World to remove drops from
     */
    public static void removeDrops(World world) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Item || entity instanceof Projectile || entity instanceof ExperienceOrb) {
                entity.remove();
            }
        }
    }

    /**
     * Removes all living entities that aren't players from the specified world.
     *
     * @param world World to remove living entities from
     */
    public static void removeEntities(World world) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                EntityDeathEvent event = new EntityDeathEvent((LivingEntity) entity, Collections.<ItemStack> emptyList());
                Bukkit.getPluginManager().callEvent(event);
                entity.remove();
            }
        }
    }
}
