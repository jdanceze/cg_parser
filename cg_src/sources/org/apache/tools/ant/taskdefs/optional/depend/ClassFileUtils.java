package org.apache.tools.ant.taskdefs.optional.depend;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/depend/ClassFileUtils.class */
public class ClassFileUtils {
    public static String convertSlashName(String name) {
        return name.replace('\\', '.').replace('/', '.');
    }

    public static String convertDotName(String dotName) {
        return dotName.replace('.', '/');
    }
}
