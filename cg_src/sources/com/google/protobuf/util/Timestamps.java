package com.google.protobuf.util;

import android.text.format.Time;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompileTimeConstant;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.tools.ant.util.DateUtils;
/* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/Timestamps.class */
public final class Timestamps {
    static final int NANOS_PER_SECOND = 1000000000;
    static final int NANOS_PER_MILLISECOND = 1000000;
    static final int NANOS_PER_MICROSECOND = 1000;
    static final int MILLIS_PER_SECOND = 1000;
    static final int MICROS_PER_SECOND = 1000000;
    static final long TIMESTAMP_SECONDS_MIN = -62135596800L;
    public static final Timestamp MIN_VALUE = Timestamp.newBuilder().setSeconds(TIMESTAMP_SECONDS_MIN).setNanos(0).build();
    static final long TIMESTAMP_SECONDS_MAX = 253402300799L;
    public static final Timestamp MAX_VALUE = Timestamp.newBuilder().setSeconds(TIMESTAMP_SECONDS_MAX).setNanos(999999999).build();
    public static final Timestamp EPOCH = Timestamp.newBuilder().setSeconds(0).setNanos(0).build();
    private static final ThreadLocal<SimpleDateFormat> timestampFormat = new ThreadLocal<SimpleDateFormat>() { // from class: com.google.protobuf.util.Timestamps.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public SimpleDateFormat initialValue() {
            return Timestamps.createTimestampFormat();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static SimpleDateFormat createTimestampFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.ISO8601_DATETIME_PATTERN, Locale.ENGLISH);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone(Time.TIMEZONE_UTC));
        calendar.setGregorianChange(new Date(Long.MIN_VALUE));
        sdf.setCalendar(calendar);
        return sdf;
    }

    private Timestamps() {
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/Timestamps$TimestampComparator.class */
    private enum TimestampComparator implements Comparator<Timestamp>, Serializable {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(Timestamp t1, Timestamp t2) {
            Timestamps.checkValid(t1);
            Timestamps.checkValid(t2);
            int secDiff = Long.compare(t1.getSeconds(), t2.getSeconds());
            return secDiff != 0 ? secDiff : Integer.compare(t1.getNanos(), t2.getNanos());
        }
    }

    public static Comparator<Timestamp> comparator() {
        return TimestampComparator.INSTANCE;
    }

    public static int compare(Timestamp x, Timestamp y) {
        return TimestampComparator.INSTANCE.compare(x, y);
    }

    public static boolean isValid(Timestamp timestamp) {
        return isValid(timestamp.getSeconds(), timestamp.getNanos());
    }

    public static boolean isValid(long seconds, int nanos) {
        if (seconds < TIMESTAMP_SECONDS_MIN || seconds > TIMESTAMP_SECONDS_MAX || nanos < 0 || nanos >= NANOS_PER_SECOND) {
            return false;
        }
        return true;
    }

    @CanIgnoreReturnValue
    public static Timestamp checkValid(Timestamp timestamp) {
        long seconds = timestamp.getSeconds();
        int nanos = timestamp.getNanos();
        if (!isValid(seconds, nanos)) {
            throw new IllegalArgumentException(String.format("Timestamp is not valid. See proto definition for valid values. Seconds (%s) must be in range [-62,135,596,800, +253,402,300,799]. Nanos (%s) must be in range [0, +999,999,999].", Long.valueOf(seconds), Integer.valueOf(nanos)));
        }
        return timestamp;
    }

    public static Timestamp checkValid(Timestamp.Builder timestampBuilder) {
        return checkValid(timestampBuilder.build());
    }

    public static String toString(Timestamp timestamp) {
        checkValid(timestamp);
        long seconds = timestamp.getSeconds();
        int nanos = timestamp.getNanos();
        StringBuilder result = new StringBuilder();
        Date date = new Date(seconds * 1000);
        result.append(timestampFormat.get().format(date));
        if (nanos != 0) {
            result.append(".");
            result.append(formatNanos(nanos));
        }
        result.append("Z");
        return result.toString();
    }

    public static Timestamp parse(String value) throws ParseException {
        int dayOffset = value.indexOf(84);
        if (dayOffset == -1) {
            throw new ParseException("Failed to parse timestamp: invalid timestamp \"" + value + "\"", 0);
        }
        int timezoneOffsetPosition = value.indexOf(90, dayOffset);
        if (timezoneOffsetPosition == -1) {
            timezoneOffsetPosition = value.indexOf(43, dayOffset);
        }
        if (timezoneOffsetPosition == -1) {
            timezoneOffsetPosition = value.indexOf(45, dayOffset);
        }
        if (timezoneOffsetPosition == -1) {
            throw new ParseException("Failed to parse timestamp: missing valid timezone offset.", 0);
        }
        String timeValue = value.substring(0, timezoneOffsetPosition);
        String secondValue = timeValue;
        String nanoValue = "";
        int pointPosition = timeValue.indexOf(46);
        if (pointPosition != -1) {
            secondValue = timeValue.substring(0, pointPosition);
            nanoValue = timeValue.substring(pointPosition + 1);
        }
        Date date = timestampFormat.get().parse(secondValue);
        long seconds = date.getTime() / 1000;
        int nanos = nanoValue.isEmpty() ? 0 : parseNanos(nanoValue);
        if (value.charAt(timezoneOffsetPosition) == 'Z') {
            if (value.length() != timezoneOffsetPosition + 1) {
                throw new ParseException("Failed to parse timestamp: invalid trailing data \"" + value.substring(timezoneOffsetPosition) + "\"", 0);
            }
        } else {
            String offsetValue = value.substring(timezoneOffsetPosition + 1);
            long offset = parseTimezoneOffset(offsetValue);
            if (value.charAt(timezoneOffsetPosition) == '+') {
                seconds -= offset;
            } else {
                seconds += offset;
            }
        }
        try {
            return normalizedTimestamp(seconds, nanos);
        } catch (IllegalArgumentException e) {
            ParseException ex = new ParseException("Failed to parse timestamp " + value + " Timestamp is out of range.", 0);
            ex.initCause(e);
            throw ex;
        }
    }

    public static Timestamp parseUnchecked(@CompileTimeConstant String value) {
        try {
            return parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Timestamp fromSeconds(long seconds) {
        return normalizedTimestamp(seconds, 0);
    }

    public static long toSeconds(Timestamp timestamp) {
        return checkValid(timestamp).getSeconds();
    }

    public static Timestamp fromMillis(long milliseconds) {
        return normalizedTimestamp(milliseconds / 1000, (int) ((milliseconds % 1000) * 1000000));
    }

    public static Timestamp fromDate(Date date) {
        if (date instanceof java.sql.Timestamp) {
            java.sql.Timestamp sqlTimestamp = (java.sql.Timestamp) date;
            long integralSeconds = sqlTimestamp.getTime() / 1000;
            return Timestamp.newBuilder().setSeconds(integralSeconds).setNanos(sqlTimestamp.getNanos()).build();
        }
        return fromMillis(date.getTime());
    }

    public static long toMillis(Timestamp timestamp) {
        checkValid(timestamp);
        return LongMath.checkedAdd(LongMath.checkedMultiply(timestamp.getSeconds(), 1000L), timestamp.getNanos() / 1000000);
    }

    public static Timestamp fromMicros(long microseconds) {
        return normalizedTimestamp(microseconds / 1000000, (int) ((microseconds % 1000000) * 1000));
    }

    public static long toMicros(Timestamp timestamp) {
        checkValid(timestamp);
        return LongMath.checkedAdd(LongMath.checkedMultiply(timestamp.getSeconds(), 1000000L), timestamp.getNanos() / 1000);
    }

    public static Timestamp fromNanos(long nanoseconds) {
        return normalizedTimestamp(nanoseconds / 1000000000, (int) (nanoseconds % 1000000000));
    }

    public static long toNanos(Timestamp timestamp) {
        checkValid(timestamp);
        return LongMath.checkedAdd(LongMath.checkedMultiply(timestamp.getSeconds(), 1000000000L), timestamp.getNanos());
    }

    public static Duration between(Timestamp from, Timestamp to) {
        checkValid(from);
        checkValid(to);
        return Durations.normalizedDuration(LongMath.checkedSubtract(to.getSeconds(), from.getSeconds()), IntMath.checkedSubtract(to.getNanos(), from.getNanos()));
    }

    public static Timestamp add(Timestamp start, Duration length) {
        checkValid(start);
        Durations.checkValid(length);
        return normalizedTimestamp(LongMath.checkedAdd(start.getSeconds(), length.getSeconds()), IntMath.checkedAdd(start.getNanos(), length.getNanos()));
    }

    public static Timestamp subtract(Timestamp start, Duration length) {
        checkValid(start);
        Durations.checkValid(length);
        return normalizedTimestamp(LongMath.checkedSubtract(start.getSeconds(), length.getSeconds()), IntMath.checkedSubtract(start.getNanos(), length.getNanos()));
    }

    static Timestamp normalizedTimestamp(long seconds, int nanos) {
        if (nanos <= -1000000000 || nanos >= NANOS_PER_SECOND) {
            seconds = LongMath.checkedAdd(seconds, nanos / NANOS_PER_SECOND);
            nanos %= NANOS_PER_SECOND;
        }
        if (nanos < 0) {
            nanos += NANOS_PER_SECOND;
            seconds = LongMath.checkedSubtract(seconds, 1L);
        }
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(seconds).setNanos(nanos).build();
        return checkValid(timestamp);
    }

    private static long parseTimezoneOffset(String value) throws ParseException {
        int pos = value.indexOf(58);
        if (pos == -1) {
            throw new ParseException("Invalid offset value: " + value, 0);
        }
        String hours = value.substring(0, pos);
        String minutes = value.substring(pos + 1);
        return ((Long.parseLong(hours) * 60) + Long.parseLong(minutes)) * 60;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int parseNanos(String value) throws ParseException {
        int result = 0;
        for (int i = 0; i < 9; i++) {
            result *= 10;
            if (i < value.length()) {
                if (value.charAt(i) < '0' || value.charAt(i) > '9') {
                    throw new ParseException("Invalid nanoseconds.", 0);
                }
                result += value.charAt(i) - '0';
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatNanos(int nanos) {
        return nanos % 1000000 == 0 ? String.format(Locale.ENGLISH, "%1$03d", Integer.valueOf(nanos / 1000000)) : nanos % 1000 == 0 ? String.format(Locale.ENGLISH, "%1$06d", Integer.valueOf(nanos / 1000)) : String.format(Locale.ENGLISH, "%1$09d", Integer.valueOf(nanos));
    }
}
