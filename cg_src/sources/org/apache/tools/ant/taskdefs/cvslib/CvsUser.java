package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/cvslib/CvsUser.class */
public class CvsUser {
    private String userID;
    private String displayName;

    public void setDisplayname(String displayName) {
        this.displayName = displayName;
    }

    public void setUserid(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getDisplayname() {
        return this.displayName;
    }

    public void validate() throws BuildException {
        if (null == this.userID) {
            throw new BuildException("Username attribute must be set.");
        }
        if (null == this.displayName) {
            throw new BuildException("Displayname attribute must be set for userID %s", this.userID);
        }
    }
}
