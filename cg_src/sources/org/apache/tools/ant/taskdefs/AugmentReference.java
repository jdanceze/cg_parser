package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TypeAdapter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AugmentReference.class */
public class AugmentReference extends Task implements TypeAdapter {
    private String id;

    @Override // org.apache.tools.ant.TypeAdapter
    public void checkProxyClass(Class<?> proxyClass) {
    }

    @Override // org.apache.tools.ant.TypeAdapter
    public synchronized Object getProxy() {
        if (getProject() == null) {
            throw new IllegalStateException(getTaskName() + "Project owner unset");
        }
        hijackId();
        if (getProject().hasReference(this.id)) {
            Object result = getProject().getReference(this.id);
            log("project reference " + this.id + "=" + String.valueOf(result), 4);
            return result;
        }
        throw new BuildException("Unknown reference \"" + this.id + "\"");
    }

    @Override // org.apache.tools.ant.TypeAdapter
    public void setProxy(Object o) {
        throw new UnsupportedOperationException();
    }

    private synchronized void hijackId() {
        if (this.id == null) {
            RuntimeConfigurable wrapper = getWrapper();
            this.id = wrapper.getId();
            if (this.id == null) {
                throw new BuildException(getTaskName() + " attribute 'id' unset");
            }
            wrapper.setAttribute("id", (String) null);
            wrapper.removeAttribute("id");
            wrapper.setElementTag("augmented reference \"" + this.id + "\"");
        }
    }

    @Override // org.apache.tools.ant.Task
    public void execute() {
        restoreWrapperId();
    }

    private synchronized void restoreWrapperId() {
        if (this.id != null) {
            log("restoring augment wrapper " + this.id, 4);
            RuntimeConfigurable wrapper = getWrapper();
            wrapper.setAttribute("id", this.id);
            wrapper.setElementTag(getTaskName());
            this.id = null;
        }
    }
}
