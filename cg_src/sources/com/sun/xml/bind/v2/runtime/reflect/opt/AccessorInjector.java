package com.sun.xml.bind.v2.runtime.reflect.opt;

import com.sun.xml.bind.Util;
import com.sun.xml.bind.v2.bytecode.ClassTailor;
import java.io.InputStream;
import java.util.logging.Logger;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/reflect/opt/AccessorInjector.class */
class AccessorInjector {
    private static final Logger logger = Util.getClassLogger();
    protected static final boolean noOptimize;
    private static final ClassLoader CLASS_LOADER;

    AccessorInjector() {
    }

    static {
        noOptimize = Util.getSystemProperty(new StringBuilder().append(ClassTailor.class.getName()).append(".noOptimize").toString()) != null;
        if (noOptimize) {
            logger.info("The optimized code generation is disabled");
        }
        CLASS_LOADER = SecureLoader.getClassClassLoader(AccessorInjector.class);
    }

    private static byte[] tailor(String templateClassName, String newClassName, String... replacements) {
        InputStream resource;
        if (CLASS_LOADER != null) {
            resource = CLASS_LOADER.getResourceAsStream(templateClassName + ".class");
        } else {
            resource = ClassLoader.getSystemResourceAsStream(templateClassName + ".class");
        }
        if (resource == null) {
            return null;
        }
        return ClassTailor.tailor(resource, templateClassName, newClassName, replacements);
    }
}
