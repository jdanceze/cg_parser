package org.apache.tools.ant.loader;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/loader/AntClassLoader5.class */
public class AntClassLoader5 extends AntClassLoader {
    static {
        registerAsParallelCapable();
    }

    public AntClassLoader5(ClassLoader parent, Project project, Path classpath, boolean parentFirst) {
        super(parent, project, classpath, parentFirst);
    }
}
