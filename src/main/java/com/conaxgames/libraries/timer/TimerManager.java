package com.conaxgames.libraries.timer;

import com.conaxgames.libraries.LibraryPlugin;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Registry that holds all active {@link Timer} instances.
 *
 * <p>Timers that implement {@link Listener} are automatically
 * registered with the Bukkit event bus on {@link #registerTimer}.
 */
public final class TimerManager {

    private final Set<Timer> timers = new LinkedHashSet<>();

    public void registerTimer(Timer timer) {
        timers.add(timer);
        if (timer instanceof Listener listener) {
            LibraryPlugin.getInstance().getPlugin().getServer().getPluginManager()
                    .registerEvents(listener, LibraryPlugin.getInstance().getPlugin());
        }
    }

    public void unregisterTimer(Timer timer) {
        timers.remove(timer);
    }

    @SuppressWarnings("unchecked")
    public <T extends Timer> T getTimer(Class<T> timerClass) {
        for (var timer : timers) {
            if (timerClass.isInstance(timer)) {
                return (T) timer;
            }
        }
        return null;
    }

    public Set<Timer> getTimers() {
        return Collections.unmodifiableSet(timers);
    }
}
