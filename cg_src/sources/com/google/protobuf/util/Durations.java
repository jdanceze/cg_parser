package com.google.protobuf.util;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompileTimeConstant;
import com.google.protobuf.Duration;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Comparator;
import org.apache.commons.cli.HelpFormatter;
/* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/Durations.class */
public final class Durations {
    private static final long SECONDS_PER_MINUTE = 60;
    private static final long SECONDS_PER_HOUR = 3600;
    private static final long SECONDS_PER_DAY = 86400;
    static final long DURATION_SECONDS_MIN = -315576000000L;
    public static final Duration MIN_VALUE = Duration.newBuilder().setSeconds(DURATION_SECONDS_MIN).setNanos(-999999999).build();
    static final long DURATION_SECONDS_MAX = 315576000000L;
    public static final Duration MAX_VALUE = Duration.newBuilder().setSeconds(DURATION_SECONDS_MAX).setNanos(999999999).build();
    public static final Duration ZERO = Duration.newBuilder().setSeconds(0).setNanos(0).build();

    private Durations() {
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/Durations$DurationComparator.class */
    private enum DurationComparator implements Comparator<Duration>, Serializable {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(Duration d1, Duration d2) {
            Durations.checkValid(d1);
            Durations.checkValid(d2);
            int secDiff = Long.compare(d1.getSeconds(), d2.getSeconds());
            return secDiff != 0 ? secDiff : Integer.compare(d1.getNanos(), d2.getNanos());
        }
    }

    public static Comparator<Duration> comparator() {
        return DurationComparator.INSTANCE;
    }

    public static int compare(Duration x, Duration y) {
        return DurationComparator.INSTANCE.compare(x, y);
    }

    public static boolean isValid(Duration duration) {
        return isValid(duration.getSeconds(), duration.getNanos());
    }

    public static boolean isValid(long seconds, int nanos) {
        if (seconds < DURATION_SECONDS_MIN || seconds > DURATION_SECONDS_MAX || nanos < -999999999 || nanos >= 1000000000) {
            return false;
        }
        if (seconds < 0 || nanos < 0) {
            if (seconds > 0 || nanos > 0) {
                return false;
            }
            return true;
        }
        return true;
    }

    public static boolean isNegative(Duration duration) {
        checkValid(duration);
        return duration.getSeconds() == 0 ? duration.getNanos() < 0 : duration.getSeconds() < 0;
    }

    public static boolean isPositive(Duration duration) {
        checkValid(duration);
        return (isNegative(duration) || duration.equals(ZERO)) ? false : true;
    }

    @CanIgnoreReturnValue
    public static Duration checkNotNegative(Duration duration) {
        Preconditions.checkArgument(!isNegative(duration), "duration (%s) must not be negative", toString(duration));
        return duration;
    }

    @CanIgnoreReturnValue
    public static Duration checkPositive(Duration duration) {
        Preconditions.checkArgument(isPositive(duration), "duration (%s) must be positive", toString(duration));
        return duration;
    }

    @CanIgnoreReturnValue
    public static Duration checkValid(Duration duration) {
        long seconds = duration.getSeconds();
        int nanos = duration.getNanos();
        if (!isValid(seconds, nanos)) {
            throw new IllegalArgumentException(String.format("Duration is not valid. See proto definition for valid values. Seconds (%s) must be in range [-315,576,000,000, +315,576,000,000]. Nanos (%s) must be in range [-999,999,999, +999,999,999]. Nanos must have the same sign as seconds", Long.valueOf(seconds), Integer.valueOf(nanos)));
        }
        return duration;
    }

    public static Duration checkValid(Duration.Builder durationBuilder) {
        return checkValid(durationBuilder.build());
    }

    public static String toString(Duration duration) {
        checkValid(duration);
        long seconds = duration.getSeconds();
        int nanos = duration.getNanos();
        StringBuilder result = new StringBuilder();
        if (seconds < 0 || nanos < 0) {
            result.append(HelpFormatter.DEFAULT_OPT_PREFIX);
            seconds = -seconds;
            nanos = -nanos;
        }
        result.append(seconds);
        if (nanos != 0) {
            result.append(".");
            result.append(Timestamps.formatNanos(nanos));
        }
        result.append("s");
        return result.toString();
    }

    public static Duration parse(String value) throws ParseException {
        if (value.isEmpty() || value.charAt(value.length() - 1) != 's') {
            throw new ParseException("Invalid duration string: " + value, 0);
        }
        boolean negative = false;
        if (value.charAt(0) == '-') {
            negative = true;
            value = value.substring(1);
        }
        String secondValue = value.substring(0, value.length() - 1);
        String nanoValue = "";
        int pointPosition = secondValue.indexOf(46);
        if (pointPosition != -1) {
            nanoValue = secondValue.substring(pointPosition + 1);
            secondValue = secondValue.substring(0, pointPosition);
        }
        long seconds = Long.parseLong(secondValue);
        int nanos = nanoValue.isEmpty() ? 0 : Timestamps.parseNanos(nanoValue);
        if (seconds < 0) {
            throw new ParseException("Invalid duration string: " + value, 0);
        }
        if (negative) {
            seconds = -seconds;
            nanos = -nanos;
        }
        try {
            return normalizedDuration(seconds, nanos);
        } catch (IllegalArgumentException e) {
            ParseException ex = new ParseException("Duration value is out of range.", 0);
            ex.initCause(e);
            throw ex;
        }
    }

    public static Duration parseUnchecked(@CompileTimeConstant String value) {
        try {
            return parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Duration fromDays(long days) {
        return Duration.newBuilder().setSeconds(LongMath.checkedMultiply(days, SECONDS_PER_DAY)).setNanos(0).build();
    }

    public static Duration fromHours(long hours) {
        return Duration.newBuilder().setSeconds(LongMath.checkedMultiply(hours, SECONDS_PER_HOUR)).setNanos(0).build();
    }

    public static Duration fromMinutes(long minutes) {
        return Duration.newBuilder().setSeconds(LongMath.checkedMultiply(minutes, SECONDS_PER_MINUTE)).setNanos(0).build();
    }

    public static Duration fromSeconds(long seconds) {
        return normalizedDuration(seconds, 0);
    }

    public static Duration fromMillis(long milliseconds) {
        return normalizedDuration(milliseconds / 1000, (int) ((milliseconds % 1000) * 1000000));
    }

    public static Duration fromMicros(long microseconds) {
        return normalizedDuration(microseconds / 1000000, (int) ((microseconds % 1000000) * 1000));
    }

    public static Duration fromNanos(long nanoseconds) {
        return normalizedDuration(nanoseconds / 1000000000, (int) (nanoseconds % 1000000000));
    }

    public static long toDays(Duration duration) {
        return checkValid(duration).getSeconds() / SECONDS_PER_DAY;
    }

    public static long toHours(Duration duration) {
        return checkValid(duration).getSeconds() / SECONDS_PER_HOUR;
    }

    public static long toMinutes(Duration duration) {
        return checkValid(duration).getSeconds() / SECONDS_PER_MINUTE;
    }

    public static long toSeconds(Duration duration) {
        return checkValid(duration).getSeconds();
    }

    public static double toSecondsAsDouble(Duration duration) {
        checkValid(duration);
        return duration.getSeconds() + (duration.getNanos() / 1.0E9d);
    }

    public static long toMillis(Duration duration) {
        checkValid(duration);
        return LongMath.checkedAdd(LongMath.checkedMultiply(duration.getSeconds(), 1000L), duration.getNanos() / 1000000);
    }

    public static long toMicros(Duration duration) {
        checkValid(duration);
        return LongMath.checkedAdd(LongMath.checkedMultiply(duration.getSeconds(), 1000000L), duration.getNanos() / 1000);
    }

    public static long toNanos(Duration duration) {
        checkValid(duration);
        return LongMath.checkedAdd(LongMath.checkedMultiply(duration.getSeconds(), 1000000000L), duration.getNanos());
    }

    public static Duration add(Duration d1, Duration d2) {
        checkValid(d1);
        checkValid(d2);
        return normalizedDuration(LongMath.checkedAdd(d1.getSeconds(), d2.getSeconds()), IntMath.checkedAdd(d1.getNanos(), d2.getNanos()));
    }

    public static Duration subtract(Duration d1, Duration d2) {
        checkValid(d1);
        checkValid(d2);
        return normalizedDuration(LongMath.checkedSubtract(d1.getSeconds(), d2.getSeconds()), IntMath.checkedSubtract(d1.getNanos(), d2.getNanos()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Duration normalizedDuration(long seconds, int nanos) {
        if (nanos <= -1000000000 || nanos >= 1000000000) {
            seconds = LongMath.checkedAdd(seconds, nanos / 1000000000);
            nanos %= 1000000000;
        }
        if (seconds > 0 && nanos < 0) {
            nanos += 1000000000;
            seconds--;
        }
        if (seconds < 0 && nanos > 0) {
            nanos -= 1000000000;
            seconds++;
        }
        Duration duration = Duration.newBuilder().setSeconds(seconds).setNanos(nanos).build();
        return checkValid(duration);
    }
}
