package org.godigit.policyvault.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class DateUtil {

    private static final ZoneId UTC = ZoneOffset.UTC;
    private static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ISO_INSTANT;
    private static final DateTimeFormatter ISO_OFFSET = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private DateUtil() {}

    public static Instant nowInstant() {
        return Instant.now();
    }

    public static OffsetDateTime nowUtc() {
        return OffsetDateTime.now(UTC);
    }

    public static String formatInstant(Instant instant) {
        return ISO_INSTANT.format(instant);
    }

    public static String formatOffset(OffsetDateTime odt) {
        return ISO_OFFSET.format(odt);
    }

    public static Instant parseInstant(String iso) {
        return Instant.parse(iso);
    }

    public static OffsetDateTime startOfDayUtc(LocalDate date) {
        Objects.requireNonNull(date, "date");
        return date.atStartOfDay(UTC).toOffsetDateTime();
    }

    public static OffsetDateTime endOfDayUtc(LocalDate date) {
        Objects.requireNonNull(date, "date");
        return date.plusDays(1).atStartOfDay(UTC).minusNanos(1).toOffsetDateTime();
    }
}