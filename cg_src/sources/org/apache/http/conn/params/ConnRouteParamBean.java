package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/params/ConnRouteParamBean.class */
public class ConnRouteParamBean extends HttpAbstractParamBean {
    public ConnRouteParamBean(HttpParams params) {
        super(params);
    }

    public void setDefaultProxy(HttpHost defaultProxy) {
        this.params.setParameter(ConnRoutePNames.DEFAULT_PROXY, defaultProxy);
    }

    public void setLocalAddress(InetAddress address) {
        this.params.setParameter(ConnRoutePNames.LOCAL_ADDRESS, address);
    }

    public void setForcedRoute(HttpRoute route) {
        this.params.setParameter(ConnRoutePNames.FORCED_ROUTE, route);
    }
}
