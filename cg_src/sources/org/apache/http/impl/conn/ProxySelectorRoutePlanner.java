package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.protocol.HttpContext;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/ProxySelectorRoutePlanner.class */
public class ProxySelectorRoutePlanner implements HttpRoutePlanner {
    protected final SchemeRegistry schemeRegistry;
    protected ProxySelector proxySelector;

    public ProxySelectorRoutePlanner(SchemeRegistry schreg, ProxySelector prosel) {
        if (schreg == null) {
            throw new IllegalArgumentException("SchemeRegistry must not be null.");
        }
        this.schemeRegistry = schreg;
        this.proxySelector = prosel;
    }

    public ProxySelector getProxySelector() {
        return this.proxySelector;
    }

    public void setProxySelector(ProxySelector prosel) {
        this.proxySelector = prosel;
    }

    @Override // org.apache.http.conn.routing.HttpRoutePlanner
    public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        HttpRoute route;
        if (request == null) {
            throw new IllegalStateException("Request must not be null.");
        }
        HttpRoute route2 = ConnRouteParams.getForcedRoute(request.getParams());
        if (route2 != null) {
            return route2;
        }
        if (target == null) {
            throw new IllegalStateException("Target host must not be null.");
        }
        InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
        HttpHost proxy = determineProxy(target, request, context);
        Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
        boolean secure = schm.isLayered();
        if (proxy == null) {
            route = new HttpRoute(target, local, secure);
        } else {
            route = new HttpRoute(target, local, proxy, secure);
        }
        return route;
    }

    protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
        ProxySelector psel = this.proxySelector;
        if (psel == null) {
            psel = ProxySelector.getDefault();
        }
        if (psel == null) {
            return null;
        }
        try {
            URI targetURI = new URI(target.toURI());
            List<Proxy> proxies = psel.select(targetURI);
            Proxy p = chooseProxy(proxies, target, request, context);
            HttpHost result = null;
            if (p.type() == Proxy.Type.HTTP) {
                if (!(p.address() instanceof InetSocketAddress)) {
                    throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
                }
                InetSocketAddress isa = (InetSocketAddress) p.address();
                result = new HttpHost(getHost(isa), isa.getPort());
            }
            return result;
        } catch (URISyntaxException usx) {
            throw new HttpException("Cannot convert host to URI: " + target, usx);
        }
    }

    protected String getHost(InetSocketAddress isa) {
        return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
    }

    protected Proxy chooseProxy(List<Proxy> proxies, HttpHost target, HttpRequest request, HttpContext context) {
        if (proxies == null || proxies.isEmpty()) {
            throw new IllegalArgumentException("Proxy list must not be empty.");
        }
        Proxy result = null;
        for (int i = 0; result == null && i < proxies.size(); i++) {
            Proxy p = proxies.get(i);
            switch (AnonymousClass1.$SwitchMap$java$net$Proxy$Type[p.type().ordinal()]) {
                case 1:
                case 2:
                    result = p;
                    break;
            }
        }
        if (result == null) {
            result = Proxy.NO_PROXY;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.apache.http.impl.conn.ProxySelectorRoutePlanner$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/conn/ProxySelectorRoutePlanner$1.class */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$java$net$Proxy$Type = new int[Proxy.Type.values().length];

        static {
            try {
                $SwitchMap$java$net$Proxy$Type[Proxy.Type.DIRECT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$net$Proxy$Type[Proxy.Type.HTTP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$net$Proxy$Type[Proxy.Type.SOCKS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }
}
