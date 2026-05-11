package com.conaxgames.libraries.timer;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.event.impl.timer.TimerExpireEvent;
import com.conaxgames.libraries.util.scheduler.Scheduler;

import java.util.UUID;

public class TimerCooldown {

    private final Timer timer;
    private final UUID owner;

    private Scheduler.CancellableTask scheduledTask;
    private long expiryMillis;
    private long pauseMillis;

    TimerCooldown(Timer timer, UUID owner, long duration) {
        this.timer = timer;
        this.owner = owner;
        setRemaining(duration);
    }

    public Timer getTimer() {
        return timer;
    }

    public long getExpiryMillis() {
        return expiryMillis;
    }

    public long getRemaining() {
        return getRemaining(false);
    }

    void setRemaining(long milliseconds) {
        if (milliseconds <= 0L) {
            cancel();
            return;
        }

        long newExpiry = System.currentTimeMillis() + milliseconds;
        if (newExpiry == expiryMillis) {
            return;
        }
        expiryMillis = newExpiry;
        cancel();

        long ticks = milliseconds / 50L;
        Scheduler scheduler = LibraryPlugin.getInstance().getScheduler();

        scheduledTask = scheduler.runTaskLaterCancellable(
                LibraryPlugin.getInstance().getPlugin(),
                this::expire,
                ticks
        );
    }

    long getRemaining(boolean ignorePaused) {
        if (!ignorePaused && pauseMillis != 0L) {
            return pauseMillis;
        }
        return expiryMillis - System.currentTimeMillis();
    }

    boolean isPaused() {
        return pauseMillis != 0L;
    }

    void setPaused(boolean paused) {
        if (paused == isPaused()) {
            return;
        }
        if (paused) {
            pauseMillis = getRemaining(true);
            cancel();
        } else {
            setRemaining(pauseMillis);
            pauseMillis = 0L;
        }
    }

    void cancel() {
        if (scheduledTask != null) {
            scheduledTask.cancel();
            scheduledTask = null;
        }
    }

    private void expire() {
        if (timer instanceof PlayerTimer playerTimer) {
            playerTimer.handleExpiry(
                    LibraryPlugin.getInstance().getPlugin().getServer().getPlayer(owner),
                    owner
            );
        }

        LibraryPlugin.getInstance().getPlugin().getServer().getPluginManager()
                .callEvent(new TimerExpireEvent(owner, timer));

        scheduledTask = null;
    }
}
