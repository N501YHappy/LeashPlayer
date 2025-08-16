package xyz.n501yhappy.leashplayer;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.n501yhappy.leashplayer.listeners.*;
import xyz.n501yhappy.leashplayer.tasks.PullLis;
import xyz.n501yhappy.leashplayer.tasks.UnleashLis;

import java.util.HashMap;
import java.util.Map;

public final class LeashPlayer extends JavaPlugin {
    public static final Map<Player, Chicken> LEASH_MAP = new HashMap<>();
    private UnleashLis unleashTask;
    private PullLis pullTask;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new LeashListener(), this);
        getServer().getPluginManager().registerEvents(new UnleashListener(), this);

        unleashTask = new UnleashLis();
        unleashTask.runTaskTimer(this, 20L, 20L);
        pullTask = new PullLis();
        pullTask.runTaskTimer(this, 1L, 1L);
    }

    @Override
    public void onDisable() {
        if (unleashTask != null) {
            unleashTask.cancel();
        }
        if (pullTask != null) {
            pullTask.cancel();
        }

        for (Chicken chicken : LEASH_MAP.values()) {
            if (chicken != null) {
                chicken.setLeashHolder(null);
                chicken.remove();
            }
        }
        LEASH_MAP.clear();
    }
}
