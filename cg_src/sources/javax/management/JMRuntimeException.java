package javax.management;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/JMRuntimeException.class */
public class JMRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6573344628407841861L;
    static Class class$java$lang$Throwable;

    public JMRuntimeException() {
    }

    public JMRuntimeException(String str) {
        super(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JMRuntimeException(String str, Throwable th) {
        super(str);
        Class cls;
        Class<?> cls2;
        try {
            if (class$java$lang$Throwable == null) {
                cls = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls;
            } else {
                cls = class$java$lang$Throwable;
            }
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$Throwable == null) {
                cls2 = class$("java.lang.Throwable");
                class$java$lang$Throwable = cls2;
            } else {
                cls2 = class$java$lang$Throwable;
            }
            clsArr[0] = cls2;
            cls.getMethod("initCause", clsArr).invoke(this, th);
        } catch (Exception e) {
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }
}
