package xyz.n501yhappy.leashplayer.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static xyz.n501yhappy.leashplayer.LeashPlayer.LEASH_MAP;
import static xyz.n501yhappy.leashplayer.LeashUtils.getNeckLocation;

public class LeashListener implements Listener {
    private static final String CHICKEN_NAME = "§bcrychic";

    @EventHandler
    public void onLeash(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getEquipment().getItemInMainHand() == null ||
                event.getPlayer().getEquipment().getItemInMainHand().getType() != Material.LEAD) {
            return;
        }

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player)) return;
        Player target = (Player) entity;
        createLeash(player, target);
        player.playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 1.0f, 1.0f);
    }

    private void createLeash(Player player, Player target) {
        player.getEquipment().getItemInMainHand().setAmount(player.getEquipment().getItemInMainHand().getAmount() - 1);
        if (target == null || player == null || LEASH_MAP.containsKey(target)) {
            return;
        }

        Location loc = getNeckLocation(target);
        if (loc == null || loc.getWorld() == null) {
            return;
        }

        Chicken chicken = player.getWorld().spawn(loc, Chicken.class);//弄个鸡用来假装玩家被拴住 //TODO 碰撞想问题
        chicken.setAI(false);
        chicken.setGravity(false);
        chicken.setInvulnerable(true);
        chicken.setCollidable(false);
        chicken.setCustomNameVisible(false);
        chicken.setSilent(true);
        chicken.setPersistent(false);
        chicken.setLeashHolder(player);
        chicken.setInvisible(true);
        chicken.setCustomName(CHICKEN_NAME);
        LEASH_MAP.put(target, chicken);
    }

}
