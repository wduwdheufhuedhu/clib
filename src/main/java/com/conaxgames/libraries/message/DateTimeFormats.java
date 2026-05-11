package com.conaxgames.libraries.message;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DateTimeFormats {

    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS =
            ThreadLocal.withInitial(() -> new DecimalFormat("0.#"));

    public static final ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING =
            ThreadLocal.withInitial(() -> new DecimalFormat("0.0"));

    private static final AtomicBoolean loaded = new AtomicBoolean(false);

    public static DateTimeFormatter DAY_MTH_HR_MIN_SECS;
    public static DateTimeFormatter DAY_MTH_YR_HR_MIN_AMPM;
    public static DateTimeFormatter DAY_MTH_HR_MIN_AMPM;
    public static DateTimeFormatter HR_MIN_AMPM;
    public static DateTimeFormatter HR_MIN_AMPM_TIMEZONE;
    public static DateTimeFormatter HR_MIN;
    public static DateTimeFormatter KOTH_FORMAT;

    private DateTimeFormats() {}

    public static void reload(TimeZone timeZone) {
        if (loaded.getAndSet(true)) {
            throw new IllegalStateException("DateTimeFormats has already been loaded");
        }

        var zone = timeZone.toZoneId();
        DAY_MTH_HR_MIN_SECS = formatter("dd/MM HH:mm:ss", zone);
        DAY_MTH_YR_HR_MIN_AMPM = formatter("dd/MM/yy hh:mma", zone);
        DAY_MTH_HR_MIN_AMPM = formatter("dd/MM hh:mma", zone);
        HR_MIN_AMPM = formatter("hh:mma", zone);
        HR_MIN_AMPM_TIMEZONE = formatter("hh:mma z", zone);
        HR_MIN = formatter("hh:mm", zone);
        KOTH_FORMAT = formatter("m:ss", zone);
    }

    private static DateTimeFormatter formatter(String pattern, ZoneId zone) {
        return DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH).withZone(zone);
    }
}
