package com.conaxgames.libraries.hooks;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public enum HookType {

    PLACEHOLDERAPI("PlaceholderAPI"),
    VAULT("Vault"),
    TITLE_MANAGER("TitleManager"),
    WORLD_GUARD("WorldGuard"),
    PROTOCOLLIB("ProtocolLib"),
    CMI("CMI"),
    TAB("TAB"),
    LUCKPERMS("LuckPerms"),
    CITIZENS("Citizens"),

    CSUITE("cSuite"),
    CMIGRATION("cMigration"),
    ANTICRASH("AntiCrash"),
    ARENAPVP("ArenaPvP"),
    UHC("UHC"),
    MANGO("Mango"),
    HCF("HCF"),
    KITPVP("KitPvP"),
    FFA("FFA"),
    CGLOBE("cGlobe"),
    SKYBLOCK("Skyblock");

    private static final Map<String, HookType> BY_PLUGIN_NAME;

    static {
        HashMap<String, HookType> map = new HashMap<>();
        for (HookType type : values()) {
            map.put(type.pluginName.toLowerCase(Locale.ROOT), type);
        }
        BY_PLUGIN_NAME = Map.copyOf(map);
    }

    private final String pluginName;

    HookType(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * @return the canonical plugin name as registered with Bukkit
     */
    public String pluginName() {
        return pluginName;
    }

    /**
     * Resolves a {@link HookType} from a Bukkit plugin name (case-insensitive).
     */
    public static Optional<HookType> fromPluginName(String name) {
        return Optional.ofNullable(BY_PLUGIN_NAME.get(name.toLowerCase(Locale.ROOT)));
    }
}
