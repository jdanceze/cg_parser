package org.apache.http.conn.routing;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/routing/HttpRouteDirector.class */
public interface HttpRouteDirector {
    public static final int UNREACHABLE = -1;
    public static final int COMPLETE = 0;
    public static final int CONNECT_TARGET = 1;
    public static final int CONNECT_PROXY = 2;
    public static final int TUNNEL_TARGET = 3;
    public static final int TUNNEL_PROXY = 4;
    public static final int LAYER_PROTOCOL = 5;

    int nextStep(RouteInfo routeInfo, RouteInfo routeInfo2);
}
