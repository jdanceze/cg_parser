package javax.xml.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import org.apache.tools.ant.launch.Launcher;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/parsers/FactoryFinder.class */
class FactoryFinder {
    private static boolean debug;
    private static Properties jaxpProperties = null;
    private static long lastModified = -1;
    static Class class$javax$xml$parsers$FactoryFinder;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/parsers/FactoryFinder$ConfigurationError.class */
    public static class ConfigurationError extends Error {
        private Exception exception;

        ConfigurationError(String str, Exception exc) {
            super(str);
            this.exception = exc;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Exception getException() {
            return this.exception;
        }
    }

    FactoryFinder() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object find(String str, String str2) throws ConfigurationError {
        Class cls;
        String property;
        String systemProperty;
        Class cls2;
        SecuritySupport securitySupport = SecuritySupport.getInstance();
        ClassLoader contextClassLoader = securitySupport.getContextClassLoader();
        if (contextClassLoader == null) {
            if (class$javax$xml$parsers$FactoryFinder == null) {
                cls2 = class$("javax.xml.parsers.FactoryFinder");
                class$javax$xml$parsers$FactoryFinder = cls2;
            } else {
                cls2 = class$javax$xml$parsers$FactoryFinder;
            }
            contextClassLoader = cls2.getClassLoader();
        }
        dPrint(new StringBuffer().append("find factoryId=").append(str).toString());
        try {
            systemProperty = securitySupport.getSystemProperty(str);
        } catch (SecurityException e) {
        }
        if (systemProperty != null) {
            dPrint(new StringBuffer().append("found system property, value=").append(systemProperty).toString());
            return newInstance(systemProperty, contextClassLoader, true);
        }
        boolean z = false;
        File file = null;
        try {
            file = new File(new StringBuffer().append(securitySupport.getSystemProperty("java.home")).append(File.separator).append(Launcher.ANT_PRIVATELIB).append(File.separator).append("jaxp.properties").toString());
            z = securitySupport.getFileExists(file);
        } catch (SecurityException e2) {
            lastModified = -1L;
            jaxpProperties = null;
        }
        if (class$javax$xml$parsers$FactoryFinder == null) {
            cls = class$("javax.xml.parsers.FactoryFinder");
            class$javax$xml$parsers$FactoryFinder = cls;
        } else {
            cls = class$javax$xml$parsers$FactoryFinder;
        }
        synchronized (cls) {
            boolean z2 = false;
            try {
                if (lastModified >= 0) {
                    if (z) {
                        long j = lastModified;
                        lastModified = securitySupport.getLastModified(file);
                        if (j < j) {
                            z2 = true;
                        }
                    }
                    if (!z) {
                        lastModified = -1L;
                        jaxpProperties = null;
                    }
                } else if (z) {
                    z2 = true;
                    lastModified = securitySupport.getLastModified(file);
                }
                if (z2) {
                    jaxpProperties = new Properties();
                    FileInputStream fileInputStream = securitySupport.getFileInputStream(file);
                    jaxpProperties.load(fileInputStream);
                    fileInputStream.close();
                }
            } catch (Exception e3) {
                lastModified = -1L;
                jaxpProperties = null;
            }
        }
        if (jaxpProperties != null && (property = jaxpProperties.getProperty(str)) != null) {
            dPrint(new StringBuffer().append("found in jaxp.properties, value=").append(property).toString());
            return newInstance(property, contextClassLoader, true);
        }
        Object findJarServiceProvider = findJarServiceProvider(str);
        if (findJarServiceProvider != null) {
            return findJarServiceProvider;
        }
        if (str2 == null) {
            throw new ConfigurationError(new StringBuffer().append("Provider for ").append(str).append(" cannot be found").toString(), null);
        }
        dPrint(new StringBuffer().append("using fallback, value=").append(str2).toString());
        return newInstance(str2, contextClassLoader, true);
    }

    private static void dPrint(String str) {
        if (debug) {
            System.err.println(new StringBuffer().append("JAXP: ").append(str).toString());
        }
    }

    private static Object newInstance(String str, ClassLoader classLoader, boolean z) throws ConfigurationError {
        Class cls;
        Class<?> loadClass;
        try {
            if (classLoader == null) {
                loadClass = Class.forName(str);
            } else {
                try {
                    loadClass = classLoader.loadClass(str);
                } catch (ClassNotFoundException e) {
                    if (!z) {
                        throw e;
                    }
                    if (class$javax$xml$parsers$FactoryFinder == null) {
                        cls = class$("javax.xml.parsers.FactoryFinder");
                        class$javax$xml$parsers$FactoryFinder = cls;
                    } else {
                        cls = class$javax$xml$parsers$FactoryFinder;
                    }
                    classLoader = cls.getClassLoader();
                    loadClass = classLoader != null ? classLoader.loadClass(str) : Class.forName(str);
                }
            }
            Object newInstance = loadClass.newInstance();
            dPrint(new StringBuffer().append("created new instance of ").append(loadClass).append(" using ClassLoader: ").append(classLoader).toString());
            return newInstance;
        } catch (ClassNotFoundException e2) {
            throw new ConfigurationError(new StringBuffer().append("Provider ").append(str).append(" not found").toString(), e2);
        } catch (Exception e3) {
            throw new ConfigurationError(new StringBuffer().append("Provider ").append(str).append(" could not be instantiated: ").append(e3).toString(), e3);
        }
    }

    private static Object findJarServiceProvider(String str) throws ConfigurationError {
        Class cls;
        InputStream resourceAsStream;
        BufferedReader bufferedReader;
        Class cls2;
        SecuritySupport securitySupport = SecuritySupport.getInstance();
        String stringBuffer = new StringBuffer().append("META-INF/services/").append(str).toString();
        ClassLoader contextClassLoader = securitySupport.getContextClassLoader();
        if (contextClassLoader != null) {
            resourceAsStream = securitySupport.getResourceAsStream(contextClassLoader, stringBuffer);
            if (resourceAsStream == null) {
                if (class$javax$xml$parsers$FactoryFinder == null) {
                    cls2 = class$("javax.xml.parsers.FactoryFinder");
                    class$javax$xml$parsers$FactoryFinder = cls2;
                } else {
                    cls2 = class$javax$xml$parsers$FactoryFinder;
                }
                contextClassLoader = cls2.getClassLoader();
                resourceAsStream = securitySupport.getResourceAsStream(contextClassLoader, stringBuffer);
            }
        } else {
            if (class$javax$xml$parsers$FactoryFinder == null) {
                cls = class$("javax.xml.parsers.FactoryFinder");
                class$javax$xml$parsers$FactoryFinder = cls;
            } else {
                cls = class$javax$xml$parsers$FactoryFinder;
            }
            contextClassLoader = cls.getClassLoader();
            resourceAsStream = securitySupport.getResourceAsStream(contextClassLoader, stringBuffer);
        }
        if (resourceAsStream == null) {
            return null;
        }
        dPrint(new StringBuffer().append("found jar resource=").append(stringBuffer).append(" using ClassLoader: ").append(contextClassLoader).toString());
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
        }
        try {
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            if (readLine == null || "".equals(readLine)) {
                return null;
            }
            dPrint(new StringBuffer().append("found in resource, value=").append(readLine).toString());
            return newInstance(readLine, contextClassLoader, false);
        } catch (IOException e2) {
            return null;
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    static {
        try {
            String systemProperty = SecuritySupport.getInstance().getSystemProperty("jaxp.debug");
            debug = (systemProperty == null || "false".equals(systemProperty)) ? false : true;
        } catch (SecurityException e) {
            debug = false;
        }
    }
}
