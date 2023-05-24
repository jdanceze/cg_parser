package org.apache.tools.ant.util.depend;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import org.apache.tools.ant.types.Path;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/depend/DependencyAnalyzer.class */
public interface DependencyAnalyzer {
    void addSourcePath(Path path);

    void addClassPath(Path path);

    void addRootClass(String str);

    Enumeration<File> getFileDependencies();

    Enumeration<String> getClassDependencies();

    void reset();

    void config(String str, Object obj);

    void setClosure(boolean z);

    File getClassContainer(String str) throws IOException;

    File getSourceContainer(String str) throws IOException;
}
