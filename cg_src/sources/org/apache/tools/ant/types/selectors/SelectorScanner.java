package org.apache.tools.ant.types.selectors;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SelectorScanner.class */
public interface SelectorScanner {
    void setSelectors(FileSelector[] fileSelectorArr);

    String[] getDeselectedDirectories();

    String[] getDeselectedFiles();
}
