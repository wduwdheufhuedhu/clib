package com.conaxgames.libraries.util.scheduler;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Unified scheduling abstraction for Paper (1.8.8–26.1.2) and Folia (26.1.2).
 *
 * <p>Every method returns a {@link Task} handle; callers that do not need
 * cancellation simply discard the return value.</p>
 */
public sealed interface Scheduler {

    static Scheduler create(Server server) {
        try {
            if (Class.forName("io.papermc.paper.threadedregions.RegionizedServer").isInstance(server)) {
                return new Folia();
            }
        } catch (ClassNotFoundException _) {}
        return new Paper();
    }

    Task run(Plugin plugin, Runnable task);

    Task runAsync(Plugin plugin, Runnable task);

    Task runLater(Plugin plugin, Runnable task, long delayTicks);

    Task runLaterAsync(Plugin plugin, Runnable task, long delayTicks);

    Task runTimer(Plugin plugin, Runnable task, long delayTicks, long periodTicks);

    Task runTimerAsync(Plugin plugin, Runnable task, long delayTicks, long periodTicks);

    void cancelAll(Plugin plugin);

    interface Task {
        void cancel();

        boolean cancelled();
    }

    // ── Paper / Bukkit backend (1.8.8 – 26.1.2) ────────────────────────

    final class Paper implements Scheduler {

        private record WrappedTask(BukkitTask handle) implements Task {
            @Override public void cancel() { handle.cancel(); }
            @Override public boolean cancelled() { return handle.isCancelled(); }
        }

        private static BukkitScheduler scheduler(Plugin plugin) {
            return plugin.getServer().getScheduler();
        }

        @Override public Task run(Plugin plugin, Runnable task) {
            return new WrappedTask(scheduler(plugin).runTask(plugin, task));
        }

        @Override public Task runAsync(Plugin plugin, Runnable task) {
            return new WrappedTask(scheduler(plugin).runTaskAsynchronously(plugin, task));
        }

        @Override public Task runLater(Plugin plugin, Runnable task, long delayTicks) {
            return new WrappedTask(scheduler(plugin).runTaskLater(plugin, task, delayTicks));
        }

        @Override public Task runLaterAsync(Plugin plugin, Runnable task, long delayTicks) {
            return new WrappedTask(scheduler(plugin).runTaskLaterAsynchronously(plugin, task, delayTicks));
        }

        @Override public Task runTimer(Plugin plugin, Runnable task, long delayTicks, long periodTicks) {
            return new WrappedTask(scheduler(plugin).runTaskTimer(plugin, task, delayTicks, periodTicks));
        }

        @Override public Task runTimerAsync(Plugin plugin, Runnable task, long delayTicks, long periodTicks) {
            return new WrappedTask(scheduler(plugin).runTaskTimerAsynchronously(plugin, task, delayTicks, periodTicks));
        }

        @Override public void cancelAll(Plugin plugin) {
            scheduler(plugin).cancelTasks(plugin);
        }
    }

    // ── Folia backend (regionised scheduler) ────────────────────────────

    final class Folia implements Scheduler {

        private static final long MS_PER_TICK = 50L;

        private record WrappedTask(ScheduledTask handle) implements Task {
            @Override public void cancel() { handle.cancel(); }
            @Override public boolean cancelled() { return handle.isCancelled(); }
        }

        private static Consumer<ScheduledTask> wrap(Runnable task) {
            return _ -> task.run();
        }

        private static GlobalRegionScheduler global(Plugin plugin) {
            return plugin.getServer().getGlobalRegionScheduler();
        }

        private static AsyncScheduler async(Plugin plugin) {
            return plugin.getServer().getAsyncScheduler();
        }

        @Override public Task run(Plugin plugin, Runnable task) {
            return new WrappedTask(global(plugin).run(plugin, wrap(task)));
        }

        @Override public Task runAsync(Plugin plugin, Runnable task) {
            return new WrappedTask(async(plugin).runNow(plugin, wrap(task)));
        }

        @Override public Task runLater(Plugin plugin, Runnable task, long delayTicks) {
            return new WrappedTask(global(plugin).runDelayed(plugin, wrap(task), delayTicks));
        }

        @Override public Task runLaterAsync(Plugin plugin, Runnable task, long delayTicks) {
            return new WrappedTask(async(plugin).runDelayed(
                    plugin, wrap(task), delayTicks * MS_PER_TICK, TimeUnit.MILLISECONDS));
        }

        @Override public Task runTimer(Plugin plugin, Runnable task, long delayTicks, long periodTicks) {
            return new WrappedTask(global(plugin).runAtFixedRate(plugin, wrap(task), delayTicks, periodTicks));
        }

        @Override public Task runTimerAsync(Plugin plugin, Runnable task, long delayTicks, long periodTicks) {
            return new WrappedTask(async(plugin).runAtFixedRate(
                    plugin, wrap(task), delayTicks * MS_PER_TICK, periodTicks * MS_PER_TICK, TimeUnit.MILLISECONDS));
        }

        @Override public void cancelAll(Plugin plugin) {
            global(plugin).cancelTasks(plugin);
            async(plugin).cancelTasks(plugin);
        }
    }
}
