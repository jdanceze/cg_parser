package org.apache.tools.ant.taskdefs.cvslib;

import android.text.format.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.stream.Stream;
import org.apache.tools.ant.taskdefs.AbstractCvsTask;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/ChangeLogParser.class */
class ChangeLogParser {
    private static final int GET_FILE = 1;
    private static final int GET_DATE = 2;
    private static final int GET_COMMENT = 3;
    private static final int GET_REVISION = 4;
    private static final int GET_PREVIOUS_REV = 5;
    private final SimpleDateFormat inputDate;
    private final SimpleDateFormat cvs1129InputDate;
    private String file;
    private String date;
    private String author;
    private String comment;
    private String revision;
    private String previousRevision;
    private int status;
    private final Map<String, CVSEntry> entries;
    private final boolean remote;
    private final String[] moduleNames;
    private final int[] moduleNameLengths;

    public ChangeLogParser() {
        this(false, "", Collections.emptyList());
    }

    public ChangeLogParser(boolean remote, String packageName, List<AbstractCvsTask.Module> modules) {
        this.inputDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        this.cvs1129InputDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
        this.status = 1;
        this.entries = new Hashtable();
        this.remote = remote;
        List<String> names = new ArrayList<>();
        if (packageName != null) {
            StringTokenizer tok = new StringTokenizer(packageName);
            while (tok.hasMoreTokens()) {
                names.add(tok.nextToken());
            }
        }
        Stream<R> map = modules.stream().map((v0) -> {
            return v0.getName();
        });
        Objects.requireNonNull(names);
        map.forEach((v1) -> {
            r1.add(v1);
        });
        this.moduleNames = (String[]) names.toArray(new String[names.size()]);
        this.moduleNameLengths = new int[this.moduleNames.length];
        for (int i = 0; i < this.moduleNames.length; i++) {
            this.moduleNameLengths[i] = this.moduleNames[i].length();
        }
        TimeZone utc = TimeZone.getTimeZone(Time.TIMEZONE_UTC);
        this.inputDate.setTimeZone(utc);
        this.cvs1129InputDate.setTimeZone(utc);
    }

    public CVSEntry[] getEntrySetAsArray() {
        return (CVSEntry[]) this.entries.values().toArray(new CVSEntry[this.entries.size()]);
    }

    public void stdout(String line) {
        switch (this.status) {
            case 1:
                reset();
                processFile(line);
                return;
            case 2:
                processDate(line);
                return;
            case 3:
                processComment(line);
                return;
            case 4:
                processRevision(line);
                return;
            case 5:
                processGetPreviousRevision(line);
                return;
            default:
                return;
        }
    }

    private void processComment(String line) {
        if ("=============================================================================".equals(line)) {
            int end = this.comment.length() - System.lineSeparator().length();
            this.comment = this.comment.substring(0, end);
            saveEntry();
            this.status = 1;
        } else if ("----------------------------".equals(line)) {
            int end2 = this.comment.length() - System.lineSeparator().length();
            this.comment = this.comment.substring(0, end2);
            this.status = 5;
        } else {
            this.comment += line + System.lineSeparator();
        }
    }

    private void processFile(String line) {
        if (!this.remote && line.startsWith("Working file:")) {
            this.file = line.substring(14);
            this.status = 4;
        } else if (this.remote && line.startsWith("RCS file:")) {
            int startOfFileName = 0;
            int i = 0;
            while (true) {
                if (i >= this.moduleNames.length) {
                    break;
                }
                int index = line.indexOf(this.moduleNames[i]);
                if (index < 0) {
                    i++;
                } else {
                    startOfFileName = index + this.moduleNameLengths[i] + 1;
                    break;
                }
            }
            int endOfFileName = line.indexOf(",v");
            if (endOfFileName == -1) {
                this.file = line.substring(startOfFileName);
            } else {
                this.file = line.substring(startOfFileName, endOfFileName);
            }
            this.status = 4;
        }
    }

    private void processRevision(String line) {
        if (line.startsWith("revision")) {
            this.revision = line.substring(9);
            this.status = 2;
        } else if (line.startsWith("======")) {
            this.status = 1;
        }
    }

    private void processDate(String line) {
        if (line.startsWith("date:")) {
            int endOfDateIndex = line.indexOf(59);
            this.date = line.substring("date: ".length(), endOfDateIndex);
            int startOfAuthorIndex = line.indexOf("author: ", endOfDateIndex + 1);
            int endOfAuthorIndex = line.indexOf(59, startOfAuthorIndex + 1);
            this.author = line.substring("author: ".length() + startOfAuthorIndex, endOfAuthorIndex);
            this.status = 3;
            this.comment = "";
        }
    }

    private void processGetPreviousRevision(String line) {
        if (!line.startsWith("revision ")) {
            throw new IllegalStateException("Unexpected line from CVS: " + line);
        }
        this.previousRevision = line.substring("revision ".length());
        saveEntry();
        this.revision = this.previousRevision;
        this.status = 2;
    }

    private void saveEntry() {
        this.entries.computeIfAbsent(this.date + this.author + this.comment, k -> {
            return new CVSEntry(parseDate(this.date), this.author, this.comment);
        }).addFile(this.file, this.revision, this.previousRevision);
    }

    private Date parseDate(String date) {
        try {
            return this.inputDate.parse(date);
        } catch (ParseException e) {
            try {
                return this.cvs1129InputDate.parse(date);
            } catch (ParseException e2) {
                throw new IllegalStateException("Invalid date format: " + date);
            }
        }
    }

    public void reset() {
        this.file = null;
        this.date = null;
        this.author = null;
        this.comment = null;
        this.revision = null;
        this.previousRevision = null;
    }
}
