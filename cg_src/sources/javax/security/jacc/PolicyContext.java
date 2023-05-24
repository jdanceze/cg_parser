package javax.security.jacc;

import java.security.SecurityPermission;
import java.util.Hashtable;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/PolicyContext.class */
public final class PolicyContext {
    private static ThreadLocal thisContextID = new ThreadLocal();
    private static ThreadLocal thisHandlerData = new ThreadLocal();
    private static Hashtable handlerTable = new Hashtable();

    private PolicyContext() {
    }

    public static void setContextID(String contextID) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SecurityPermission("setPolicy"));
        }
        thisContextID.set(contextID);
    }

    public static String getContextID() {
        return (String) thisContextID.get();
    }

    public static void setHandlerData(Object data) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SecurityPermission("setPolicy"));
        }
        thisHandlerData.set(data);
    }

    public static void registerHandler(String key, PolicyContextHandler handler, boolean replace) throws PolicyContextException {
        if (handler == null || key == null) {
            throw new IllegalArgumentException("invalid (null) key or handler");
        }
        if (!handler.supports(key)) {
            throw new IllegalArgumentException("handler does not support key");
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SecurityPermission("setPolicy"));
        }
        if (handlerTable.containsKey(key) && !replace) {
            throw new IllegalArgumentException("handler exists");
        }
        handlerTable.put(key, handler);
    }

    public static Set getHandlerKeys() {
        return handlerTable.keySet();
    }

    public static Object getContext(String key) throws PolicyContextException {
        if (key == null) {
            throw new IllegalArgumentException("invalid key");
        }
        PolicyContextHandler handler = (PolicyContextHandler) handlerTable.get(key);
        if (handler == null || !handler.supports(key)) {
            throw new IllegalArgumentException("unknown handler key");
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(new SecurityPermission("setPolicy"));
        }
        return handler.getContext(key, thisHandlerData.get());
    }
}
