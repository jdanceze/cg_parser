package org.apache.tools.ant;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/ExtensionPoint.class */
public class ExtensionPoint extends Target {
    private static final String NO_CHILDREN_ALLOWED = "you must not nest child elements into an extension-point";

    public ExtensionPoint() {
    }

    public ExtensionPoint(Target other) {
        super(other);
    }

    @Override // org.apache.tools.ant.Target, org.apache.tools.ant.TaskContainer
    public final void addTask(Task task) {
        throw new BuildException(NO_CHILDREN_ALLOWED);
    }

    @Override // org.apache.tools.ant.Target
    public final void addDataType(RuntimeConfigurable r) {
        throw new BuildException(NO_CHILDREN_ALLOWED);
    }
}
