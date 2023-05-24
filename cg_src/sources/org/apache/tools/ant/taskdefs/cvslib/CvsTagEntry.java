package org.apache.tools.ant.taskdefs.cvslib;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/CvsTagEntry.class */
public class CvsTagEntry {
    private String filename;
    private String prevRevision;
    private String revision;

    public CvsTagEntry(String filename) {
        this(filename, null, null);
    }

    public CvsTagEntry(String filename, String revision) {
        this(filename, revision, null);
    }

    public CvsTagEntry(String filename, String revision, String prevRevision) {
        this.filename = filename;
        this.revision = revision;
        this.prevRevision = prevRevision;
    }

    public String getFile() {
        return this.filename;
    }

    public String getRevision() {
        return this.revision;
    }

    public String getPreviousRevision() {
        return this.prevRevision;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.filename);
        if (this.revision == null) {
            buffer.append(" was removed");
            if (this.prevRevision != null) {
                buffer.append("; previous revision was ").append(this.prevRevision);
            }
        } else if (this.prevRevision == null) {
            buffer.append(" is new; current revision is ").append(this.revision);
        } else {
            buffer.append(" has changed from ").append(this.prevRevision).append(" to ").append(this.revision);
        }
        return buffer.toString();
    }
}
