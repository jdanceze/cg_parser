package org.apache.tools.ant.taskdefs.cvslib;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/RCSFile.class */
class RCSFile {
    private String name;
    private String revision;
    private String previousRevision;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RCSFile(String name, String revision) {
        this(name, revision, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RCSFile(String name, String revision, String previousRevision) {
        this.name = name;
        this.revision = revision;
        if (!revision.equals(previousRevision)) {
            this.previousRevision = previousRevision;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getName() {
        return this.name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getRevision() {
        return this.revision;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getPreviousRevision() {
        return this.previousRevision;
    }
}
