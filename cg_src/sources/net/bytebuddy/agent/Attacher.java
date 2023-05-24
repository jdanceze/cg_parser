package net.bytebuddy.agent;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.InvocationTargetException;
/* loaded from: gencallgraphv3.jar:byte-buddy-agent-1.10.15.jar:net/bytebuddy/agent/Attacher.class */
public class Attacher {
    private static final Object STATIC_MEMBER = null;
    private static final String ATTACH_METHOD_NAME = "attach";
    private static final String LOAD_AGENT_METHOD_NAME = "loadAgent";
    private static final String LOAD_AGENT_PATH_METHOD_NAME = "loadAgentPath";
    private static final String DETACH_METHOD_NAME = "detach";

    private Attacher() {
        throw new UnsupportedOperationException("This class is a utility class and not supposed to be instantiated");
    }

    @SuppressFBWarnings(value = {"REC_CATCH_EXCEPTION"}, justification = "Exception should not be rethrown but trigger a fallback")
    public static void main(String[] args) {
        String argument;
        try {
            if (args.length < 5 || args[4].length() == 0) {
                argument = null;
            } else {
                StringBuilder stringBuilder = new StringBuilder(args[4].substring(1));
                for (int index = 5; index < args.length; index++) {
                    stringBuilder.append(' ').append(args[index]);
                }
                argument = stringBuilder.toString();
            }
            install(Class.forName(args[0]), args[1], args[2], Boolean.parseBoolean(args[3]), argument);
        } catch (Throwable th) {
            System.exit(1);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void install(Class<?> virtualMachineType, String processId, String agent, boolean isNative, String argument) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object virtualMachineInstance = virtualMachineType.getMethod(ATTACH_METHOD_NAME, String.class).invoke(STATIC_MEMBER, processId);
        try {
            virtualMachineType.getMethod(isNative ? LOAD_AGENT_PATH_METHOD_NAME : LOAD_AGENT_METHOD_NAME, String.class, String.class).invoke(virtualMachineInstance, agent, argument);
            virtualMachineType.getMethod(DETACH_METHOD_NAME, new Class[0]).invoke(virtualMachineInstance, new Object[0]);
        } catch (Throwable th) {
            virtualMachineType.getMethod(DETACH_METHOD_NAME, new Class[0]).invoke(virtualMachineInstance, new Object[0]);
            throw th;
        }
    }
}
