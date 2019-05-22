package com.mcspacecraft.itemmagnet.util;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerUtils {
    private static final Set<UUID> frozenPlayers = new HashSet<>();

    /**
     * Completely resets to default player health, walk speed, potion effects,
     * inventory and armor slots.
     *
     * @param player Player to reset
     */
    public static void fullyResetPlayer(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        // reset player health
        topPlayerHealth(player);

        // reset flying options
        resetFlyingOptions(player);

        // clear player potion effects
        clearPlayerPotionEffects(player);

        // clear player inventory
        clearPlayerInventory(player);

        /*
         * setWalkSpeed
         * removePotionEffect
         */
        unfreezePlayer(player);

        /* setMaximumAir */
        player.setRemainingAir(player.getMaximumAir());

        /* set game mode to survival */
        setGameModeToSurvival(player);

        // Default walk speed.
        player.setWalkSpeed(0.2f);

        // Remove any glow effects
        player.setGlowing(false);
    }

    /**
     * Resets a player back to survival mode.
     *
     * @param player
     */
    public static void setGameModeToSurvival(final Player player) {
        player.setGameMode(GameMode.SURVIVAL);
    }

    /**
     * Turns off all flying options for a player.
     *
     * @param player
     */
    public static void resetFlyingOptions(final Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    /**
     * Fully heals and feeds player.
     *
     * @param player
     */
    @SuppressWarnings("deprecation")
    public static void topPlayerHealth(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        player.setMaxHealth(20);
        // getMaxHealth() = 20
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(10);

        player.setFireTicks(0);
        player.setFallDistance(0);

    }

    /**
     * Clears all potion effects from player.
     *
     * @param player Player to clear potion effects from
     */
    public static void clearPlayerPotionEffects(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    /**
     * Empties player inventory.
     *
     * @param player Player to clear inventory for
     */
    public static void clearPlayerInventory(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        final PlayerInventory inventory = player.getInventory();
        inventory.clear();
        // added setArmorContents
        inventory.setArmorContents(new ItemStack[4]);
        inventory.setBoots(null);
        inventory.setChestplate(null);
        inventory.setHelmet(null);
        inventory.setLeggings(null);
        inventory.setItemInMainHand(null);
        inventory.setItemInOffHand(null);
        player.setItemOnCursor(null);
    }

    /**
     * Drops all items from player inventory on the ground, armor included, and
     * then clears player inventory.
     *
     * @param player Player to drop items from
     */
    public static void dropAllItems(final Player player) {
        dropAllItems(player, true);
    }

    /**
     * Drops all items from player inventory on the ground, armor included, and
     * then clears player inventory.
     *
     * @param player Player to drop items from
     * @param dropArmor whether or not to drop armor.
     */
    public static void dropAllItems(final Player player, boolean dropArmor) {
        if (player == null || !player.isOnline()) {
            return;
        }
        final Location playerLocation = player.getLocation();
        final World world = playerLocation.getWorld();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                world.dropItemNaturally(playerLocation, item);
            }
        }
        if (dropArmor) {
            for (ItemStack item : player.getEquipment().getArmorContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    world.dropItemNaturally(playerLocation, item);
                }
            }
        }
        ItemStack cursor = player.getItemOnCursor();
        if (cursor != null && cursor.getType() != Material.AIR) {
            world.dropItemNaturally(playerLocation, cursor);
        }
        clearPlayerInventory(player);
    }

    /**
     * Prevents specified player from moving.
     *
     * @param player
     */
    public static void freezePlayer(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        frozenPlayers.add(player.getUniqueId());
    }

    /**
     * Allows player to move normally.
     *
     * @param player
     */
    public static void unfreezePlayer(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }

        frozenPlayers.remove(player.getUniqueId());
    }

    /**
     * Check if a player is frozen.
     *
     * @param player
     *
     * @return true if player is frozen, otherwise false.
     */
    public static boolean isFrozen(final Player player) {
        return player != null && player.isOnline() && frozenPlayers.contains(player.getUniqueId());
    }

    /**
     * Blinds specified player.
     *
     * @param player
     */
    public static void blindPlayer(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 5));
    }

    /**
     * Allows player to see again.
     *
     * @param player
     */
    public static void unblindPlayer(final Player player) {
        if (player == null || !player.isOnline()) {
            return;
        }
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    /**
     * Resets all armor damage for specified player.
     *
     * @param player Player to remove armor damage from
     */
    @SuppressWarnings("deprecation")
    public static void resetArmorDamage(final Player player) {
        if (player == null) {
            return;
        }

        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                item.setDurability((short) 0);
            }
        }

        player.updateInventory();
    }
}
