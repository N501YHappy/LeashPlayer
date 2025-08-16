package xyz.n501yhappy.leashplayer.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import static xyz.n501yhappy.leashplayer.LeashPlayer.LEASH_MAP;
import static xyz.n501yhappy.leashplayer.LeashUtils.*;

public class UnleashListener implements Listener {

    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent event) {
        if (event.getEntity() instanceof Chicken) {
            Chicken chicken = (Chicken) event.getEntity();

            for (Player player : LEASH_MAP.keySet()) {
                if (LEASH_MAP.get(player) == chicken) {
                    breakLeash(player, chicken);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (LEASH_MAP.containsKey(player)) {
            Chicken chicken = LEASH_MAP.get(player);
            breakLeash(player, chicken);
        }

        for (Player leashedPlayer : LEASH_MAP.keySet()) {
            Chicken chicken = LEASH_MAP.get(leashedPlayer);
            if (chicken != null && chicken.getLeashHolder() == player) {
                breakLeash(leashedPlayer, chicken);
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (LEASH_MAP.containsKey(player)) {
            Chicken chicken = LEASH_MAP.get(player);
            if (chicken != null && (!chicken.isLeashed() || chicken.getLeashHolder() == null)) {
                breakLeash(player, chicken);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player)) return;

        Player target = (Player) event.getRightClicked();

        if (LEASH_MAP.containsKey(target)) {
            Chicken chicken = LEASH_MAP.get(target);

            if (chicken != null && chicken.isLeashed() && player.getEquipment().getItemInMainHand().getType() != Material.LEAD) {
                breakLeash(target, chicken);
            }
        }
    }
}
