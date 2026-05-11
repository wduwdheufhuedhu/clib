package com.conaxgames.libraries.timer;

import com.conaxgames.libraries.message.TimeUtil;
import com.conaxgames.libraries.util.CC;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lightweight, static cooldown manager keyed by player {@link UUID}
 * and ability name.  Thread-safe via {@link ConcurrentHashMap}.
 *
 * <p>Use this for simple cooldowns that don't need the full
 * {@link PlayerTimer} lifecycle events.
 */
public final class QuickTimerManager {

    private static final ConcurrentHashMap<UUID, ConcurrentHashMap<String, QuickTimerType>> timers =
            new ConcurrentHashMap<>();

    private QuickTimerManager() {}

    // -- add -------------------------------------------------------------

    public static void addTimer(Player player, String ability, long time) {
        addTimer(player.getUniqueId(), ability, time, false);
    }

    public static void addTimer(Player player, String ability, long time, boolean announce) {
        addTimer(player.getUniqueId(), ability, time, announce);
    }

    public static void addTimer(UUID uuid, String ability, long time) {
        addTimer(uuid, ability, time, false);
    }

    public static void addTimer(UUID uuid, String ability, long time, boolean announce) {
        timers.computeIfAbsent(uuid, _ -> new ConcurrentHashMap<>())
                .putIfAbsent(ability, new QuickTimerType(System.currentTimeMillis() + time, announce));
    }

    // -- query -----------------------------------------------------------

    public static boolean hasTimer(Player player, String ability) {
        return hasTimer(player.getUniqueId(), ability);
    }

    public static boolean hasTimer(UUID uuid, String ability) {
        var abilities = timers.get(uuid);
        if (abilities == null) {
            return false;
        }
        var entry = abilities.get(ability);
        if (entry == null) {
            return false;
        }
        if (entry.isExpired()) {
            abilities.remove(ability);
            return false;
        }
        return true;
    }

    public static boolean hasAndMessage(Player player, String ability) {
        if (!hasTimer(player, ability)) {
            return false;
        }
        long remaining = getRemaining(player.getUniqueId(), ability);
        player.sendMessage(CC.RED + "You must wait " + CC.B_RED
                + TimeUtil.timeAsString(remaining) + CC.RED + " before doing this again.");
        return true;
    }

    // -- remaining -------------------------------------------------------

    public static long getRemaining(UUID uuid, String ability) {
        var abilities = timers.get(uuid);
        if (abilities == null) {
            return 0L;
        }
        var entry = abilities.get(ability);
        return entry == null ? 0L : entry.getRemaining();
    }

    // -- remove ----------------------------------------------------------

    public static void removeTimer(Player player, String ability) {
        removeTimer(player.getUniqueId(), ability);
    }

    public static void removeTimer(UUID uuid, String ability) {
        var abilities = timers.get(uuid);
        if (abilities != null) {
            abilities.remove(ability);
        }
    }

    // -- maintenance -----------------------------------------------------

    /**
     * Purges all expired entries across every tracked player.
     */
    public static void clearExpired() {
        timers.forEach((_, abilities) ->
                abilities.entrySet().removeIf(e -> e.getValue().isExpired()));
        timers.entrySet().removeIf(e -> e.getValue().isEmpty());
    }

    /**
     * @deprecated renamed to {@link #clearExpired()} for clarity
     */
    @Deprecated(forRemoval = true)
    public static void clearCacheCooldown() {
        clearExpired();
    }
}
