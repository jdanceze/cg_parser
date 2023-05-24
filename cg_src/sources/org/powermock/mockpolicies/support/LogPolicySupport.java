package org.powermock.mockpolicies.support;

import java.lang.reflect.Method;
import org.powermock.reflect.Whitebox;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/mockpolicies/support/LogPolicySupport.class */
public class LogPolicySupport {
    public Method[] getLoggerMethods(String fullyQualifiedClassName, String methodName, String logFramework) {
        try {
            return Whitebox.getMethods(getType(fullyQualifiedClassName, logFramework), methodName);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public Class<?> getType(String name, String logFramework) throws Exception {
        try {
            Class<?> loggerType = Class.forName(name);
            return loggerType;
        } catch (ClassNotFoundException e) {
            String message = String.format("Cannot find %s in the classpath which the %s policy requires.", logFramework, getClass().getSimpleName());
            throw new RuntimeException(message, e);
        }
    }
}
