package xyz.n501yhappy.leashplayer.tasks;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static xyz.n501yhappy.leashplayer.LeashPlayer.LEASH_MAP;
import static xyz.n501yhappy.leashplayer.LeashUtils.breakLeash;

public class UnleashLis extends BukkitRunnable {//处理listener里面处理不了的或者我懒得做的一下情况，遍历map每个映射查看拴绳情况

    @Override
    public void run() {
        java.util.List<Player> removeList = new java.util.ArrayList<>();

        for (java.util.Map.Entry<Player, Chicken> entry : LEASH_MAP.entrySet()) {
            Player player = entry.getKey();
            Chicken chicken = entry.getValue();

            if (chicken == null) {
                removeList.add(player);
                continue;
            }

            if (!chicken.isValid() || !chicken.isLeashed()) {
                breakLeash(player, chicken);
                removeList.add(player);
            }
        }

        for (Player player : removeList) {
            LEASH_MAP.remove(player);
        }
    }
}
