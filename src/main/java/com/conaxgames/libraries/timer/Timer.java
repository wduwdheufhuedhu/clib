package com.conaxgames.libraries.timer;

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
