package android.text.format;

import android.content.Context;
import java.util.Calendar;
import java.util.Date;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/text/format/DateFormat.class */
public class DateFormat {
    public static final char QUOTE = '\'';
    public static final char AM_PM = 'a';
    public static final char CAPITAL_AM_PM = 'A';
    public static final char DATE = 'd';
    public static final char DAY = 'E';
    public static final char HOUR = 'h';
    public static final char HOUR_OF_DAY = 'k';
    public static final char MINUTE = 'm';
    public static final char MONTH = 'M';
    public static final char SECONDS = 's';
    public static final char TIME_ZONE = 'z';
    public static final char YEAR = 'y';

    public DateFormat() {
        throw new RuntimeException("Stub!");
    }

    public static boolean is24HourFormat(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static final java.text.DateFormat getTimeFormat(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static final java.text.DateFormat getDateFormat(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static final java.text.DateFormat getLongDateFormat(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static final java.text.DateFormat getMediumDateFormat(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static final char[] getDateFormatOrder(Context context) {
        throw new RuntimeException("Stub!");
    }

    public static final CharSequence format(CharSequence inFormat, long inTimeInMillis) {
        throw new RuntimeException("Stub!");
    }

    public static final CharSequence format(CharSequence inFormat, Date inDate) {
        throw new RuntimeException("Stub!");
    }

    public static final CharSequence format(CharSequence inFormat, Calendar inDate) {
        throw new RuntimeException("Stub!");
    }
}
