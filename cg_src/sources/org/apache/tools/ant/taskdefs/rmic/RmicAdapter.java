package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Rmic;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileNameMapper;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/RmicAdapter.class */
public interface RmicAdapter {
    void setRmic(Rmic rmic);

    boolean execute() throws BuildException;

    FileNameMapper getMapper();

    Path getClasspath();
}
