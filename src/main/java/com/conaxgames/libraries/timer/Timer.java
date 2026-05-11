package com.conaxgames.libraries.timer;

/**
 * Base type for all managed timers in the library.
 *
 * <p>Sealed to {@link PlayerTimer} which is itself {@code non-sealed},
 * so downstream code extends {@code PlayerTimer} for concrete timers.
 */
public sealed abstract class Timer permits PlayerTimer {

    private final String name;
    private final long defaultCooldown;

    protected Timer(String name, long defaultCooldown) {
        this.name = name;
        this.defaultCooldown = defaultCooldown;
    }

    public final String getDisplayName() {
        return name;
    }

    public final long getDefaultCooldown() {
        return defaultCooldown;
    }
}
