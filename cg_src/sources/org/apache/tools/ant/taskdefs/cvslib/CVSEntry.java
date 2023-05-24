package org.apache.tools.ant.taskdefs.cvslib;

import java.util.Date;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/CVSEntry.class */
public class CVSEntry {
    private Date date;
    private String author;
    private final String comment;
    private final Vector<RCSFile> files = new Vector<>();

    public CVSEntry(Date date, String author, String comment) {
        this.date = date;
        this.author = author;
        this.comment = comment;
    }

    public void addFile(String file, String revision) {
        this.files.add(new RCSFile(file, revision));
    }

    public void addFile(String file, String revision, String previousRevision) {
        this.files.add(new RCSFile(file, revision, previousRevision));
    }

    public Date getDate() {
        return this.date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getComment() {
        return this.comment;
    }

    public Vector<RCSFile> getFiles() {
        return this.files;
    }

    public String toString() {
        return getAuthor() + "\n" + getDate() + "\n" + getFiles() + "\n" + getComment();
    }
}
