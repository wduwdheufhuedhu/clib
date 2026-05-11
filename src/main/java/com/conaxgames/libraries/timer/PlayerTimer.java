package com.conaxgames.libraries.timer;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.event.impl.timer.TimerClearEvent;
import com.conaxgames.libraries.event.impl.timer.TimerExtendEvent;
import com.conaxgames.libraries.event.impl.timer.TimerPauseEvent;
import com.conaxgames.libraries.event.impl.timer.TimerStartEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongPredicate;

/**
 * A {@link Timer} that tracks per-player cooldowns backed by
 * {@link TimerCooldown} instances in a {@link ConcurrentHashMap}.
 *
 * <p>{@code non-sealed} so downstream code can provide concrete
 * timer implementations (e.g. combat tag, enderpearl cooldown).
 */
public non-sealed abstract class PlayerTimer extends Timer {

    private final Map<UUID, TimerCooldown> cooldowns = new ConcurrentHashMap<>();

    protected PlayerTimer(String name, long defaultCooldown) {
        super(name, defaultCooldown);
    }

    protected void handleExpiry(@Nullable Player player, UUID playerUUID) {
        cooldowns.remove(playerUUID);
    }

    // -- clear -----------------------------------------------------------

    public TimerCooldown clearCooldown(UUID playerUUID) {
        return clearCooldown(null, playerUUID);
    }

    public TimerCooldown clearCooldown(Player player) {
        return clearCooldown(player, player.getUniqueId());
    }

    public TimerCooldown clearCooldown(@Nullable Player player, UUID playerUUID) {
        var cooldown = cooldowns.remove(playerUUID);
        if (cooldown != null) {
            cooldown.cancel();
            dispatch(new TimerClearEvent(player, playerUUID, this));
        }
        return cooldown;
    }

    // -- pause -----------------------------------------------------------

    public boolean isPaused(Player player) {
        return isPaused(player.getUniqueId());
    }

    public boolean isPaused(UUID playerUUID) {
        var cooldown = cooldowns.get(playerUUID);
        return cooldown != null && cooldown.isPaused();
    }

    public void setPaused(UUID playerUUID, boolean paused) {
        var cooldown = cooldowns.get(playerUUID);
        if (cooldown != null && cooldown.isPaused() != paused) {
            var event = new TimerPauseEvent(playerUUID, this, paused);
            dispatch(event);
            if (!event.isCancelled()) {
                cooldown.setPaused(paused);
            }
        }
    }

    // -- remaining -------------------------------------------------------

    public long getRemaining(Player player) {
        return getRemaining(player.getUniqueId());
    }

    public long getRemaining(UUID playerUUID) {
        var cooldown = cooldowns.get(playerUUID);
        return cooldown == null ? 0L : cooldown.getRemaining();
    }

    // -- set cooldown ----------------------------------------------------

    public boolean setCooldown(Player player, UUID playerUUID) {
        return setCooldown(player, playerUUID, getDefaultCooldown(), false);
    }

    public boolean setCooldown(Player player, UUID playerUUID, long duration, boolean overwrite) {
        return setCooldown(player, playerUUID, duration, overwrite, null);
    }

    public boolean setCooldown(@Nullable Player player, UUID playerUUID, long duration,
                               boolean overwrite, @Nullable LongPredicate currentCooldownPredicate) {
        if (duration <= 0L) {
            clearCooldown(player, playerUUID);
            return false;
        }

        var existing = cooldowns.get(playerUUID);
        if (existing != null) {
            long remaining = existing.getRemaining();
            if (!overwrite && remaining > 0L && duration <= remaining) {
                return false;
            }

            var extendEvent = new TimerExtendEvent(player, playerUUID, this, remaining, duration);
            dispatch(extendEvent);
            if (extendEvent.isCancelled()) {
                return false;
            }

            if (currentCooldownPredicate != null && !currentCooldownPredicate.test(remaining)) {
                return false;
            }

            existing.setRemaining(duration);
            return true;
        }

        var startEvent = new TimerStartEvent(player, playerUUID, this, duration);
        dispatch(startEvent);
        if (startEvent.isCancelled()) {
            return false;
        }

        cooldowns.put(playerUUID, new TimerCooldown(this, playerUUID, duration));
        return true;
    }

    // -- access ----------------------------------------------------------

    public Map<UUID, TimerCooldown> getCooldowns() {
        return cooldowns;
    }

    // -- internal --------------------------------------------------------

    private static void dispatch(Event event) {
        LibraryPlugin.getInstance().getPlugin().getServer().getPluginManager().callEvent(event);
    }
}
