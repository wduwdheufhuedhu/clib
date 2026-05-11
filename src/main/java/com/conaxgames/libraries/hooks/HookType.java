package com.conaxgames.libraries.hooks;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public enum HookType {

    PLACEHOLDERAPI,
    VAULT,
    TITLE_MANAGER,
    WORLD_GUARD,
    PROTOCOLLIB,
    CMI,
    TAB,
    LUCKPERMS,
    CITIZENS,

    CSUITE,
    CMIGRATION,
    ANTICRASH,
    ARENAPVP,
    UHC,
    MANGO,
    HCF,
    KITPVP,
    FFA,
    CGLOBE,
    SKYBLOCK;

    private static final Map<String, HookType> BY_NORMALIZED_NAME;

    static {
        HashMap<String, HookType> map = new HashMap<>();
        for (HookType type : values()) {
            map.put(normalize(type.name()), type);
        }
        BY_NORMALIZED_NAME = Map.copyOf(map);
    }

    private static String normalize(String name) {
        return name.toLowerCase(Locale.ROOT).replace("_", "");
    }

    public static Optional<HookType> fromPluginName(String name) {
        return Optional.ofNullable(BY_NORMALIZED_NAME.get(normalize(name)));
    }
}
