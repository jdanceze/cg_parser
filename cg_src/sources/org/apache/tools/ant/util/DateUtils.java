package org.apache.tools.ant.util;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.resource.spi.work.WorkException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.tar.TarConstants;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/DateUtils.class */
public final class DateUtils {
    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60;
    private static final int ONE_HOUR = 60;
    private static final int TEN = 10;
    public static final String ISO8601_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ISO8601_DATE_PATTERN = "yyyy-MM-dd";
    public static final String ISO8601_TIME_PATTERN = "HH:mm:ss";
    @Deprecated
    public static final DateFormat DATE_HEADER_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ", Locale.US);
    private static final DateFormat DATE_HEADER_FORMAT_INT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ", Locale.US);
    private static final MessageFormat MINUTE_SECONDS = new MessageFormat("{0}{1}");
    private static final double[] LIMITS = {Const.default_value_double, 1.0d, 2.0d};
    private static final String[] MINUTES_PART = {"", "1 minute ", "{0,number,###############} minutes "};
    private static final String[] SECONDS_PART = {"0 seconds", "1 second", "{1,number} seconds"};
    private static final ChoiceFormat MINUTES_FORMAT = new ChoiceFormat(LIMITS, MINUTES_PART);
    private static final ChoiceFormat SECONDS_FORMAT = new ChoiceFormat(LIMITS, SECONDS_PART);
    public static final ThreadLocal<DateFormat> EN_US_DATE_FORMAT_MIN = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
    });
    public static final ThreadLocal<DateFormat> EN_US_DATE_FORMAT_SEC = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US);
    });
    private static final ThreadLocal<DateFormat> iso8601WithTimeZone;
    private static final Pattern iso8601normalizer;

    static {
        MINUTE_SECONDS.setFormat(0, MINUTES_FORMAT);
        MINUTE_SECONDS.setFormat(1, SECONDS_FORMAT);
        iso8601WithTimeZone = ThreadLocal.withInitial(() -> {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        });
        iso8601normalizer = Pattern.compile("^(\\d{4,}-\\d{2}-\\d{2})[Tt ](\\d{2}:\\d{2}(:\\d{2}(\\.\\d{3})?)?) ?(?:Z|([+-]\\d{2})(?::?(\\d{2}))?)?$");
    }

    private DateUtils() {
    }

    public static String format(long date, String pattern) {
        return format(new Date(date), pattern);
    }

    public static String format(Date date, String pattern) {
        DateFormat df = createDateFormat(pattern);
        return df.format(date);
    }

    public static String formatElapsedTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        return MINUTE_SECONDS.format(new Object[]{Long.valueOf(minutes), Long.valueOf(seconds % 60)});
    }

    private static DateFormat createDateFormat(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        sdf.setTimeZone(gmt);
        sdf.setLenient(true);
        return sdf;
    }

    public static int getPhaseOfMoon(Calendar cal) {
        int dayOfTheYear = cal.get(6);
        int yearInMetonicCycle = ((cal.get(1) - 1900) % 19) + 1;
        int epact = ((11 * yearInMetonicCycle) + 18) % 30;
        if ((epact == 25 && yearInMetonicCycle > 11) || epact == 24) {
            epact++;
        }
        return (((((dayOfTheYear + epact) * 6) + 11) % 177) / 22) & 7;
    }

    public static String getDateForHeader() {
        String str;
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        int offset = tz.getOffset(cal.get(0), cal.get(1), cal.get(2), cal.get(5), cal.get(7), cal.get(14));
        StringBuilder tzMarker = new StringBuilder(offset < 0 ? HelpFormatter.DEFAULT_OPT_PREFIX : "+");
        int offset2 = Math.abs(offset);
        int hours = offset2 / 3600000;
        int minutes = (offset2 / 60000) - (60 * hours);
        if (hours < 10) {
            tzMarker.append(WorkException.UNDEFINED);
        }
        tzMarker.append(hours);
        if (minutes < 10) {
            tzMarker.append(WorkException.UNDEFINED);
        }
        tzMarker.append(minutes);
        synchronized (DATE_HEADER_FORMAT_INT) {
            str = DATE_HEADER_FORMAT_INT.format(cal.getTime()) + tzMarker.toString();
        }
        return str;
    }

    public static Date parseDateFromHeader(String datestr) throws ParseException {
        Date parse;
        synchronized (DATE_HEADER_FORMAT_INT) {
            parse = DATE_HEADER_FORMAT_INT.parse(datestr);
        }
        return parse;
    }

    public static Date parseIso8601DateTime(String datestr) throws ParseException {
        return new SimpleDateFormat(ISO8601_DATETIME_PATTERN).parse(datestr);
    }

    public static Date parseIso8601Date(String datestr) throws ParseException {
        return new SimpleDateFormat(ISO8601_DATE_PATTERN).parse(datestr);
    }

    public static Date parseIso8601DateTimeOrDate(String datestr) throws ParseException {
        try {
            return parseIso8601DateTime(datestr);
        } catch (ParseException e) {
            return parseIso8601Date(datestr);
        }
    }

    public static Date parseLenientDateTime(String dateStr) throws ParseException {
        try {
            return new Date(Long.parseLong(dateStr));
        } catch (NumberFormatException e) {
            try {
                return EN_US_DATE_FORMAT_MIN.get().parse(dateStr);
            } catch (ParseException e2) {
                try {
                    return EN_US_DATE_FORMAT_SEC.get().parse(dateStr);
                } catch (ParseException e3) {
                    Matcher m = iso8601normalizer.matcher(dateStr);
                    if (!m.find()) {
                        throw new ParseException(dateStr, 0);
                    }
                    String normISO = m.group(1) + Instruction.argsep + (m.group(3) == null ? m.group(2) + ":00" : m.group(2)) + (m.group(4) == null ? ".000 " : Instruction.argsep) + (m.group(5) == null ? "+00" : m.group(5)) + (m.group(6) == null ? TarConstants.VERSION_POSIX : m.group(6));
                    return iso8601WithTimeZone.get().parse(normISO);
                }
            }
        }
    }
}
