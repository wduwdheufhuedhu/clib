package com.conaxgames.libraries.hooks;

import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * Immutable representation of a detected third-party plugin integration.
 */
public record Hook(HookType type, Plugin plugin) {

    public Hook {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(plugin, "plugin");
    }
}
