package com.sun.xml.bind.util;

import java.net.URL;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/util/Which.class */
public class Which {
    public static String which(Class clazz) {
        return which(clazz.getName(), SecureLoader.getClassClassLoader(clazz));
    }

    public static String which(String classname, ClassLoader loader) {
        String classnameAsResource = classname.replace('.', '/') + ".class";
        if (loader == null) {
            loader = SecureLoader.getSystemClassLoader();
        }
        URL it = loader.getResource(classnameAsResource);
        if (it != null) {
            return it.toString();
        }
        return null;
    }
}
