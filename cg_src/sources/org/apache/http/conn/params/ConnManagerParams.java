package org.apache.http.conn.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/params/ConnManagerParams.class */
public final class ConnManagerParams implements ConnManagerPNames {
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
    private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE = new ConnPerRoute() { // from class: org.apache.http.conn.params.ConnManagerParams.1
        @Override // org.apache.http.conn.params.ConnPerRoute
        public int getMaxForRoute(HttpRoute route) {
            return 2;
        }
    };

    public static long getTimeout(HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        return params.getLongParameter(ConnManagerPNames.TIMEOUT, 0L);
    }

    public static void setTimeout(HttpParams params, long timeout) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        params.setLongParameter(ConnManagerPNames.TIMEOUT, timeout);
    }

    public static void setMaxConnectionsPerRoute(HttpParams params, ConnPerRoute connPerRoute) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters must not be null.");
        }
        params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, connPerRoute);
    }

    public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters must not be null.");
        }
        ConnPerRoute connPerRoute = (ConnPerRoute) params.getParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE);
        if (connPerRoute == null) {
            connPerRoute = DEFAULT_CONN_PER_ROUTE;
        }
        return connPerRoute;
    }

    public static void setMaxTotalConnections(HttpParams params, int maxTotalConnections) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters must not be null.");
        }
        params.setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, maxTotalConnections);
    }

    public static int getMaxTotalConnections(HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters must not be null.");
        }
        return params.getIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 20);
    }
}
