package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/DateSelector.class */
public class DateSelector extends BaseExtendSelector {
    public static final String MILLIS_KEY = "millis";
    public static final String DATETIME_KEY = "datetime";
    public static final String CHECKDIRS_KEY = "checkdirs";
    public static final String GRANULARITY_KEY = "granularity";
    public static final String WHEN_KEY = "when";
    public static final String PATTERN_KEY = "pattern";
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    private String pattern;
    private long millis = -1;
    private String dateTime = null;
    private boolean includeDirs = false;
    private long granularity = FILE_UTILS.getFileTimestampGranularity();
    private TimeComparison when = TimeComparison.EQUAL;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/DateSelector$TimeComparisons.class */
    public static class TimeComparisons extends TimeComparison {
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        StringBuilder buf = new StringBuilder("{dateselector date: ");
        buf.append(this.dateTime);
        buf.append(" compare: ").append(this.when.getValue());
        buf.append(" granularity: ").append(this.granularity);
        if (this.pattern != null) {
            buf.append(" pattern: ").append(this.pattern);
        }
        buf.append("}");
        return buf.toString();
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        if (this.dateTime != null) {
            validate();
        }
        return this.millis;
    }

    public void setDatetime(String dateTime) {
        this.dateTime = dateTime;
        this.millis = -1L;
    }

    public void setCheckdirs(boolean includeDirs) {
        this.includeDirs = includeDirs;
    }

    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    public void setWhen(TimeComparisons tcmp) {
        setWhen((TimeComparison) tcmp);
    }

    public void setWhen(TimeComparison t) {
        this.when = t;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramname = parameter.getName();
                if (MILLIS_KEY.equalsIgnoreCase(paramname)) {
                    try {
                        setMillis(Long.parseLong(parameter.getValue()));
                    } catch (NumberFormatException e) {
                        setError("Invalid millisecond setting " + parameter.getValue());
                    }
                } else if (DATETIME_KEY.equalsIgnoreCase(paramname)) {
                    setDatetime(parameter.getValue());
                } else if (CHECKDIRS_KEY.equalsIgnoreCase(paramname)) {
                    setCheckdirs(Project.toBoolean(parameter.getValue()));
                } else if (GRANULARITY_KEY.equalsIgnoreCase(paramname)) {
                    try {
                        setGranularity(Integer.parseInt(parameter.getValue()));
                    } catch (NumberFormatException e2) {
                        setError("Invalid granularity setting " + parameter.getValue());
                    }
                } else if ("when".equalsIgnoreCase(paramname)) {
                    setWhen(new TimeComparison(parameter.getValue()));
                } else if (PATTERN_KEY.equalsIgnoreCase(paramname)) {
                    setPattern(parameter.getValue());
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        DateFormat simpleDateFormat;
        if (this.dateTime == null && this.millis < 0) {
            setError("You must provide a datetime or the number of milliseconds.");
        } else if (this.millis < 0 && this.dateTime != null) {
            String p = this.pattern == null ? "MM/dd/yyyy hh:mm a" : this.pattern;
            if (this.pattern == null) {
                simpleDateFormat = new SimpleDateFormat(p, Locale.US);
            } else {
                simpleDateFormat = new SimpleDateFormat(p);
            }
            DateFormat df = simpleDateFormat;
            try {
                setMillis(df.parse(this.dateTime).getTime());
                if (this.millis < 0) {
                    setError("Date of " + this.dateTime + " results in negative milliseconds value relative to epoch (January 1, 1970, 00:00:00 GMT).");
                }
            } catch (ParseException pe) {
                setError("Date of " + this.dateTime + " Cannot be parsed correctly. It should be in '" + p + "' format.", pe);
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        return (file.isDirectory() && !this.includeDirs) || this.when.evaluate(file.lastModified(), this.millis, this.granularity);
    }
}
