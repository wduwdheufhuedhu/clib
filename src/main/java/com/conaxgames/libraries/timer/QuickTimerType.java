package com.conaxgames.libraries.timer;

/**
 * Immutable snapshot of a single quick-timer entry.
 *
 * @param expiryMillis epoch-millis at which this timer expires
 * @param announce     whether expiry should be announced to the player
 */
public record QuickTimerType(long expiryMillis, boolean announce) {

    public long getRemaining() {
        return expiryMillis - System.currentTimeMillis();
    }

    public boolean isExpired() {
        return getRemaining() <= 0L;
    }
}
