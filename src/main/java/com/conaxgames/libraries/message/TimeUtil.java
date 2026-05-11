package com.conaxgames.libraries.message;

import com.conaxgames.libraries.util.Duration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class TimeUtil {

    private static final long MINUTE_MS = TimeUnit.MINUTES.toMillis(1L);
    private static final long HOUR_MS = TimeUnit.HOURS.toMillis(1L);
    private static final Pattern TIME_SEGMENT = Pattern.compile("(\\d+)([smhdwMy])");

    private TimeUtil() {}

    public static Instant addDuration(long durationMillis) {
        return clampInstant(Instant.now().plusMillis(durationMillis));
    }

    public static Instant addDuration(Instant offset) {
        return clampInstant(Instant.now().plusMillis(offset.toEpochMilli()));
    }

    public static Instant fromMillis(long millis) {
        return Instant.ofEpochMilli(millis);
    }

    public static Instant getCurrentTimestamp() {
        return Instant.now();
    }

    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    public static long toMillis(String time) {
        if (time == null || time.length() < 2) return -1;

        var suffix = time.substring(time.length() - 1);
        var duration = Duration.getByName(suffix);
        if (duration == null) return -1;

        try {
            int value = Integer.parseInt(time.substring(0, time.length() - 1));
            return value * duration.getDuration();
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String millisToRoundedTime(long millis) {
        millis += 1L;
        long seconds = millis / 1_000L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;
        long weeks = days / 7L;
        long months = weeks / 4L;
        long years = months / 12L;

        if (years > 0) return pluralize(years, "year");
        if (months > 0) return pluralize(months, "month");
        if (weeks > 0) return pluralize(weeks, "week");
        if (days > 0) return pluralize(days, "day");
        if (hours > 0) return pluralize(hours, "hour");
        if (minutes > 0) return pluralize(minutes, "minute");
        return pluralize(seconds, "second");
    }

    public static long parseTime(String time) {
        long total = 0L;
        boolean found = false;
        var matcher = TIME_SEGMENT.matcher(time);

        while (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            long multiplier = switch (matcher.group(2)) {
                case "s" -> 1L;
                case "m" -> 60L;
                case "h" -> 3_600L;
                case "d" -> 86_400L;
                case "w" -> 604_800L;
                case "M" -> 2_592_000L;
                case "y" -> 31_536_000L;
                default -> 0L;
            };
            total += value * multiplier;
            found = true;
        }
        return found ? total * 1_000L : -1L;
    }

    public static String timeAsStringLongFormat(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        var sb = new StringBuilder();
        if (days > 0) sb.append(days).append(days == 1 ? " day " : " days ");
        if (hours > 0) sb.append(hours).append(hours == 1 ? " hour " : " hours ");
        if (minutes > 0) sb.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        if (seconds > 0) sb.append(seconds).append(seconds == 1 ? " second " : " seconds ");
        return sb.toString();
    }

    public static String timeAsString(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        var sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0) sb.append(seconds).append('s');
        return sb.isEmpty() ? "0s" : sb.toString().trim();
    }

    public static String timeAsStringCondensed(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

        var sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        return sb.toString();
    }

    public static String getTimerRemaining(long millis, boolean milliseconds) {
        return getTimerRemaining(millis, milliseconds, true);
    }

    public static String getTimerRemaining(long duration, boolean milliseconds, boolean trail) {
        if (milliseconds && duration < MINUTE_MS) {
            var fmt = (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get();
            return fmt.format(duration * 0.001) + 's';
        }
        long totalSeconds = duration / 1_000L;
        long hrs = totalSeconds / 3_600L;
        long mins = (totalSeconds % 3_600L) / 60L;
        long secs = totalSeconds % 60L;

        return duration >= HOUR_MS
                ? "%02d:%02d:%02d".formatted(hrs, mins, secs)
                : "%02d:%02d".formatted(mins, secs);
    }

    private static Instant clampInstant(Instant instant) {
        var max = Instant.parse("2037-12-31T23:59:59Z");
        return instant.isAfter(max) ? max : instant;
    }

    private static String pluralize(long value, String unit) {
        return value + " " + unit + (value == 1 ? "" : "s");
    }
}
