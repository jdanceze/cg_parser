package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Javac;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/compilers/CompilerAdapter.class */
public interface CompilerAdapter {
    void setJavac(Javac javac);

    boolean execute() throws BuildException;
}
