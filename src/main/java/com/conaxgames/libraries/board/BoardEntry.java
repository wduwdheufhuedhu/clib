package com.conaxgames.libraries.board;

import com.conaxgames.libraries.message.CC;
import org.bukkit.scoreboard.Team;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("deprecation")
final class BoardEntry {

    private static final AtomicInteger TEAM_COUNTER = new AtomicInteger();

    private final Board board;
    private final String key;
    private final Team team;
    private String text;
    private String[] splitCache;

    BoardEntry(Board board, String text) {
        this.board = board;
        this.text = text != null ? text : "";
        this.key = board.allocateKey(this.text);
        this.team = board.scoreboard().registerNewTeam("board_" + TEAM_COUNTER.getAndIncrement());
        team.addEntry(key);
    }

    void send(int position) {
        var split = split();
        int max = Board.segmentMax();
        var prefix = split[0].length() <= max ? split[0] : split[0].substring(0, max);
        var suffix = split[1].length() <= max ? split[1] : split[1].substring(0, max);

        if (!prefix.equals(team.getPrefix())) {
            team.setPrefix(prefix);
        }
        if (!suffix.equals(team.getSuffix())) {
            team.setSuffix(suffix);
        }

        var score = board.objective().getScore(key);
        if (score.getScore() != position) {
            score.setScore(position);
        }
    }

    void remove() {
        board.releaseKey(key);
        board.scoreboard().resetScores(key);
        team.removeEntry(key);
        team.unregister();
    }

    void text(String newText) {
        if (newText != null && !text.equals(newText)) {
            text = newText;
            splitCache = null;
        }
    }

    private String[] split() {
        if (splitCache != null) {
            return splitCache;
        }
        var translated = CC.translate(text);
        int unit = Board.segmentMax();

        if (translated.length() <= unit) {
            return splitCache = new String[]{translated, ""};
        }

        int splitAt = CC.safeSplitIndex(translated, unit);
        var prefix = translated.substring(0, splitAt);
        var suffix = CC.getLastColors(prefix) + translated.substring(splitAt);
        return splitCache = new String[]{prefix, suffix};
    }
}
