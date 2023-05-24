package org.apache.tools.ant.taskdefs.optional.jsp.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
import org.apache.tools.ant.taskdefs.optional.jsp.JspMangler;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/jsp/compilers/JspCompilerAdapter.class */
public interface JspCompilerAdapter {
    void setJspc(JspC jspC);

    boolean execute() throws BuildException;

    JspMangler createMangler();

    boolean implementsOwnDependencyChecking();
}
