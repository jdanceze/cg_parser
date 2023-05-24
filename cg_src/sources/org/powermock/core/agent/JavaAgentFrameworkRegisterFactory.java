package org.powermock.core.agent;

import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/agent/JavaAgentFrameworkRegisterFactory.class */
public class JavaAgentFrameworkRegisterFactory {
    public static JavaAgentFrameworkRegister create() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return getInstanceForClassLoader(classLoader);
    }

    private static JavaAgentFrameworkRegister getInstanceForClassLoader(ClassLoader classLoader) {
        Class<JavaAgentFrameworkRegister> frameworkReporterClass = getJavaAgentFrameworkRegisterClass(classLoader);
        return (JavaAgentFrameworkRegister) Whitebox.newInstance(frameworkReporterClass);
    }

    private static Class<JavaAgentFrameworkRegister> getJavaAgentFrameworkRegisterClass(ClassLoader classLoader) {
        try {
            return classLoader.loadClass(getImplementerClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getImplementerClassName() {
        return "org.powermock.api.extension.agent.JavaAgentFrameworkRegisterImpl";
    }
}
