package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.RouteInfo;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/routing/HttpRoute.class */
public final class HttpRoute implements RouteInfo, Cloneable {
    private static final HttpHost[] EMPTY_HTTP_HOST_ARRAY = new HttpHost[0];
    private final HttpHost targetHost;
    private final InetAddress localAddress;
    private final HttpHost[] proxyChain;
    private final RouteInfo.TunnelType tunnelled;
    private final RouteInfo.LayerType layered;
    private final boolean secure;

    private HttpRoute(InetAddress local, HttpHost target, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
        if (target == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        }
        if (proxies == null) {
            throw new IllegalArgumentException("Proxies may not be null.");
        }
        if (tunnelled == RouteInfo.TunnelType.TUNNELLED && proxies.length == 0) {
            throw new IllegalArgumentException("Proxy required if tunnelled.");
        }
        tunnelled = tunnelled == null ? RouteInfo.TunnelType.PLAIN : tunnelled;
        layered = layered == null ? RouteInfo.LayerType.PLAIN : layered;
        this.targetHost = target;
        this.localAddress = local;
        this.proxyChain = proxies;
        this.secure = secure;
        this.tunnelled = tunnelled;
        this.layered = layered;
    }

    public HttpRoute(HttpHost target, InetAddress local, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
        this(local, target, toChain(proxies), secure, tunnelled, layered);
    }

    public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered) {
        this(local, target, toChain(proxy), secure, tunnelled, layered);
    }

    public HttpRoute(HttpHost target, InetAddress local, boolean secure) {
        this(local, target, EMPTY_HTTP_HOST_ARRAY, secure, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
    }

    public HttpRoute(HttpHost target) {
        this((InetAddress) null, target, EMPTY_HTTP_HOST_ARRAY, false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
    }

    public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure) {
        this(local, target, toChain(proxy), secure, secure ? RouteInfo.TunnelType.TUNNELLED : RouteInfo.TunnelType.PLAIN, secure ? RouteInfo.LayerType.LAYERED : RouteInfo.LayerType.PLAIN);
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy host may not be null.");
        }
    }

    private static HttpHost[] toChain(HttpHost proxy) {
        return proxy == null ? EMPTY_HTTP_HOST_ARRAY : new HttpHost[]{proxy};
    }

    private static HttpHost[] toChain(HttpHost[] proxies) {
        if (proxies == null || proxies.length < 1) {
            return EMPTY_HTTP_HOST_ARRAY;
        }
        for (HttpHost proxy : proxies) {
            if (proxy == null) {
                throw new IllegalArgumentException("Proxy chain may not contain null elements.");
            }
        }
        HttpHost[] result = new HttpHost[proxies.length];
        System.arraycopy(proxies, 0, result, 0, proxies.length);
        return result;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final HttpHost getTargetHost() {
        return this.targetHost;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final InetAddress getLocalAddress() {
        return this.localAddress;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final int getHopCount() {
        return this.proxyChain.length + 1;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final HttpHost getHopTarget(int hop) {
        HttpHost result;
        if (hop < 0) {
            throw new IllegalArgumentException("Hop index must not be negative: " + hop);
        }
        int hopcount = getHopCount();
        if (hop >= hopcount) {
            throw new IllegalArgumentException("Hop index " + hop + " exceeds route length " + hopcount);
        }
        if (hop < hopcount - 1) {
            result = this.proxyChain[hop];
        } else {
            result = this.targetHost;
        }
        return result;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final HttpHost getProxyHost() {
        if (this.proxyChain.length == 0) {
            return null;
        }
        return this.proxyChain[0];
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final RouteInfo.TunnelType getTunnelType() {
        return this.tunnelled;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final boolean isTunnelled() {
        return this.tunnelled == RouteInfo.TunnelType.TUNNELLED;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final RouteInfo.LayerType getLayerType() {
        return this.layered;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final boolean isLayered() {
        return this.layered == RouteInfo.LayerType.LAYERED;
    }

    @Override // org.apache.http.conn.routing.RouteInfo
    public final boolean isSecure() {
        return this.secure;
    }

    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HttpRoute)) {
            return false;
        }
        HttpRoute that = (HttpRoute) o;
        boolean equal = this.targetHost.equals(that.targetHost) & (this.localAddress == that.localAddress || (this.localAddress != null && this.localAddress.equals(that.localAddress))) & (this.proxyChain == that.proxyChain || this.proxyChain.length == that.proxyChain.length) & (this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered);
        if (equal && this.proxyChain != null) {
            for (int i = 0; equal && i < this.proxyChain.length; i++) {
                equal = this.proxyChain[i].equals(that.proxyChain[i]);
            }
        }
        return equal;
    }

    public final int hashCode() {
        int hc = this.targetHost.hashCode();
        if (this.localAddress != null) {
            hc ^= this.localAddress.hashCode();
        }
        int hc2 = hc ^ this.proxyChain.length;
        HttpHost[] arr$ = this.proxyChain;
        for (HttpHost aProxyChain : arr$) {
            hc2 ^= aProxyChain.hashCode();
        }
        if (this.secure) {
            hc2 ^= 286331153;
        }
        return (hc2 ^ this.tunnelled.hashCode()) ^ this.layered.hashCode();
    }

    public final String toString() {
        StringBuilder cab = new StringBuilder(50 + (getHopCount() * 30));
        cab.append("HttpRoute[");
        if (this.localAddress != null) {
            cab.append(this.localAddress);
            cab.append("->");
        }
        cab.append('{');
        if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
            cab.append('t');
        }
        if (this.layered == RouteInfo.LayerType.LAYERED) {
            cab.append('l');
        }
        if (this.secure) {
            cab.append('s');
        }
        cab.append("}->");
        HttpHost[] arr$ = this.proxyChain;
        for (HttpHost aProxyChain : arr$) {
            cab.append(aProxyChain);
            cab.append("->");
        }
        cab.append(this.targetHost);
        cab.append(']');
        return cab.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
