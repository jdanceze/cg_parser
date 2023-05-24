package org.apache.tools.ant.taskdefs;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Tstamp.class */
public class Tstamp extends Task {
    private static final String ENV_SOURCE_DATE_EPOCH = "SOURCE_DATE_EPOCH";
    private List<CustomFormat> customFormats = new Vector();
    private String prefix = "";

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        if (!this.prefix.endsWith(".")) {
            this.prefix += ".";
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        try {
            Date d = getNow();
            String epoch = System.getenv(ENV_SOURCE_DATE_EPOCH);
            if (epoch != null) {
                try {
                    d = new Date(Integer.parseInt(epoch) * 1000);
                    log("Honouring environment variable SOURCE_DATE_EPOCH which has been set to " + epoch);
                } catch (NumberFormatException e) {
                    log("Ignoring invalid value '" + epoch + "' for " + ENV_SOURCE_DATE_EPOCH + " environment variable", 4);
                }
            }
            Date date = d;
            this.customFormats.forEach(cts -> {
                date.execute(getProject(), date, getLocation());
            });
            SimpleDateFormat dstamp = new SimpleDateFormat("yyyyMMdd");
            setProperty("DSTAMP", dstamp.format(d));
            SimpleDateFormat tstamp = new SimpleDateFormat("HHmm");
            setProperty("TSTAMP", tstamp.format(d));
            SimpleDateFormat today = new SimpleDateFormat("MMMM d yyyy", Locale.US);
            setProperty("TODAY", today.format(d));
        } catch (Exception e2) {
            throw new BuildException(e2);
        }
    }

    public CustomFormat createFormat() {
        CustomFormat cts = new CustomFormat();
        this.customFormats.add(cts);
        return cts;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProperty(String name, String value) {
        getProject().setNewProperty(this.prefix + name, value);
    }

    protected Date getNow() {
        Optional<Date> now = getNow(MagicNames.TSTAMP_NOW_ISO, s -> {
            return Date.from(Instant.parse(s));
        }, k, v -> {
            return "magic property " + k + " ignored as '" + v + "' is not in valid ISO pattern";
        });
        if (now.isPresent()) {
            return now.get();
        }
        return getNow(MagicNames.TSTAMP_NOW, s2 -> {
            return new Date(1000 * Long.parseLong(s2));
        }, k2, v2 -> {
            return "magic property " + k2 + " ignored as " + v2 + " is not a valid number";
        }).orElseGet(Date::new);
    }

    protected Optional<Date> getNow(String propertyName, Function<String, Date> map, BiFunction<String, String, String> log) {
        String property = getProject().getProperty(propertyName);
        if (property != null && !property.isEmpty()) {
            try {
                return Optional.ofNullable(map.apply(property));
            } catch (Exception e) {
                log(log.apply(propertyName, property));
            }
        }
        return Optional.empty();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Tstamp$CustomFormat.class */
    public class CustomFormat {
        private TimeZone timeZone;
        private String propertyName;
        private String pattern;
        private String language;
        private String country;
        private String variant;
        private int offset = 0;
        private int field = 5;

        public CustomFormat() {
        }

        public void setProperty(String propertyName) {
            this.propertyName = propertyName;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public void setLocale(String locale) {
            StringTokenizer st = new StringTokenizer(locale, " \t\n\r\f,");
            try {
                this.language = st.nextToken();
                if (st.hasMoreElements()) {
                    this.country = st.nextToken();
                    if (st.hasMoreElements()) {
                        this.variant = st.nextToken();
                        if (st.hasMoreElements()) {
                            throw new BuildException("bad locale format", Tstamp.this.getLocation());
                        }
                    }
                } else {
                    this.country = "";
                }
            } catch (NoSuchElementException e) {
                throw new BuildException("bad locale format", e, Tstamp.this.getLocation());
            }
        }

        public void setTimezone(String id) {
            this.timeZone = TimeZone.getTimeZone(id);
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        @Deprecated
        public void setUnit(String unit) {
            Tstamp.this.log("DEPRECATED - The setUnit(String) method has been deprecated. Use setUnit(Tstamp.Unit) instead.");
            Unit u = new Unit();
            u.setValue(unit);
            this.field = u.getCalendarField();
        }

        public void setUnit(Unit unit) {
            this.field = unit.getCalendarField();
        }

        public void execute(Project project, Date date, Location location) {
            SimpleDateFormat sdf;
            if (this.propertyName == null) {
                throw new BuildException("property attribute must be provided", location);
            }
            if (this.pattern == null) {
                throw new BuildException("pattern attribute must be provided", location);
            }
            if (this.language == null) {
                sdf = new SimpleDateFormat(this.pattern);
            } else if (this.variant == null) {
                sdf = new SimpleDateFormat(this.pattern, new Locale(this.language, this.country));
            } else {
                sdf = new SimpleDateFormat(this.pattern, new Locale(this.language, this.country, this.variant));
            }
            if (this.offset != 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(this.field, this.offset);
                date = calendar.getTime();
            }
            if (this.timeZone != null) {
                sdf.setTimeZone(this.timeZone);
            }
            Tstamp.this.setProperty(this.propertyName, sdf.format(date));
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Tstamp$Unit.class */
    public static class Unit extends EnumeratedAttribute {
        private static final String MILLISECOND = "millisecond";
        private static final String SECOND = "second";
        private static final String MINUTE = "minute";
        private static final String HOUR = "hour";
        private static final String DAY = "day";
        private static final String WEEK = "week";
        private static final String YEAR = "year";
        private Map<String, Integer> calendarFields = new HashMap();
        private static final String MONTH = "month";
        private static final String[] UNITS = {"millisecond", "second", "minute", "hour", "day", "week", MONTH, "year"};

        public Unit() {
            this.calendarFields.put("millisecond", 14);
            this.calendarFields.put("second", 13);
            this.calendarFields.put("minute", 12);
            this.calendarFields.put("hour", 11);
            this.calendarFields.put("day", 5);
            this.calendarFields.put("week", 3);
            this.calendarFields.put(MONTH, 2);
            this.calendarFields.put("year", 1);
        }

        public int getCalendarField() {
            String key = getValue().toLowerCase(Locale.ENGLISH);
            return this.calendarFields.get(key).intValue();
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return UNITS;
        }
    }
}
