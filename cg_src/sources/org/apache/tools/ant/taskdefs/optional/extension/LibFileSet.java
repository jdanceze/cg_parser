package org.apache.tools.ant.taskdefs.optional.extension;

import org.apache.tools.ant.types.FileSet;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/LibFileSet.class */
public class LibFileSet extends FileSet {
    private boolean includeURL;
    private boolean includeImpl;
    private String urlBase;

    public void setIncludeUrl(boolean includeURL) {
        this.includeURL = includeURL;
    }

    public void setIncludeImpl(boolean includeImpl) {
        this.includeImpl = includeImpl;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isIncludeURL() {
        return this.includeURL;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isIncludeImpl() {
        return this.includeImpl;
    }

    String getUrlBase() {
        return this.urlBase;
    }
}
