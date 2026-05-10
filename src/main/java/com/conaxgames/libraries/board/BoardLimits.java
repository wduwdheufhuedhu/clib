package com.conaxgames.libraries.board;

import com.conaxgames.libraries.util.VersioningChecker;

record BoardLimits(int teamSegmentMax, int lineSplitUnit, int titleMax) {

    private static final BoardLimits LEGACY = new BoardLimits(16, 16, 32);
    private static final BoardLimits MODERN = new BoardLimits(64, 64, 1024);

    static BoardLimits forCurrentServer() {
        return VersioningChecker.getInstance().isServerVersionBefore("1.13") ? LEGACY : MODERN;
    }

    String clip(String value) {
        return value.length() <= teamSegmentMax ? value : value.substring(0, teamSegmentMax);
    }
}
