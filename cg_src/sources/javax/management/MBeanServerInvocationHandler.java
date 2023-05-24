package javax.management;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/MBeanServerInvocationHandler.class */
public class MBeanServerInvocationHandler implements InvocationHandler {
    private final MBeanServerConnection connection;
    private final ObjectName objectName;
    static Class class$javax$management$NotificationEmitter;
    static Class class$javax$management$NotificationBroadcaster;
    static Class class$java$lang$Boolean;

    public MBeanServerInvocationHandler(MBeanServerConnection mBeanServerConnection, ObjectName objectName) {
        this.connection = mBeanServerConnection;
        this.objectName = objectName;
    }

    public static Object newProxyInstance(MBeanServerConnection mBeanServerConnection, ObjectName objectName, Class cls, boolean z) {
        Class[] clsArr;
        Class cls2;
        MBeanServerInvocationHandler mBeanServerInvocationHandler = new MBeanServerInvocationHandler(mBeanServerConnection, objectName);
        if (z) {
            Class[] clsArr2 = new Class[2];
            clsArr2[0] = cls;
            if (class$javax$management$NotificationEmitter == null) {
                cls2 = class$("javax.management.NotificationEmitter");
                class$javax$management$NotificationEmitter = cls2;
            } else {
                cls2 = class$javax$management$NotificationEmitter;
            }
            clsArr2[1] = cls2;
            clsArr = clsArr2;
        } else {
            clsArr = new Class[]{cls};
        }
        return Proxy.newProxyInstance(cls.getClassLoader(), clsArr, mBeanServerInvocationHandler);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00da, code lost:
        if (r0.equals(r1) != false) goto L39;
     */
    @Override // java.lang.reflect.InvocationHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object invoke(java.lang.Object r7, java.lang.reflect.Method r8, java.lang.Object[] r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 377
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.MBeanServerInvocationHandler.invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }

    private Object invokeBroadcasterMethod(Object obj, Method method, Object[] objArr) throws Exception {
        String name = method.getName();
        int length = objArr == null ? 0 : objArr.length;
        if (name.equals("addNotificationListener")) {
            if (length != 3) {
                throw new IllegalArgumentException(new StringBuffer().append("Bad arg count to addNotificationListener: ").append(length).toString());
            }
            this.connection.addNotificationListener(this.objectName, (NotificationListener) objArr[0], (NotificationFilter) objArr[1], objArr[2]);
            return null;
        } else if (name.equals("removeNotificationListener")) {
            NotificationListener notificationListener = (NotificationListener) objArr[0];
            switch (length) {
                case 1:
                    this.connection.removeNotificationListener(this.objectName, notificationListener);
                    return null;
                case 3:
                    this.connection.removeNotificationListener(this.objectName, notificationListener, (NotificationFilter) objArr[1], objArr[2]);
                    return null;
                default:
                    throw new IllegalArgumentException(new StringBuffer().append("Bad arg count to removeNotificationListener: ").append(length).toString());
            }
        } else if (name.equals("getNotificationInfo")) {
            if (objArr != null) {
                throw new IllegalArgumentException("getNotificationInfo has args");
            }
            return this.connection.getMBeanInfo(this.objectName).getNotifications();
        } else {
            throw new IllegalArgumentException(new StringBuffer().append("Bad method name: ").append(name).toString());
        }
    }
}
