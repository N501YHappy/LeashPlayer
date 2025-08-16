package xyz.n501yhappy.leashplayer.tasks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;

import static xyz.n501yhappy.leashplayer.LeashPlayer.LEASH_MAP;
import static xyz.n501yhappy.leashplayer.LeashUtils.breakLeash;
import static xyz.n501yhappy.leashplayer.LeashUtils.getNeckLocation;

public class PullLis extends BukkitRunnable {//给拉力的
    private static final double MIN_PULL_DISTANCE = 3.5;//拉扯距离
    private static final double MAX_PULL_SPEED = 2.0;
    private static final double CNUM = 0.5;
    private static final double JUMP_HEIGHT = 0.45;
    private static final double GRAVITY = 0.05;

    @Override
    public void run() {
        for (Map.Entry<Player, Chicken> entry : LEASH_MAP.entrySet()) {
            Player player = entry.getKey();
            Chicken chicken = entry.getValue();

            if (player == null || !player.isOnline() ||
                chicken == null || !chicken.isValid() || !chicken.isLeashed()) {
                continue;
            }

            Location pLoc = player.getLocation();
            Location cLoc = chicken.getLocation();

            if (pLoc.distance(cLoc) > 0.1) {
                chicken.teleport(getNeckLocation(player));
            }

            Entity leashHolder = chicken.getLeashHolder();
            if (leashHolder == null) {//是空的直接拉断
                breakLeash(player, chicken);
                continue;
            }

            Location hLoc = leashHolder.getLocation();
            double distance = pLoc.distance(hLoc);

            if (distance > MIN_PULL_DISTANCE) {
                Vector direction = hLoc.toVector().subtract(pLoc.toVector());
                double speed = Math.min((distance - MIN_PULL_DISTANCE) * CNUM, MAX_PULL_SPEED);
                Vector velocity = direction.normalize().multiply(speed);

                velocity.setY(velocity.getY() - GRAVITY);

                if (shouldJump(player, pLoc, hLoc) && isOnGround(player) && !player.isJumping()) {
                    velocity.setY(JUMP_HEIGHT);//模拟一次跳跃。。
                    player.setJumping(true);
                }

                player.setVelocity(velocity);
            }
        }
    }

    public boolean shouldJump(Player player, Location pLoc, Location hLoc) {
        double heightDiff = hLoc.getY() - pLoc.getY();
        if (heightDiff > 0.25) {
            return true;
        }

        return false;
    }

    public boolean isOnGround(Player player) {
        Location loc = player.getLocation();
        Block underBlock = loc.getBlock().getRelative(BlockFace.DOWN);

        return !isAir(underBlock.getType());
    }

    public boolean isAir(Material material) {
        return material == Material.AIR ||
               material == Material.CAVE_AIR ||
               material == Material.VOID_AIR;
    }
}
