package com.conaxgames.libraries.board;

import com.conaxgames.libraries.util.CC;
import com.conaxgames.libraries.util.VersioningChecker;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Sidebar objective helpers intended to run on <strong>1.8.8 through current Paper</strong> without
 * version-specific implementations.
 * <p>
 * Creation uses {@link Scoreboard#registerNewObjective(String, String)} with criteria {@code "dummy"} and
 * {@link Objective#setDisplayName(String)}. Those APIs exist on every supported release. Modern Paper also
 * offers {@code Criteria}-based registration and Adventure {@link net.kyori.adventure.text.Component}
 * display names; we keep the legacy string path so behaviour stays consistent on old servers. Deprecation
 * warnings on newer API jars are expected and accepted here.
 * <p>
 * {@link #init()} applies shorter title and team line limits when the server is older than 1.13, matching
 * legacy client restrictions while allowing longer text on newer versions.
 */
public final class BoardHandler {

    private static final String DUMMY_CRITERIA = "dummy";

    private static volatile boolean initialized;
    private static int maxPrefixLength = 64;
    private static int maxSuffixLength = 64;
    private static int lineSplitUnit = 64;
    private static int maxObjectiveTitleLength = 1024;

    private BoardHandler() {
    }

    public static void init() {
        if (initialized) {
            return;
        }
        synchronized (BoardHandler.class) {
            if (initialized) {
                return;
            }
            if (VersioningChecker.getInstance().isServerVersionBefore("1.13")) {
                maxPrefixLength = 16;
                maxSuffixLength = 16;
                lineSplitUnit = 16;
                maxObjectiveTitleLength = 32;
            }
            initialized = true;
        }
    }

    public static int maxPrefixLength() {
        return maxPrefixLength;
    }

    public static int maxSuffixLength() {
        return maxSuffixLength;
    }

    public static int lineSplitUnit() {
        return lineSplitUnit;
    }

    public static int maxObjectiveTitleLength() {
        return maxObjectiveTitleLength;
    }

    static String clipToLength(String value, int maxChars) {
        return value.length() <= maxChars ? value : value.substring(0, maxChars);
    }

    public static Objective createSidebarObjective(Scoreboard scoreboard, String name, String rawTitle) {
        Objective objective = scoreboard.registerNewObjective(name, DUMMY_CRITERIA);
        objective.setDisplayName(sidebarTitle(rawTitle));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        return objective;
    }

    public static void applyObjectiveTitle(Objective objective, String rawTitle) {
        objective.setDisplayName(sidebarTitle(rawTitle));
    }

    private static String sidebarTitle(String rawTitle) {
        return clipToLength(CC.translate(rawTitle != null ? rawTitle : ""), maxObjectiveTitleLength);
    }

}
