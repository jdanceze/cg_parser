package org.xml.sax.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/xml/sax/helpers/SecuritySupport.class */
public class SecuritySupport {
    private static final Object securitySupport = null;

    public static SecuritySupport getInstance() {
        return (SecuritySupport) securitySupport;
    }

    public ClassLoader getContextClassLoader() {
        return null;
    }

    public String getSystemProperty(String str) {
        return System.getProperty(str);
    }

    public FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public InputStream getResourceAsStream(ClassLoader classLoader, String str) {
        return classLoader == null ? ClassLoader.getSystemResourceAsStream(str) : classLoader.getResourceAsStream(str);
    }
}
