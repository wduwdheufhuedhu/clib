package com.conaxgames.libraries.board;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.message.CC;
import com.conaxgames.libraries.util.VersioningChecker;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Board {

    private static final boolean LEGACY = VersioningChecker.getInstance().isServerVersionBefore("1.13");

    private static final String[] ENTRY_KEYS;

    static {
        var codes = "0123456789abcdefklmor";
        ENTRY_KEYS = new String[codes.length()];
        for (int i = 0; i < codes.length(); i++) {
            ENTRY_KEYS[i] = "\u00a7" + codes.charAt(i) + "\u00a7f";
        }
    }

    private final List<BoardEntry> entries = new ArrayList<>();
    private final Set<String> usedKeys = new HashSet<>();
    private final Scoreboard scoreboard;
    private final Objective objective;
    private volatile String lastTitle;

    @SuppressWarnings("deprecation")
    Board(Player player, BoardAdapter adapter) {
        var manager = LibraryPlugin.getInstance().getPlugin().getServer().getScoreboardManager();
        this.scoreboard = player.getScoreboard().equals(manager.getMainScoreboard())
                ? manager.getNewScoreboard()
                : player.getScoreboard();

        this.lastTitle = clipTitle(adapter.getTitle(player));
        this.objective = scoreboard.registerNewObjective("sb", "dummy");
        objective.setDisplayName(lastTitle);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    static int segmentMax() {
        return LEGACY ? 16 : 64;
    }

    static int titleMax() {
        return LEGACY ? 32 : 1024;
    }

    public Scoreboard scoreboard() {
        return scoreboard;
    }

    String allocateKey(String text) {
        var suffix = text.length() > segmentMax()
                ? CC.getLastColors(text.substring(0, segmentMax()))
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
        return translated.length() <= titleMax()
                ? translated
                : translated.substring(0, titleMax());
    }

    Objective objective()        { return objective; }
    List<BoardEntry> entries()   { return entries; }
    String lastTitle()           { return lastTitle; }
    void lastTitle(String title) { lastTitle = title; }
}
