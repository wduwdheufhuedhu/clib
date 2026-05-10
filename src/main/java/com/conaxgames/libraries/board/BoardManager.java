package com.conaxgames.libraries.board;

import com.conaxgames.libraries.LibraryPlugin;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BoardManager implements Runnable {

    public static final String SKIP_BOARD_METADATA = "cElement";

    private final Map<UUID, Board> boards = new ConcurrentHashMap<>();
    private final BoardAdapter adapter;

    public BoardManager(BoardAdapter adapter) {
        this.adapter = adapter;
    }

    public BoardAdapter adapter() {
        return adapter;
    }

    @Override
    public void run() {
        adapter.preLoop();
        var server = LibraryPlugin.getInstance().getPlugin().getServer();
        var logger = LibraryPlugin.getInstance().getPlugin().getLogger();

        boards.entrySet().removeIf(entry -> {
            var player = server.getPlayer(entry.getKey());
            if (player == null || !player.isOnline()) return true;
            try {
                updateBoard(player, entry.getValue());
            } catch (Exception ex) {
                logger.severe("Scoreboard error for " + player.getName() + ": " + ex.getMessage());
            }
            return false;
        });
    }

    @SuppressWarnings("deprecation")
    private void updateBoard(Player player, Board board) {
        var lines = adapter.getLines(player, board);
        if (lines == null) lines = List.of();

        var newTitle = board.clipTitle(adapter.getTitle(player));
        if (!Objects.equals(newTitle, board.lastTitle())) {
            board.lastTitle(newTitle);
            board.objective().setDisplayName(newTitle);
        }

        var entries = board.entries();
        while (entries.size() > lines.size()) {
            entries.removeLast().remove();
        }

        int i = 0;
        for (var line : lines.reversed()) {
            BoardEntry entry;
            if (i < entries.size()) {
                entry = entries.get(i);
                entry.text(line);
            } else {
                entry = new BoardEntry(board, line);
                entries.add(entry);
            }
            entry.send(i + 1);
            i++;
        }

        var sb = board.scoreboard();
        if (!player.getScoreboard().equals(sb)) {
            player.setScoreboard(sb);
            adapter.onScoreboardCreate(player, sb);
        }
    }

    public void createBoard(Player player) {
        if (player.hasMetadata(SKIP_BOARD_METADATA) || boards.containsKey(player.getUniqueId())) return;
        boards.put(player.getUniqueId(), new Board(player, adapter));
    }

    public void removeBoard(Player player) {
        if (player.hasMetadata(SKIP_BOARD_METADATA)) return;
        boards.remove(player.getUniqueId());
    }
}
