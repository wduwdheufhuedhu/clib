package com.conaxgames.libraries.timer;

public record QuickTimerType(long expiryMillis, boolean announce) {

    public long getRemaining() {
        return expiryMillis - System.currentTimeMillis();
    }

    public boolean isExpired() {
        return getRemaining() <= 0L;
    }
}
