package org.apache.tools.ant.dispatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/dispatch/DispatchUtils.class */
public class DispatchUtils {
    public static final void execute(Object task) throws BuildException {
        Dispatchable dispatchable = null;
        try {
            if (task instanceof Dispatchable) {
                dispatchable = (Dispatchable) task;
            } else if (task instanceof UnknownElement) {
                UnknownElement ue = (UnknownElement) task;
                Object realThing = ue.getRealThing();
                if ((realThing instanceof Dispatchable) && (realThing instanceof Task)) {
                    dispatchable = (Dispatchable) realThing;
                }
            }
            if (dispatchable == null) {
                Method executeM = task.getClass().getMethod("execute", new Class[0]);
                if (executeM == null) {
                    throw new BuildException("No public execute() in " + task.getClass());
                }
                executeM.invoke(task, new Object[0]);
                if (task instanceof UnknownElement) {
                    ((UnknownElement) task).setRealThing(null);
                }
            } else {
                try {
                    String name = dispatchable.getActionParameterName();
                    if (name == null || name.trim().isEmpty()) {
                        throw new BuildException("Action Parameter Name must not be empty for Dispatchable Task.");
                    }
                    String mName = "get" + name.trim().substring(0, 1).toUpperCase();
                    if (name.length() > 1) {
                        mName = mName + name.substring(1);
                    }
                    Method actionM = dispatchable.getClass().getMethod(mName, new Class[0]);
                    if (actionM != null) {
                        Object o = actionM.invoke(dispatchable, null);
                        if (o == null) {
                            throw new BuildException("Dispatchable Task attribute '" + name.trim() + "' not set or value is empty.");
                        }
                        String methodName = o.toString().trim();
                        if (methodName.isEmpty()) {
                            throw new BuildException("Dispatchable Task attribute '" + name.trim() + "' not set or value is empty.");
                        }
                        Method executeM2 = dispatchable.getClass().getMethod(methodName, new Class[0]);
                        if (executeM2 == null) {
                            throw new BuildException("No public " + methodName + "() in " + dispatchable.getClass());
                        }
                        executeM2.invoke(dispatchable, null);
                        if (task instanceof UnknownElement) {
                            ((UnknownElement) task).setRealThing(null);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    throw new BuildException("No public " + ((String) null) + "() in " + task.getClass());
                }
            }
        } catch (IllegalAccessException | NoSuchMethodException e2) {
            throw new BuildException(e2);
        } catch (InvocationTargetException ie) {
            Throwable t = ie.getTargetException();
            if (t instanceof BuildException) {
                throw ((BuildException) t);
            }
            throw new BuildException(t);
        }
    }
}
