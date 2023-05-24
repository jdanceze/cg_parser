package org.apache.tools.ant;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import org.apache.tools.ant.dispatch.DispatchUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/Task.class */
public abstract class Task extends ProjectComponent {
    @Deprecated
    protected Target target;
    @Deprecated
    protected String taskName;
    @Deprecated
    protected String taskType;
    @Deprecated
    protected RuntimeConfigurable wrapper;
    private boolean invalid;
    private UnknownElement replacement;

    public void setOwningTarget(Target target) {
        this.target = target;
    }

    public Target getOwningTarget() {
        return this.target;
    }

    public void setTaskName(String name) {
        this.taskName = name;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskType(String type) {
        this.taskType = type;
    }

    public void init() throws BuildException {
    }

    public void execute() throws BuildException {
    }

    public RuntimeConfigurable getRuntimeConfigurableWrapper() {
        if (this.wrapper == null) {
            this.wrapper = new RuntimeConfigurable(this, getTaskName());
        }
        return this.wrapper;
    }

    public void setRuntimeConfigurableWrapper(RuntimeConfigurable wrapper) {
        this.wrapper = wrapper;
    }

    public void maybeConfigure() throws BuildException {
        if (this.invalid) {
            getReplacement();
        } else if (this.wrapper != null) {
            this.wrapper.maybeConfigure(getProject());
        }
    }

    public void reconfigure() {
        if (this.wrapper != null) {
            this.wrapper.reconfigure(getProject());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleOutput(String output) {
        log(output, 2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleFlush(String output) {
        handleOutput(output);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int handleInput(byte[] buffer, int offset, int length) throws IOException {
        return getProject().defaultInput(buffer, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleErrorOutput(String output) {
        log(output, 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleErrorFlush(String output) {
        handleErrorOutput(output);
    }

    @Override // org.apache.tools.ant.ProjectComponent
    public void log(String msg) {
        log(msg, 2);
    }

    @Override // org.apache.tools.ant.ProjectComponent
    public void log(String msg, int msgLevel) {
        if (getProject() == null) {
            super.log(msg, msgLevel);
        } else {
            getProject().log(this, msg, msgLevel);
        }
    }

    public void log(Throwable t, int msgLevel) {
        if (t != null) {
            log(t.getMessage(), t, msgLevel);
        }
    }

    public void log(String msg, Throwable t, int msgLevel) {
        if (getProject() == null) {
            super.log(msg, msgLevel);
        } else {
            getProject().log(this, msg, t, msgLevel);
        }
    }

    public final void perform() {
        if (this.invalid) {
            UnknownElement ue = getReplacement();
            Task task = ue.getTask();
            task.perform();
            return;
        }
        getProject().fireTaskStarted(this);
        try {
            try {
                try {
                    maybeConfigure();
                    DispatchUtils.execute(this);
                    getProject().fireTaskFinished(this, null);
                } catch (Error ex) {
                    throw ex;
                }
            } catch (BuildException ex2) {
                if (ex2.getLocation() == Location.UNKNOWN_LOCATION) {
                    ex2.setLocation(getLocation());
                }
                throw ex2;
            } catch (Exception ex3) {
                BuildException be = new BuildException(ex3);
                be.setLocation(getLocation());
                throw be;
            }
        } catch (Throwable th) {
            getProject().fireTaskFinished(this, null);
            throw th;
        }
    }

    final void markInvalid() {
        this.invalid = true;
    }

    protected final boolean isInvalid() {
        return this.invalid;
    }

    private UnknownElement getReplacement() {
        if (this.replacement == null) {
            this.replacement = new UnknownElement(this.taskType);
            this.replacement.setProject(getProject());
            this.replacement.setTaskType(this.taskType);
            this.replacement.setTaskName(this.taskName);
            this.replacement.setLocation(getLocation());
            this.replacement.setOwningTarget(this.target);
            this.replacement.setRuntimeConfigurableWrapper(this.wrapper);
            this.wrapper.setProxy(this.replacement);
            replaceChildren(this.wrapper, this.replacement);
            this.target.replaceChild(this, this.replacement);
            this.replacement.maybeConfigure();
        }
        return this.replacement;
    }

    private void replaceChildren(RuntimeConfigurable wrapper, UnknownElement parentElement) {
        Iterator it = Collections.list(wrapper.getChildren()).iterator();
        while (it.hasNext()) {
            RuntimeConfigurable childWrapper = (RuntimeConfigurable) it.next();
            UnknownElement childElement = new UnknownElement(childWrapper.getElementTag());
            parentElement.addChild(childElement);
            childElement.setProject(getProject());
            childElement.setRuntimeConfigurableWrapper(childWrapper);
            childWrapper.setProxy(childElement);
            replaceChildren(childWrapper, childElement);
        }
    }

    public String getTaskType() {
        return this.taskType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RuntimeConfigurable getWrapper() {
        return this.wrapper;
    }

    public final void bindToOwner(Task owner) {
        setProject(owner.getProject());
        setOwningTarget(owner.getOwningTarget());
        setTaskName(owner.getTaskName());
        setDescription(owner.getDescription());
        setLocation(owner.getLocation());
        setTaskType(owner.getTaskType());
    }
}
