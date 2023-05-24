package org.eclipse.jdt.internal.jarinjarloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
/* loaded from: gencallgraphv3.jar:org/eclipse/jdt/internal/jarinjarloader/RsrcURLConnection.class */
public class RsrcURLConnection extends URLConnection {
    private ClassLoader classLoader;

    public RsrcURLConnection(URL url, ClassLoader classLoader) {
        super(url);
        this.classLoader = classLoader;
    }

    @Override // java.net.URLConnection
    public void connect() throws IOException {
    }

    @Override // java.net.URLConnection
    public InputStream getInputStream() throws IOException {
        String file = URLDecoder.decode(this.url.getFile(), "UTF-8");
        InputStream result = this.classLoader.getResourceAsStream(file);
        if (result == null) {
            throw new MalformedURLException("Could not open InputStream for URL '" + this.url + "'");
        }
        return result;
    }
}
