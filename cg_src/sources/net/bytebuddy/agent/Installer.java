package net.bytebuddy.agent;

import java.lang.instrument.Instrumentation;
/* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/Installer.class */
public class Installer {
    private static volatile Instrumentation instrumentation;

    private Installer() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    public static Instrumentation getInstrumentation() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getInstrumentation"));
        }
        Instrumentation instrumentation2 = instrumentation;
        if (instrumentation2 == null) {
            throw new IllegalStateException("The Byte Buddy agent is not loaded or this method is not called via the system class loader");
        }
        return instrumentation2;
    }

    public static void premain(String arguments, Instrumentation instrumentation2) {
        instrumentation = instrumentation2;
    }

    public static void agentmain(String arguments, Instrumentation instrumentation2) {
        instrumentation = instrumentation2;
    }
}
