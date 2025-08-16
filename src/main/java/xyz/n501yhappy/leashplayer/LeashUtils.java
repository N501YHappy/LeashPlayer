package xyz.n501yhappy.leashplayer;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;

import static xyz.n501yhappy.leashplayer.LeashPlayer.LEASH_MAP;

public class LeashUtils {
    public static final float SOUND_VOLUME = 1.0f;
    public static final float SOUND_PITCH = 1.0f;

    public static Location getNeckLocation(Player player) {
        return player.getLocation().add(0, 0.75, 0);
    }

    public static void breakLeash(Player player, Chicken chicken) {
        if (chicken != null && chicken.isValid()) {
            chicken.setLeashHolder(null);
            chicken.remove();
        }

        LEASH_MAP.remove(player);

        if (player != null && player.isOnline()) {
            player.playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_BREAK, SOUND_VOLUME, SOUND_PITCH);
        }
    }
}
