package org.apache.tools.ant.taskdefs.condition;

import android.text.style.SuggestionSpan;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.Touch;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Resource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsLastModified.class */
public class IsLastModified extends ProjectComponent implements Condition {
    private Resource resource;
    private long millis = -1;
    private String dateTime = null;
    private Touch.DateFormatFactory dfFactory = Touch.DEFAULT_DF_FACTORY;
    private CompareMode mode = CompareMode.EQUALS;

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public void setDatetime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPattern(final String pattern) {
        this.dfFactory = new Touch.DateFormatFactory() { // from class: org.apache.tools.ant.taskdefs.condition.IsLastModified.1
            @Override // org.apache.tools.ant.taskdefs.Touch.DateFormatFactory
            public DateFormat getPrimaryFormat() {
                return new SimpleDateFormat(pattern);
            }

            @Override // org.apache.tools.ant.taskdefs.Touch.DateFormatFactory
            public DateFormat getFallbackFormat() {
                return null;
            }
        };
    }

    public void add(Resource r) {
        if (this.resource != null) {
            throw new BuildException("only one resource can be tested");
        }
        this.resource = r;
    }

    public void setMode(CompareMode mode) {
        this.mode = mode;
    }

    protected void validate() throws BuildException {
        if (this.millis >= 0 && this.dateTime != null) {
            throw new BuildException("Only one of dateTime and millis can be set");
        }
        if (this.millis < 0 && this.dateTime == null) {
            throw new BuildException("millis or dateTime is required");
        }
        if (this.resource == null) {
            throw new BuildException("resource is required");
        }
    }

    protected long getMillis() throws BuildException {
        ParseException pe;
        if (this.millis >= 0) {
            return this.millis;
        }
        if ("now".equalsIgnoreCase(this.dateTime)) {
            return System.currentTimeMillis();
        }
        try {
            return this.dfFactory.getPrimaryFormat().parse(this.dateTime).getTime();
        } catch (ParseException peOne) {
            DateFormat df = this.dfFactory.getFallbackFormat();
            if (df == null) {
                pe = peOne;
            } else {
                try {
                    return df.parse(this.dateTime).getTime();
                } catch (ParseException peTwo) {
                    pe = peTwo;
                    throw new BuildException(pe.getMessage(), pe, getLocation());
                }
            }
            throw new BuildException(pe.getMessage(), pe, getLocation());
        }
    }

    @Override // org.apache.tools.ant.taskdefs.condition.Condition
    public boolean eval() throws BuildException {
        validate();
        long expected = getMillis();
        long actual = this.resource.getLastModified();
        log("expected timestamp: " + expected + " (" + new Date(expected) + "), actual timestamp: " + actual + " (" + new Date(actual) + ")", 3);
        if ("equals".equals(this.mode.getValue())) {
            return expected == actual;
        } else if (SuggestionSpan.SUGGESTION_SPAN_PICKED_BEFORE.equals(this.mode.getValue())) {
            return expected > actual;
        } else if ("not-before".equals(this.mode.getValue())) {
            return expected <= actual;
        } else if (SuggestionSpan.SUGGESTION_SPAN_PICKED_AFTER.equals(this.mode.getValue())) {
            return expected < actual;
        } else if ("not-after".equals(this.mode.getValue())) {
            return expected >= actual;
        } else {
            throw new BuildException("Unknown mode " + this.mode.getValue());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/condition/IsLastModified$CompareMode.class */
    public static class CompareMode extends EnumeratedAttribute {
        private static final String BEFORE_TEXT = "before";
        private static final String AFTER_TEXT = "after";
        private static final String NOT_BEFORE_TEXT = "not-before";
        private static final String NOT_AFTER_TEXT = "not-after";
        private static final String EQUALS_TEXT = "equals";
        private static final CompareMode EQUALS = new CompareMode(EQUALS_TEXT);

        public CompareMode() {
            this(EQUALS_TEXT);
        }

        public CompareMode(String s) {
            setValue(s);
        }

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{EQUALS_TEXT, "before", "after", NOT_BEFORE_TEXT, NOT_AFTER_TEXT};
        }
    }
}
