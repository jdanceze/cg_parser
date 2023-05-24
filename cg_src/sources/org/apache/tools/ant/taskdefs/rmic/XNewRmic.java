package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.types.Commandline;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/rmic/XNewRmic.class */
public class XNewRmic extends ForkingSunRmic {
    public static final String COMPILER_NAME = "xnew";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.taskdefs.rmic.DefaultRmicAdapter
    public Commandline setupRmicCommand() {
        String[] options = {"-Xnew"};
        return super.setupRmicCommand(options);
    }
}
