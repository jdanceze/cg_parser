package com.sun.xml.bind;

import java.util.logging.Logger;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/Util.class */
public final class Util {
    private Util() {
    }

    public static Logger getClassLogger() {
        try {
            StackTraceElement[] trace = new Exception().getStackTrace();
            return Logger.getLogger(trace[1].getClassName());
        } catch (SecurityException e) {
            return Logger.getLogger("com.sun.xml.bind");
        }
    }

    public static String getSystemProperty(String name) {
        try {
            return System.getProperty(name);
        } catch (SecurityException e) {
            return null;
        }
    }
}
