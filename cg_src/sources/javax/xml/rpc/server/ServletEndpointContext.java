package javax.xml.rpc.server;

import java.security.Principal;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.handler.MessageContext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/server/ServletEndpointContext.class */
public interface ServletEndpointContext {
    MessageContext getMessageContext();

    Principal getUserPrincipal();

    HttpSession getHttpSession();

    ServletContext getServletContext();

    boolean isUserInRole(String str);
}
