package com.oreilly.servlet;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/VersionDetector.class */
public class VersionDetector {
    static String servletVersion;
    static String javaVersion;

    public static String getServletVersion() {
        if (servletVersion != null) {
            return servletVersion;
        }
        String ver = null;
        try {
            Class.forName("javax.servlet.http.HttpSession");
            Class.forName("javax.servlet.RequestDispatcher");
            Class.forName("javax.servlet.http.HttpServletResponse").getDeclaredField("SC_EXPECTATION_FAILED");
            Class.forName("javax.servlet.Filter");
            ver = "2.3";
        } catch (Throwable th) {
        }
        servletVersion = ver;
        return servletVersion;
    }

    public static String getJavaVersion() {
        if (javaVersion != null) {
            return javaVersion;
        }
        String ver = null;
        try {
            Class.forName("java.lang.Void");
            Class.forName("java.lang.ThreadLocal");
            Class.forName("java.lang.StrictMath");
            Class.forName("java.net.URI");
            ver = "1.4";
        } catch (Throwable th) {
        }
        javaVersion = ver;
        return javaVersion;
    }
}
