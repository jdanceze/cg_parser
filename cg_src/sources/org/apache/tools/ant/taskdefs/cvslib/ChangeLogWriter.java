package org.apache.tools.ant.taskdefs.cvslib;

import android.text.format.Time;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TimeZone;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.DOMUtils;
import org.apache.tools.ant.util.DateUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/ChangeLogWriter.class */
public class ChangeLogWriter {
    private final SimpleDateFormat outputDate = new SimpleDateFormat(DateUtils.ISO8601_DATE_PATTERN);
    private SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm");
    private static final DOMElementWriter DOM_WRITER = new DOMElementWriter();

    public ChangeLogWriter() {
        TimeZone utc = TimeZone.getTimeZone(Time.TIMEZONE_UTC);
        this.outputDate.setTimeZone(utc);
        this.outputTime.setTimeZone(utc);
    }

    public void printChangeLog(PrintWriter output, CVSEntry[] entries) {
        try {
            output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            Document doc = DOMUtils.newDocument();
            Element root = doc.createElement("changelog");
            DOM_WRITER.openElement(root, output, 0, "\t");
            output.println();
            for (CVSEntry entry : entries) {
                printEntry(doc, output, entry);
            }
            DOM_WRITER.closeElement(root, output, 0, "\t", true);
            output.flush();
            output.close();
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    private void printEntry(Document doc, PrintWriter output, CVSEntry entry) throws IOException {
        Element ent = doc.createElement("entry");
        DOMUtils.appendTextElement(ent, "date", this.outputDate.format(entry.getDate()));
        DOMUtils.appendTextElement(ent, "time", this.outputTime.format(entry.getDate()));
        DOMUtils.appendCDATAElement(ent, "author", entry.getAuthor());
        Iterator<RCSFile> it = entry.getFiles().iterator();
        while (it.hasNext()) {
            RCSFile file = it.next();
            Element f = DOMUtils.createChildElement(ent, "file");
            DOMUtils.appendCDATAElement(f, "name", file.getName());
            DOMUtils.appendTextElement(f, "revision", file.getRevision());
            String previousRevision = file.getPreviousRevision();
            if (previousRevision != null) {
                DOMUtils.appendTextElement(f, "prevrevision", previousRevision);
            }
        }
        DOMUtils.appendCDATAElement(ent, "msg", entry.getComment());
        DOM_WRITER.write(ent, output, 1, "\t");
    }
}
