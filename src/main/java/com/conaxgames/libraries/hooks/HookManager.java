package com.conaxgames.libraries.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Discovers and exposes third-party plugin integrations that are present on the server.
 * <p>
 * Hooks are resolved once at construction time by matching each loaded plugin's name
 * against the known {@link HookType} entries. All lookups are O(1) via {@link EnumMap}.
 */
public final class HookManager {

    private final Map<HookType, Hook> hooks;

    public HookManager(JavaPlugin plugin) {
        EnumMap<HookType, Hook> discovered = new EnumMap<>(HookType.class);
        for (Plugin p : plugin.getServer().getPluginManager().getPlugins()) {
            HookType.fromPluginName(p.getName())
                    .ifPresent(type -> discovered.put(type, new Hook(type, p)));
        }
        this.hooks = Collections.unmodifiableMap(discovered);
    }

    /**
     * @return the hook for the given type, or empty if the plugin is not present
     */
    public Optional<Hook> getHook(HookType type) {
        return Optional.ofNullable(hooks.get(type));
    }

    /**
     * @return the hook matching the given Bukkit plugin name (case-insensitive),
     *         or empty if no matching hook type exists or the plugin is not present
     */
    public Optional<Hook> getHook(String pluginName) {
        return HookType.fromPluginName(pluginName).flatMap(this::getHook);
    }

    /**
     * @return {@code true} if a plugin matching the given hook type was detected
     */
    public boolean isHooked(HookType type) {
        return hooks.containsKey(type);
    }

    /**
     * @return an unmodifiable view of all detected hook types
     */
    public Set<HookType> hookedTypes() {
        return hooks.keySet();
    }

    /**
     * @return an unmodifiable view of all detected hooks
     */
    public Collection<Hook> hooks() {
        return hooks.values();
    }

    /**
     * @return every loaded plugin that declares {@code cLibraries} as a hard dependency
     */
    public List<Plugin> dependents() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(p -> p.getDescription().getDepend().contains("cLibraries"))
                .toList();
    }
}
