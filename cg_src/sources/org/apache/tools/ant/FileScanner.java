package org.apache.tools.ant;

import java.io.File;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/FileScanner.class */
public interface FileScanner {
    void addDefaultExcludes();

    File getBasedir();

    String[] getExcludedDirectories();

    String[] getExcludedFiles();

    String[] getIncludedDirectories();

    String[] getIncludedFiles();

    String[] getNotIncludedDirectories();

    String[] getNotIncludedFiles();

    void scan() throws IllegalStateException;

    void setBasedir(String str);

    void setBasedir(File file);

    void setExcludes(String[] strArr);

    void setIncludes(String[] strArr);

    void setCaseSensitive(boolean z);
}
