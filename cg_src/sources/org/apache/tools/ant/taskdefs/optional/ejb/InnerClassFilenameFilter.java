package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.FilenameFilter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/ejb/InnerClassFilenameFilter.class */
public class InnerClassFilenameFilter implements FilenameFilter {
    private String baseClassName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InnerClassFilenameFilter(String baseclass) {
        int extidx = baseclass.lastIndexOf(".class");
        this.baseClassName = baseclass.substring(0, extidx == -1 ? baseclass.length() - 1 : extidx);
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File dir, String filename) {
        return filename.lastIndexOf(46) == filename.lastIndexOf(".class") && filename.indexOf(new StringBuilder().append(this.baseClassName).append("$").toString()) == 0;
    }
}
