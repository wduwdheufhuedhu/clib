package com.conaxgames.libraries.message;

import org.bukkit.ChatColor;

public final class ProgressionBar {

    private ProgressionBar() {}

    public static String construct(int current, int max) {
        return construct(current, max, 20, '-', ChatColor.GREEN.toString(), ChatColor.GRAY.toString());
    }

    public static String construct(int current, int max, int totalBars) {
        return construct(current, max, totalBars, '-', ChatColor.GREEN.toString(), ChatColor.GRAY.toString());
    }

    public static String construct(int current, int max, int totalBars,
                                   char symbol, String completedColor, String notCompletedColor) {
        int clamped = Math.clamp(current, 0, max);
        float percent = max > 0 ? (float) clamped / max : 0f;
        int filled = (int) (totalBars * percent);
        int empty = totalBars - filled;

        var sym = String.valueOf(symbol);
        return (completedColor + sym).repeat(filled) + (notCompletedColor + sym).repeat(empty);
    }
}
