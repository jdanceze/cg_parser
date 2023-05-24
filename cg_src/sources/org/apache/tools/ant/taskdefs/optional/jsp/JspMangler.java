package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/JspMangler.class */
public interface JspMangler {
    String mapJspToJavaName(File file);

    String mapPath(String str);
}
