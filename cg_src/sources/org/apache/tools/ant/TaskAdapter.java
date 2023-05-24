package org.apache.tools.ant;

import java.lang.reflect.Method;
import org.apache.tools.ant.dispatch.DispatchUtils;
import org.apache.tools.ant.dispatch.Dispatchable;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/TaskAdapter.class */
public class TaskAdapter extends Task implements TypeAdapter {
    private Object proxy;

    public TaskAdapter() {
    }

    public TaskAdapter(Object proxy) {
        this();
        setProxy(proxy);
    }

    public static void checkTaskClass(Class<?> taskClass, Project project) {
        if (!Dispatchable.class.isAssignableFrom(taskClass)) {
            try {
                Method executeM = taskClass.getMethod("execute", new Class[0]);
                if (!Void.TYPE.equals(executeM.getReturnType())) {
                    project.log("return type of execute() should be void but was \"" + executeM.getReturnType() + "\" in " + taskClass, 1);
                }
            } catch (LinkageError e) {
                String message = "Could not load " + taskClass + ": " + e;
                project.log(message, 0);
                throw new BuildException(message, e);
            } catch (NoSuchMethodException e2) {
                String message2 = "No public execute() in " + taskClass;
                project.log(message2, 0);
                throw new BuildException(message2);
            }
        }
    }

    @Override // org.apache.tools.ant.TypeAdapter
    public void checkProxyClass(Class<?> proxyClass) {
        checkTaskClass(proxyClass, getProject());
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        try {
            Method setLocationM = this.proxy.getClass().getMethod("setLocation", Location.class);
            if (setLocationM != null) {
                setLocationM.invoke(this.proxy, getLocation());
            }
        } catch (NoSuchMethodException e) {
        } catch (Exception ex) {
            log("Error setting location in " + this.proxy.getClass(), 0);
            throw new BuildException(ex);
        }
        try {
            Method setProjectM = this.proxy.getClass().getMethod("setProject", Project.class);
            if (setProjectM != null) {
                setProjectM.invoke(this.proxy, getProject());
            }
        } catch (NoSuchMethodException e2) {
        } catch (Exception ex2) {
            log("Error setting project in " + this.proxy.getClass(), 0);
            throw new BuildException(ex2);
        }
        try {
            DispatchUtils.execute(this.proxy);
        } catch (BuildException be) {
            throw be;
        } catch (Exception ex3) {
            log("Error in " + this.proxy.getClass(), 3);
            throw new BuildException(ex3);
        }
    }

    @Override // org.apache.tools.ant.TypeAdapter
    public void setProxy(Object o) {
        this.proxy = o;
    }

    @Override // org.apache.tools.ant.TypeAdapter
    public Object getProxy() {
        return this.proxy;
    }
}
