package javax.mail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Vector;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/SecuritySupport12.class */
class SecuritySupport12 extends SecuritySupport {
    @Override // javax.mail.SecuritySupport
    public ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction(this) { // from class: javax.mail.SecuritySupport12.1
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
            }

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

    @Override // javax.mail.SecuritySupport
    public InputStream getResourceAsStream(Class c, String name) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction(this, c, name) { // from class: javax.mail.SecuritySupport12.2
                private final Class val$c;
                private final String val$name;
                private final SecuritySupport12 this$0;

                {
                    this.this$0 = this;
                    this.val$c = c;
                    this.val$name = name;
                }

                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    return this.val$c.getResourceAsStream(this.val$name);
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    @Override // javax.mail.SecuritySupport
    public URL[] getResources(ClassLoader cl, String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction(this, cl, name) { // from class: javax.mail.SecuritySupport12.3
            private final ClassLoader val$cl;
            private final String val$name;
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
                this.val$cl = cl;
                this.val$name = name;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                URL[] ret = null;
                try {
                    Vector v = new Vector();
                    Enumeration e = this.val$cl.getResources(this.val$name);
                    while (e != null && e.hasMoreElements()) {
                        URL url = e.nextElement();
                        if (url != null) {
                            v.addElement(url);
                        }
                    }
                    if (v.size() > 0) {
                        ret = new URL[v.size()];
                        v.copyInto(ret);
                    }
                } catch (IOException e2) {
                } catch (SecurityException e3) {
                }
                return ret;
            }
        });
    }

    @Override // javax.mail.SecuritySupport
    public URL[] getSystemResources(String name) {
        return (URL[]) AccessController.doPrivileged(new PrivilegedAction(this, name) { // from class: javax.mail.SecuritySupport12.4
            private final String val$name;
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
                this.val$name = name;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                URL[] ret = null;
                try {
                    Vector v = new Vector();
                    Enumeration e = ClassLoader.getSystemResources(this.val$name);
                    while (e != null && e.hasMoreElements()) {
                        URL url = e.nextElement();
                        if (url != null) {
                            v.addElement(url);
                        }
                    }
                    if (v.size() > 0) {
                        ret = new URL[v.size()];
                        v.copyInto(ret);
                    }
                } catch (IOException e2) {
                } catch (SecurityException e3) {
                }
                return ret;
            }
        });
    }

    @Override // javax.mail.SecuritySupport
    public InputStream openStream(URL url) throws IOException {
        try {
            return (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction(this, url) { // from class: javax.mail.SecuritySupport12.5
                private final URL val$url;
                private final SecuritySupport12 this$0;

                {
                    this.this$0 = this;
                    this.val$url = url;
                }

                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    return this.val$url.openStream();
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }
}
