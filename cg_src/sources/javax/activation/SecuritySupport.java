package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
/* loaded from: gencallgraphv3.jar:javax.activation-api-1.2.0.jar:javax/activation/SecuritySupport.class */
class SecuritySupport {
    private SecuritySupport() {
    }

    public static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.SecuritySupport.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e) {
                }
                return cl;
            }
        });
    }

    public static InputStream getResourceAsStream(final Class c, final String name) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.activation.SecuritySupport.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    return c.getResourceAsStream(name);
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    public static URL[] getResources(final ClassLoader cl, final String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.SecuritySupport.3
            @Override // java.security.PrivilegedAction
            public Object run() {
                URL[] ret = null;
                try {
                    List v = new ArrayList();
                    Enumeration e = cl.getResources(name);
                    while (e != null && e.hasMoreElements()) {
                        URL url = e.nextElement();
                        if (url != null) {
                            v.add(url);
                        }
                    }
                    if (v.size() > 0) {
                        ret = (URL[]) v.toArray(new URL[v.size()]);
                    }
                } catch (IOException e2) {
                } catch (SecurityException e3) {
                }
                return ret;
            }
        });
    }

    public static URL[] getSystemResources(final String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.activation.SecuritySupport.4
            @Override // java.security.PrivilegedAction
            public Object run() {
                URL[] ret = null;
                try {
                    List v = new ArrayList();
                    Enumeration e = ClassLoader.getSystemResources(name);
                    while (e != null && e.hasMoreElements()) {
                        URL url = e.nextElement();
                        if (url != null) {
                            v.add(url);
                        }
                    }
                    if (v.size() > 0) {
                        ret = (URL[]) v.toArray(new URL[v.size()]);
                    }
                } catch (IOException e2) {
                } catch (SecurityException e3) {
                }
                return ret;
            }
        });
    }

    public static InputStream openStream(final URL url) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: javax.activation.SecuritySupport.5
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    return url.openStream();
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }
}
