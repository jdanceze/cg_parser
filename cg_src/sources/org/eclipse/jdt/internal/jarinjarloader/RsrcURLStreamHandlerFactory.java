package org.eclipse.jdt.internal.jarinjarloader;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
/* loaded from: gencallgraphv3.jar:org/eclipse/jdt/internal/jarinjarloader/RsrcURLStreamHandlerFactory.class */
public class RsrcURLStreamHandlerFactory implements URLStreamHandlerFactory {
    private ClassLoader classLoader;
    private URLStreamHandlerFactory chainFac;

    public RsrcURLStreamHandlerFactory(ClassLoader cl) {
        this.classLoader = cl;
    }

    @Override // java.net.URLStreamHandlerFactory
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("rsrc".equals(protocol)) {
            return new RsrcURLStreamHandler(this.classLoader);
        }
        if (this.chainFac != null) {
            return this.chainFac.createURLStreamHandler(protocol);
        }
        return null;
    }

    public void setURLStreamHandlerFactory(URLStreamHandlerFactory fac) {
        this.chainFac = fac;
    }
}
