package org.apache.http.conn.scheme;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.LangUtils;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/scheme/Scheme.class */
public final class Scheme {
    private final String name;
    private final SocketFactory socketFactory;
    private final int defaultPort;
    private final boolean layered;
    private String stringRep;

    public Scheme(String name, SocketFactory factory, int port) {
        if (name == null) {
            throw new IllegalArgumentException("Scheme name may not be null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Socket factory may not be null");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port is invalid: " + port);
        }
        this.name = name.toLowerCase(Locale.ENGLISH);
        this.socketFactory = factory;
        this.defaultPort = port;
        this.layered = factory instanceof LayeredSocketFactory;
    }

    public final int getDefaultPort() {
        return this.defaultPort;
    }

    public final SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isLayered() {
        return this.layered;
    }

    public final int resolvePort(int port) {
        return port <= 0 ? this.defaultPort : port;
    }

    public final String toString() {
        if (this.stringRep == null) {
            this.stringRep = this.name + ':' + Integer.toString(this.defaultPort);
        }
        return this.stringRep;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Scheme) {
            Scheme s = (Scheme) obj;
            return this.name.equals(s.name) && this.defaultPort == s.defaultPort && this.layered == s.layered && this.socketFactory.equals(s.socketFactory);
        }
        return false;
    }

    public int hashCode() {
        int hash = LangUtils.hashCode(17, this.defaultPort);
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(hash, this.name), this.layered), this.socketFactory);
    }
}
