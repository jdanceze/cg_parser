package com.oreilly.servlet;

import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.security.Provider;
import java.security.Security;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/HttpsMessage.class */
public class HttpsMessage extends HttpMessage {
    static boolean m_bStreamHandlerSet = false;

    public HttpsMessage(String szURL) throws Exception {
        super(null);
        if (!m_bStreamHandlerSet) {
            String szVendor = System.getProperty("java.vendor");
            String szVersion = System.getProperty("java.version");
            Double dVersion = new Double(szVersion.substring(0, 3));
            if (-1 < szVendor.indexOf("Microsoft")) {
                try {
                    Class clsFactory = Class.forName("com.ms.net.wininet.WininetStreamHandlerFactory");
                    if (null != clsFactory) {
                        URL.setURLStreamHandlerFactory((URLStreamHandlerFactory) clsFactory.newInstance());
                    }
                } catch (ClassNotFoundException cfe) {
                    throw new Exception(new StringBuffer().append("Unable to load the Microsoft SSL stream handler.  Check classpath.").append(cfe.toString()).toString());
                } catch (Error e) {
                    m_bStreamHandlerSet = true;
                }
            } else if (1.2d <= dVersion.doubleValue()) {
                System.getProperties().put("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
                try {
                    Class clsFactory2 = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
                    if (null != clsFactory2 && null == Security.getProvider("SunJSSE")) {
                        Security.addProvider((Provider) clsFactory2.newInstance());
                    }
                } catch (ClassNotFoundException cfe2) {
                    throw new Exception(new StringBuffer().append("Unable to load the JSSE SSL stream handler.  Check classpath.").append(cfe2.toString()).toString());
                }
            }
            m_bStreamHandlerSet = true;
        }
        this.servlet = new URL(szURL);
    }
}
