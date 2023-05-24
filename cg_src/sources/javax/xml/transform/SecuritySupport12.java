package javax.xml.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/SecuritySupport12.class */
class SecuritySupport12 extends SecuritySupport {
    @Override // javax.xml.transform.SecuritySupport
    public ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction(this) { // from class: javax.xml.transform.SecuritySupport12.1
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                ClassLoader classLoader = null;
                try {
                    classLoader = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e) {
                }
                return classLoader;
            }
        });
    }

    @Override // javax.xml.transform.SecuritySupport
    public String getSystemProperty(String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction(this, str) { // from class: javax.xml.transform.SecuritySupport12.2
            private final String val$propName;
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
                this.val$propName = str;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(this.val$propName);
            }
        });
    }

    @Override // javax.xml.transform.SecuritySupport
    public FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction(this, file) { // from class: javax.xml.transform.SecuritySupport12.3
                private final File val$file;
                private final SecuritySupport12 this$0;

                {
                    this.this$0 = this;
                    this.val$file = file;
                }

                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws FileNotFoundException {
                    return new FileInputStream(this.val$file);
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((FileNotFoundException) e.getException());
        }
    }

    @Override // javax.xml.transform.SecuritySupport
    public InputStream getResourceAsStream(ClassLoader classLoader, String str) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction(this, classLoader, str) { // from class: javax.xml.transform.SecuritySupport12.4
            private final ClassLoader val$cl;
            private final String val$name;
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
                this.val$cl = classLoader;
                this.val$name = str;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                return this.val$cl == null ? ClassLoader.getSystemResourceAsStream(this.val$name) : this.val$cl.getResourceAsStream(this.val$name);
            }
        });
    }

    @Override // javax.xml.transform.SecuritySupport
    public boolean getFileExists(File file) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction(this, file) { // from class: javax.xml.transform.SecuritySupport12.5
            private final File val$f;
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
                this.val$f = file;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                return new Boolean(this.val$f.exists());
            }
        })).booleanValue();
    }

    @Override // javax.xml.transform.SecuritySupport
    public long getLastModified(File file) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction(this, file) { // from class: javax.xml.transform.SecuritySupport12.6
            private final File val$f;
            private final SecuritySupport12 this$0;

            {
                this.this$0 = this;
                this.val$f = file;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                return new Long(this.val$f.lastModified());
            }
        })).longValue();
    }
}
