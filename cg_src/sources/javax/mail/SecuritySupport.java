package javax.mail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/SecuritySupport.class */
class SecuritySupport {
    private static final Object securitySupport = null;

    public static SecuritySupport getInstance() {
        return (SecuritySupport) securitySupport;
    }

    public ClassLoader getContextClassLoader() {
        return null;
    }

    public InputStream getResourceAsStream(Class c, String name) throws IOException {
        return c.getResourceAsStream(name);
    }

    public URL[] getResources(ClassLoader cl, String name) {
        return null;
    }

    public URL[] getSystemResources(String name) {
        return null;
    }

    public InputStream openStream(URL url) throws IOException {
        return url.openStream();
    }
}
