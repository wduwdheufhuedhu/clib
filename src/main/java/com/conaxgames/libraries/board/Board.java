package com.conaxgames.libraries.board;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Board {

    private static final String OBJECTIVE_NAME = "sb";
    private static final String DUMMY_CRITERIA = "dummy";

    private static final String[] ENTRY_KEYS;

    static {
        var codes = "0123456789abcdefklmor";
        ENTRY_KEYS = new String[codes.length()];
        for (int i = 0; i < codes.length(); i++) {
            ENTRY_KEYS[i] = "\u00a7" + codes.charAt(i) + "\u00a7f";
        }
    }

    private final BoardLimits limits;
    private final List<BoardEntry> entries = new ArrayList<>();
    private final Set<String> usedKeys = ConcurrentHashMap.newKeySet();
    private final Scoreboard scoreboard;
    private final Objective objective;
    private volatile String lastTitle;

    @SuppressWarnings("deprecation")
    Board(Player player, BoardAdapter adapter, BoardLimits limits) {
        this.limits = limits;

        var manager = LibraryPlugin.getInstance().getPlugin().getServer().getScoreboardManager();
        this.scoreboard = player.getScoreboard().equals(manager.getMainScoreboard())
                ? manager.getNewScoreboard()
                : player.getScoreboard();

        this.lastTitle = clipTitle(adapter.getTitle(player));
        this.objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, DUMMY_CRITERIA);
        objective.setDisplayName(lastTitle);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Scoreboard scoreboard() {
        return scoreboard;
    }

    String allocateKey(String text) {
        var suffix = text.length() > limits.lineSplitUnit()
                ? CC.getLastColors(text.substring(0, limits.lineSplitUnit()))
                : "";
        for (var base : ENTRY_KEYS) {
            var key = base + suffix;
            if (usedKeys.add(key)) return key;
        }
        throw new IllegalStateException("No free board entry keys (max " + ENTRY_KEYS.length + ")");
    }

    void releaseKey(String key) {
        usedKeys.remove(key);
    }

    String clipTitle(String raw) {
        var translated = CC.translate(raw != null ? raw : "");
        return translated.length() <= limits.titleMax()
                ? translated
                : translated.substring(0, limits.titleMax());
    }

    void clearEntries() {
        synchronized (entries) {
            for (var entry : entries) entry.remove();
            entries.clear();
        }
        usedKeys.clear();
    }

    Objective objective()        { return objective; }
    List<BoardEntry> entries()   { return entries; }
    BoardLimits limits()         { return limits; }
    String lastTitle()           { return lastTitle; }
    void lastTitle(String title) { lastTitle = title; }
}
