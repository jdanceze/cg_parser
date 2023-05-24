package org.apache.tools.ant.types;

import android.text.style.SuggestionSpan;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/TimeComparison.class */
public class TimeComparison extends EnumeratedAttribute {
    private static final String[] VALUES = {SuggestionSpan.SUGGESTION_SPAN_PICKED_BEFORE, SuggestionSpan.SUGGESTION_SPAN_PICKED_AFTER, "equal"};
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    public static final TimeComparison BEFORE = new TimeComparison(SuggestionSpan.SUGGESTION_SPAN_PICKED_BEFORE);
    public static final TimeComparison AFTER = new TimeComparison(SuggestionSpan.SUGGESTION_SPAN_PICKED_AFTER);
    public static final TimeComparison EQUAL = new TimeComparison("equal");

    public TimeComparison() {
    }

    public TimeComparison(String value) {
        setValue(value);
    }

    @Override // org.apache.tools.ant.types.EnumeratedAttribute
    public String[] getValues() {
        return VALUES;
    }

    public boolean evaluate(long t1, long t2) {
        return evaluate(t1, t2, FILE_UTILS.getFileTimestampGranularity());
    }

    public boolean evaluate(long t1, long t2, long g) {
        int cmp = getIndex();
        if (cmp == -1) {
            throw new BuildException("TimeComparison value not set.");
        }
        return cmp == 0 ? t1 - g < t2 : cmp == 1 ? t1 + g > t2 : Math.abs(t1 - t2) <= g;
    }

    public static int compare(long t1, long t2) {
        return compare(t1, t2, FILE_UTILS.getFileTimestampGranularity());
    }

    public static int compare(long t1, long t2, long g) {
        long diff = t1 - t2;
        long abs = Math.abs(diff);
        if (abs > Math.abs(g)) {
            return (int) (diff / abs);
        }
        return 0;
    }
}
