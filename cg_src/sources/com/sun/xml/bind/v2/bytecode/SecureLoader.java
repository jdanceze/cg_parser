package com.sun.xml.bind.v2.bytecode;

import java.security.AccessController;
import java.security.PrivilegedAction;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/bytecode/SecureLoader.class */
class SecureLoader {
    SecureLoader() {
    }

    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.bind.v2.bytecode.SecureLoader.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ClassLoader getClassClassLoader(final Class c) {
        if (System.getSecurityManager() == null) {
            return c.getClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.bind.v2.bytecode.SecureLoader.2
            @Override // java.security.PrivilegedAction
            public Object run() {
                return c.getClassLoader();
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.bind.v2.bytecode.SecureLoader.3
            @Override // java.security.PrivilegedAction
            public Object run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }
}
