package com.conaxgames.libraries.message;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public final class ItemNameUtil {

    private static final Map<String, String> POTION_NAMES = Map.ofEntries(
            Map.entry("speed", "Speed"),
            Map.entry("slowness", "Slowness"),
            Map.entry("haste", "Haste"),
            Map.entry("mining_fatigue", "Mining Fatigue"),
            Map.entry("strength", "Strength"),
            Map.entry("instant_health", "Instant Health"),
            Map.entry("instant_damage", "Instant Damage"),
            Map.entry("jump_boost", "Jump Boost"),
            Map.entry("nausea", "Nausea"),
            Map.entry("regeneration", "Regeneration"),
            Map.entry("resistance", "Resistance"),
            Map.entry("fire_resistance", "Fire Resistance"),
            Map.entry("water_breathing", "Water Breathing"),
            Map.entry("invisibility", "Invisibility"),
            Map.entry("blindness", "Blindness"),
            Map.entry("night_vision", "Night Vision"),
            Map.entry("hunger", "Hunger"),
            Map.entry("weakness", "Weakness"),
            Map.entry("poison", "Poison"),
            Map.entry("wither", "Wither"),
            Map.entry("health_boost", "Health Boost"),
            Map.entry("absorption", "Absorption"),
            Map.entry("saturation", "Saturation")
    );

    private ItemNameUtil() {}

    public static String potionLookup(PotionEffectType potion) {
        var result = POTION_NAMES.get(potion.getKey().getKey());
        return result != null ? result : capitalizeFully(potion.getKey().getKey());
    }

    public static String enchantLookup(Enchantment enchantment, Player player) {
        return capitalizeFully(enchantment.getKey().getKey());
    }

    private static String capitalizeFully(String input) {
        var words = input.replace('_', ' ').replace('-', ' ').split(" ");
        var sb = new StringBuilder(input.length());
        for (int i = 0; i < words.length; i++) {
            if (i > 0) sb.append(' ');
            var word = words[i];
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
}
