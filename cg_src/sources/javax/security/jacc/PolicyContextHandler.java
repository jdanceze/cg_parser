package javax.security.jacc;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/security/jacc/PolicyContextHandler.class */
public interface PolicyContextHandler {
    boolean supports(String str) throws PolicyContextException;

    String[] getKeys() throws PolicyContextException;

    Object getContext(String str, Object obj) throws PolicyContextException;
}
