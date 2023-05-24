package javassist.util.proxy;
/* loaded from: gencallgraphv3.jar:javassist-3.28.0-GA.jar:javassist/util/proxy/ProxyObject.class */
public interface ProxyObject extends Proxy {
    @Override // javassist.util.proxy.Proxy
    void setHandler(MethodHandler methodHandler);

    MethodHandler getHandler();
}
