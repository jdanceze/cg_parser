package javax.management.loading;

import java.net.URL;
import java.net.URLStreamHandlerFactory;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/loading/PrivateMLet.class */
public class PrivateMLet extends MLet implements PrivateClassLoader {
    private static final long serialVersionUID = 2503458973393711979L;

    public PrivateMLet(URL[] urlArr, boolean z) {
        super(urlArr, z);
    }

    public PrivateMLet(URL[] urlArr, ClassLoader classLoader, boolean z) {
        super(urlArr, classLoader, z);
    }

    public PrivateMLet(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory, boolean z) {
        super(urlArr, classLoader, uRLStreamHandlerFactory, z);
    }
}
