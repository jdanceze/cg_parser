package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.types.resources.FileResource;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/LoadFile.class */
public class LoadFile extends LoadResource {
    public final void setSrcFile(File srcFile) {
        addConfigured(new FileResource(srcFile));
    }
}
