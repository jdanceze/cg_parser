package javax.activation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:j2ee.jar:javax/activation/CommandInfo.class
 */
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/CommandInfo.class */
public class CommandInfo {
    private String verb;
    private String className;

    public CommandInfo(String verb, String className) {
        this.verb = verb;
        this.className = className;
    }

    public String getCommandName() {
        return this.verb;
    }

    public String getCommandClass() {
        return this.className;
    }

    public Object getCommandObject(DataHandler dh, ClassLoader loader) throws IOException, ClassNotFoundException {
        InputStream is;
        Object new_bean = Beans.instantiate(loader, this.className);
        if (new_bean != null) {
            if (new_bean instanceof CommandObject) {
                ((CommandObject) new_bean).setCommandContext(this.verb, dh);
            } else if ((new_bean instanceof Externalizable) && dh != null && (is = dh.getInputStream()) != null) {
                ((Externalizable) new_bean).readExternal(new ObjectInputStream(is));
            }
        }
        return new_bean;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/CommandInfo$Beans.class */
    public static final class Beans {
        static final Method instantiateMethod;

        private Beans() {
        }

        static {
            Method m;
            try {
                Class<?> c = Class.forName("java.beans.Beans");
                m = c.getDeclaredMethod("instantiate", ClassLoader.class, String.class);
            } catch (ClassNotFoundException e) {
                m = null;
            } catch (NoSuchMethodException e2) {
                m = null;
            }
            instantiateMethod = m;
        }

        static Object instantiate(ClassLoader loader, String cn) throws IOException, ClassNotFoundException {
            int b;
            if (instantiateMethod == null) {
                SecurityManager security = System.getSecurityManager();
                if (security != null) {
                    String cname = cn.replace('/', '.');
                    if (cname.startsWith("[") && (b = cname.lastIndexOf(91) + 2) > 1 && b < cname.length()) {
                        cname = cname.substring(b);
                    }
                    int i = cname.lastIndexOf(46);
                    if (i != -1) {
                        security.checkPackageAccess(cname.substring(0, i));
                    }
                }
                if (loader == null) {
                    loader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.CommandInfo.Beans.1
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            ClassLoader cl = null;
                            try {
                                cl = ClassLoader.getSystemClassLoader();
                            } catch (SecurityException e) {
                            }
                            return cl;
                        }
                    });
                }
                Class<?> beanClass = Class.forName(cn, true, loader);
                try {
                    return beanClass.newInstance();
                } catch (Exception ex) {
                    throw new ClassNotFoundException(beanClass + ": " + ex, ex);
                }
            }
            try {
                return instantiateMethod.invoke(null, loader, cn);
            } catch (IllegalAccessException e) {
                return null;
            } catch (InvocationTargetException e2) {
                return null;
            }
        }
    }
}
