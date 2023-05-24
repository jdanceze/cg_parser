package org.apache.commons.logging;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:commons-logging-1.1.1.jar:org/apache/commons/logging/LogFactory.class */
public abstract class LogFactory {
    public static final String PRIORITY_KEY = "priority";
    public static final String TCCL_KEY = "use_tccl";
    public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
    public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
    private static String diagnosticPrefix;
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
    private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
    private static ClassLoader thisClassLoader;
    protected static Hashtable factories;
    static Class class$java$lang$Thread;
    static Class class$org$apache$commons$logging$LogFactory;
    private static PrintStream diagnosticsStream = null;
    protected static LogFactory nullClassLoaderFactory = null;

    public abstract Object getAttribute(String str);

    public abstract String[] getAttributeNames();

    public abstract Log getInstance(Class cls) throws LogConfigurationException;

    public abstract Log getInstance(String str) throws LogConfigurationException;

    public abstract void release();

    public abstract void removeAttribute(String str);

    public abstract void setAttribute(String str, Object obj);

    static {
        Class cls;
        Class cls2;
        factories = null;
        if (class$org$apache$commons$logging$LogFactory == null) {
            cls = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls;
        } else {
            cls = class$org$apache$commons$logging$LogFactory;
        }
        thisClassLoader = getClassLoader(cls);
        initDiagnostics();
        if (class$org$apache$commons$logging$LogFactory == null) {
            cls2 = class$(FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls2;
        } else {
            cls2 = class$org$apache$commons$logging$LogFactory;
        }
        logClassLoaderEnvironment(cls2);
        factories = createFactoryStore();
        if (isDiagnosticsEnabled()) {
            logDiagnostic("BOOTSTRAP COMPLETED");
        }
    }

    private static final Hashtable createFactoryStore() {
        String storeImplementationClass;
        Hashtable result = null;
        try {
            storeImplementationClass = getSystemProperty(HASHTABLE_IMPLEMENTATION_PROPERTY, null);
        } catch (SecurityException e) {
            storeImplementationClass = null;
        }
        if (storeImplementationClass == null) {
            storeImplementationClass = WEAK_HASHTABLE_CLASSNAME;
        }
        try {
            Class implementationClass = Class.forName(storeImplementationClass);
            result = (Hashtable) implementationClass.newInstance();
        } catch (Throwable th) {
            if (!WEAK_HASHTABLE_CLASSNAME.equals(storeImplementationClass)) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
                } else {
                    System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
                }
            }
        }
        if (result == null) {
            result = new Hashtable();
        }
        return result;
    }

    private static String trim(String src) {
        if (src == null) {
            return null;
        }
        return src.trim();
    }

    public static LogFactory getFactory() throws LogConfigurationException {
        BufferedReader rd;
        String useTCCLStr;
        ClassLoader contextClassLoader = getContextClassLoaderInternal();
        if (contextClassLoader == null && isDiagnosticsEnabled()) {
            logDiagnostic("Context classloader is null.");
        }
        LogFactory factory = getCachedFactory(contextClassLoader);
        if (factory != null) {
            return factory;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[LOOKUP] LogFactory implementation requested for the first time for context classloader ").append(objectId(contextClassLoader)).toString());
            logHierarchy("[LOOKUP] ", contextClassLoader);
        }
        Properties props = getConfigurationFile(contextClassLoader, FACTORY_PROPERTIES);
        ClassLoader baseClassLoader = contextClassLoader;
        if (props != null && (useTCCLStr = props.getProperty(TCCL_KEY)) != null && !Boolean.valueOf(useTCCLStr).booleanValue()) {
            baseClassLoader = thisClassLoader;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic("[LOOKUP] Looking for system property [org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
        }
        try {
            String factoryClass = getSystemProperty(FACTORY_PROPERTY, null);
            if (factoryClass != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] Creating an instance of LogFactory class '").append(factoryClass).append("' as specified by system property ").append(FACTORY_PROPERTY).toString());
                }
                factory = newFactory(factoryClass, baseClassLoader, contextClassLoader);
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No system property [org.apache.commons.logging.LogFactory] defined.");
            }
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(e.getMessage())).append("]. Trying alternative implementations...").toString());
            }
        } catch (RuntimeException e2) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[LOOKUP] An exception occurred while trying to create an instance of the custom factory class: [").append(trim(e2.getMessage())).append("] as specified by a system property.").toString());
            }
            throw e2;
        }
        if (factory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Looking for a resource file of name [META-INF/services/org.apache.commons.logging.LogFactory] to define the LogFactory subclass to use...");
            }
            try {
                InputStream is = getResourceAsStream(contextClassLoader, SERVICE_ID);
                if (is != null) {
                    try {
                        rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    } catch (UnsupportedEncodingException e3) {
                        rd = new BufferedReader(new InputStreamReader(is));
                    }
                    String factoryClassName = rd.readLine();
                    rd.close();
                    if (factoryClassName != null && !"".equals(factoryClassName)) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("[LOOKUP]  Creating an instance of LogFactory class ").append(factoryClassName).append(" as specified by file '").append(SERVICE_ID).append("' which was present in the path of the context").append(" classloader.").toString());
                        }
                        factory = newFactory(factoryClassName, baseClassLoader, contextClassLoader);
                    }
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] No resource file with name 'META-INF/services/org.apache.commons.logging.LogFactory' found.");
                }
            } catch (Exception ex) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("[LOOKUP] A security exception occurred while trying to create an instance of the custom factory class: [").append(trim(ex.getMessage())).append("]. Trying alternative implementations...").toString());
                }
            }
        }
        if (factory == null) {
            if (props != null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Looking in properties file for entry with key 'org.apache.commons.logging.LogFactory' to define the LogFactory subclass to use...");
                }
                String factoryClass2 = props.getProperty(FACTORY_PROPERTY);
                if (factoryClass2 != null) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file specifies LogFactory subclass '").append(factoryClass2).append("'").toString());
                    }
                    factory = newFactory(factoryClass2, baseClassLoader, contextClassLoader);
                } else if (isDiagnosticsEnabled()) {
                    logDiagnostic("[LOOKUP] Properties file has no entry specifying LogFactory subclass.");
                }
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] No properties file available to determine LogFactory subclass from..");
            }
        }
        if (factory == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("[LOOKUP] Loading the default LogFactory implementation 'org.apache.commons.logging.impl.LogFactoryImpl' via the same classloader that loaded this LogFactory class (ie not looking in the context classloader).");
            }
            factory = newFactory(FACTORY_DEFAULT, thisClassLoader, contextClassLoader);
        }
        if (factory != null) {
            cacheFactory(contextClassLoader, factory);
            if (props != null) {
                Enumeration names = props.propertyNames();
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    String value = props.getProperty(name);
                    factory.setAttribute(name, value);
                }
            }
        }
        return factory;
    }

    public static Log getLog(Class clazz) throws LogConfigurationException {
        return getFactory().getInstance(clazz);
    }

    public static Log getLog(String name) throws LogConfigurationException {
        return getFactory().getInstance(name);
    }

    public static void release(ClassLoader classLoader) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Releasing factory for classloader ").append(objectId(classLoader)).toString());
        }
        synchronized (factories) {
            if (classLoader == null) {
                if (nullClassLoaderFactory != null) {
                    nullClassLoaderFactory.release();
                    nullClassLoaderFactory = null;
                }
            } else {
                LogFactory factory = (LogFactory) factories.get(classLoader);
                if (factory != null) {
                    factory.release();
                    factories.remove(classLoader);
                }
            }
        }
    }

    public static void releaseAll() {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Releasing factory for all classloaders.");
        }
        synchronized (factories) {
            Enumeration elements = factories.elements();
            while (elements.hasMoreElements()) {
                LogFactory element = (LogFactory) elements.nextElement();
                element.release();
            }
            factories.clear();
            if (nullClassLoaderFactory != null) {
                nullClassLoaderFactory.release();
                nullClassLoaderFactory = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static ClassLoader getClassLoader(Class clazz) {
        try {
            return clazz.getClassLoader();
        } catch (SecurityException ex) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Unable to get classloader for class '").append(clazz).append("' due to security restrictions - ").append(ex.getMessage()).toString());
            }
            throw ex;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return directGetContextClassLoader();
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.commons.logging.LogFactory.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return LogFactory.directGetContextClassLoader();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
        Class cls;
        Class cls2;
        ClassLoader classLoader = null;
        try {
            if (class$java$lang$Thread == null) {
                cls2 = class$("java.lang.Thread");
                class$java$lang$Thread = cls2;
            } else {
                cls2 = class$java$lang$Thread;
            }
            Method method = cls2.getMethod("getContextClassLoader", null);
            try {
                classLoader = (ClassLoader) method.invoke(Thread.currentThread(), null);
            } catch (IllegalAccessException e) {
                throw new LogConfigurationException("Unexpected IllegalAccessException", e);
            } catch (InvocationTargetException e2) {
                if (!(e2.getTargetException() instanceof SecurityException)) {
                    throw new LogConfigurationException("Unexpected InvocationTargetException", e2.getTargetException());
                }
            }
        } catch (NoSuchMethodException e3) {
            if (class$org$apache$commons$logging$LogFactory == null) {
                cls = class$(FACTORY_PROPERTY);
                class$org$apache$commons$logging$LogFactory = cls;
            } else {
                cls = class$org$apache$commons$logging$LogFactory;
            }
            classLoader = getClassLoader(cls);
        }
        return classLoader;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private static LogFactory getCachedFactory(ClassLoader contextClassLoader) {
        LogFactory factory;
        if (contextClassLoader == null) {
            factory = nullClassLoaderFactory;
        } else {
            factory = (LogFactory) factories.get(contextClassLoader);
        }
        return factory;
    }

    private static void cacheFactory(ClassLoader classLoader, LogFactory factory) {
        if (factory != null) {
            if (classLoader == null) {
                nullClassLoaderFactory = factory;
            } else {
                factories.put(classLoader, factory);
            }
        }
    }

    protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader, ClassLoader contextClassLoader) throws LogConfigurationException {
        Object result = AccessController.doPrivileged(new PrivilegedAction(factoryClass, classLoader) { // from class: org.apache.commons.logging.LogFactory.2
            private final String val$factoryClass;
            private final ClassLoader val$classLoader;

            {
                this.val$factoryClass = factoryClass;
                this.val$classLoader = classLoader;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                return LogFactory.createFactory(this.val$factoryClass, this.val$classLoader);
            }
        });
        if (result instanceof LogConfigurationException) {
            LogConfigurationException ex = (LogConfigurationException) result;
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("An error occurred while loading the factory class:").append(ex.getMessage()).toString());
            }
            throw ex;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Created object ").append(objectId(result)).append(" to manage classloader ").append(objectId(contextClassLoader)).toString());
        }
        return (LogFactory) result;
    }

    protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader) {
        return newFactory(factoryClass, classLoader, null);
    }

    protected static Object createFactory(String factoryClass, ClassLoader classLoader) {
        Class cls;
        Class cls2;
        String msg;
        Class cls3;
        Class cls4;
        Class logFactoryClass = null;
        try {
            if (classLoader != null) {
                try {
                    try {
                        logFactoryClass = classLoader.loadClass(factoryClass);
                        if (class$org$apache$commons$logging$LogFactory == null) {
                            cls3 = class$(FACTORY_PROPERTY);
                            class$org$apache$commons$logging$LogFactory = cls3;
                        } else {
                            cls3 = class$org$apache$commons$logging$LogFactory;
                        }
                        if (cls3.isAssignableFrom(logFactoryClass)) {
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(new StringBuffer().append("Loaded class ").append(logFactoryClass.getName()).append(" from classloader ").append(objectId(classLoader)).toString());
                            }
                        } else if (isDiagnosticsEnabled()) {
                            StringBuffer append = new StringBuffer().append("Factory class ").append(logFactoryClass.getName()).append(" loaded from classloader ").append(objectId(logFactoryClass.getClassLoader())).append(" does not extend '");
                            if (class$org$apache$commons$logging$LogFactory == null) {
                                cls4 = class$(FACTORY_PROPERTY);
                                class$org$apache$commons$logging$LogFactory = cls4;
                            } else {
                                cls4 = class$org$apache$commons$logging$LogFactory;
                            }
                            logDiagnostic(append.append(cls4.getName()).append("' as loaded by this classloader.").toString());
                            logHierarchy("[BAD CL TREE] ", classLoader);
                        }
                        return (LogFactory) logFactoryClass.newInstance();
                    } catch (ClassCastException e) {
                        if (classLoader == thisClassLoader) {
                            boolean implementsLogFactory = implementsLogFactory(logFactoryClass);
                            StringBuffer append2 = new StringBuffer().append("The application has specified that a custom LogFactory implementation should be used but Class '").append(factoryClass).append("' cannot be converted to '");
                            if (class$org$apache$commons$logging$LogFactory == null) {
                                cls2 = class$(FACTORY_PROPERTY);
                                class$org$apache$commons$logging$LogFactory = cls2;
                            } else {
                                cls2 = class$org$apache$commons$logging$LogFactory;
                            }
                            String msg2 = append2.append(cls2.getName()).append("'. ").toString();
                            if (implementsLogFactory) {
                                msg = new StringBuffer().append(msg2).append("The conflict is caused by the presence of multiple LogFactory classes in incompatible classloaders. ").append("Background can be found in http://commons.apache.org/logging/tech.html. ").append("If you have not explicitly specified a custom LogFactory then it is likely that ").append("the container has set one without your knowledge. ").append("In this case, consider using the commons-logging-adapters.jar file or ").append("specifying the standard LogFactory from the command line. ").toString();
                            } else {
                                msg = new StringBuffer().append(msg2).append("Please check the custom implementation. ").toString();
                            }
                            String msg3 = new StringBuffer().append(msg).append("Help can be found @http://commons.apache.org/logging/troubleshooting.html.").toString();
                            if (isDiagnosticsEnabled()) {
                                logDiagnostic(msg3);
                            }
                            ClassCastException ex = new ClassCastException(msg3);
                            throw ex;
                        }
                    }
                } catch (ClassNotFoundException ex2) {
                    if (classLoader == thisClassLoader) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("Unable to locate any class called '").append(factoryClass).append("' via classloader ").append(objectId(classLoader)).toString());
                        }
                        throw ex2;
                    }
                } catch (NoClassDefFoundError e2) {
                    if (classLoader == thisClassLoader) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("Class '").append(factoryClass).append("' cannot be loaded").append(" via classloader ").append(objectId(classLoader)).append(" - it depends on some other class that cannot").append(" be found.").toString());
                        }
                        throw e2;
                    }
                }
            }
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Unable to load factory class via classloader ").append(objectId(classLoader)).append(" - trying the classloader associated with this LogFactory.").toString());
            }
            return (LogFactory) Class.forName(factoryClass).newInstance();
        } catch (Exception e3) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Unable to create LogFactory instance.");
            }
            if (logFactoryClass != null) {
                if (class$org$apache$commons$logging$LogFactory == null) {
                    cls = class$(FACTORY_PROPERTY);
                    class$org$apache$commons$logging$LogFactory = cls;
                } else {
                    cls = class$org$apache$commons$logging$LogFactory;
                }
                if (!cls.isAssignableFrom(logFactoryClass)) {
                    return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e3);
                }
            }
            return new LogConfigurationException(e3);
        }
    }

    private static boolean implementsLogFactory(Class logFactoryClass) {
        boolean implementsLogFactory = false;
        if (logFactoryClass != null) {
            try {
                ClassLoader logFactoryClassLoader = logFactoryClass.getClassLoader();
                if (logFactoryClassLoader == null) {
                    logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
                } else {
                    logHierarchy("[CUSTOM LOG FACTORY] ", logFactoryClassLoader);
                    Class factoryFromCustomLoader = Class.forName(FACTORY_PROPERTY, false, logFactoryClassLoader);
                    implementsLogFactory = factoryFromCustomLoader.isAssignableFrom(logFactoryClass);
                    if (implementsLogFactory) {
                        logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] ").append(logFactoryClass.getName()).append(" implements LogFactory but was loaded by an incompatible classloader.").toString());
                    } else {
                        logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] ").append(logFactoryClass.getName()).append(" does not implement LogFactory.").toString());
                    }
                }
            } catch (ClassNotFoundException e) {
                logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
            } catch (LinkageError e2) {
                logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e2.getMessage()).toString());
            } catch (SecurityException e3) {
                logDiagnostic(new StringBuffer().append("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ").append(e3.getMessage()).toString());
            }
        }
        return implementsLogFactory;
    }

    private static InputStream getResourceAsStream(ClassLoader loader, String name) {
        return (InputStream) AccessController.doPrivileged(new PrivilegedAction(loader, name) { // from class: org.apache.commons.logging.LogFactory.3
            private final ClassLoader val$loader;
            private final String val$name;

            {
                this.val$loader = loader;
                this.val$name = name;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                if (this.val$loader != null) {
                    return this.val$loader.getResourceAsStream(this.val$name);
                }
                return ClassLoader.getSystemResourceAsStream(this.val$name);
            }
        });
    }

    private static Enumeration getResources(ClassLoader loader, String name) {
        PrivilegedAction action = new PrivilegedAction(loader, name) { // from class: org.apache.commons.logging.LogFactory.4
            private final ClassLoader val$loader;
            private final String val$name;

            {
                this.val$loader = loader;
                this.val$name = name;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                try {
                    if (this.val$loader != null) {
                        return this.val$loader.getResources(this.val$name);
                    }
                    return ClassLoader.getSystemResources(this.val$name);
                } catch (IOException e) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic(new StringBuffer().append("Exception while trying to find configuration file ").append(this.val$name).append(":").append(e.getMessage()).toString());
                        return null;
                    }
                    return null;
                } catch (NoSuchMethodError e2) {
                    return null;
                }
            }
        };
        Object result = AccessController.doPrivileged(action);
        return (Enumeration) result;
    }

    private static Properties getProperties(URL url) {
        PrivilegedAction action = new PrivilegedAction(url) { // from class: org.apache.commons.logging.LogFactory.5
            private final URL val$url;

            {
                this.val$url = url;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                try {
                    InputStream stream = this.val$url.openStream();
                    if (stream != null) {
                        Properties props = new Properties();
                        props.load(stream);
                        stream.close();
                        return props;
                    }
                    return null;
                } catch (IOException e) {
                    if (LogFactory.isDiagnosticsEnabled()) {
                        LogFactory.logDiagnostic(new StringBuffer().append("Unable to read URL ").append(this.val$url).toString());
                        return null;
                    }
                    return null;
                }
            }
        };
        return (Properties) AccessController.doPrivileged(action);
    }

    private static final Properties getConfigurationFile(ClassLoader classLoader, String fileName) {
        Enumeration urls;
        Properties props = null;
        double priority = 0.0d;
        URL propsUrl = null;
        try {
            urls = getResources(classLoader, fileName);
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("SecurityException thrown while trying to find/read config files.");
            }
        }
        if (urls == null) {
            return null;
        }
        while (urls.hasMoreElements()) {
            URL url = (URL) urls.nextElement();
            Properties newProps = getProperties(url);
            if (newProps != null) {
                if (props == null) {
                    propsUrl = url;
                    props = newProps;
                    String priorityStr = props.getProperty("priority");
                    priority = 0.0d;
                    if (priorityStr != null) {
                        priority = Double.parseDouble(priorityStr);
                    }
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file found at '").append(url).append("'").append(" with priority ").append(priority).toString());
                    }
                } else {
                    String newPriorityStr = newProps.getProperty("priority");
                    double newPriority = 0.0d;
                    if (newPriorityStr != null) {
                        newPriority = Double.parseDouble(newPriorityStr);
                    }
                    if (newPriority > priority) {
                        if (isDiagnosticsEnabled()) {
                            logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file at '").append(url).append("'").append(" with priority ").append(newPriority).append(" overrides file at '").append(propsUrl).append("'").append(" with priority ").append(priority).toString());
                        }
                        propsUrl = url;
                        props = newProps;
                        priority = newPriority;
                    } else if (isDiagnosticsEnabled()) {
                        logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file at '").append(url).append("'").append(" with priority ").append(newPriority).append(" does not override file at '").append(propsUrl).append("'").append(" with priority ").append(priority).toString());
                    }
                }
            }
        }
        if (isDiagnosticsEnabled()) {
            if (props == null) {
                logDiagnostic(new StringBuffer().append("[LOOKUP] No properties file of name '").append(fileName).append("' found.").toString());
            } else {
                logDiagnostic(new StringBuffer().append("[LOOKUP] Properties file of name '").append(fileName).append("' found at '").append(propsUrl).append('\"').toString());
            }
        }
        return props;
    }

    private static String getSystemProperty(String key, String def) throws SecurityException {
        return (String) AccessController.doPrivileged(new PrivilegedAction(key, def) { // from class: org.apache.commons.logging.LogFactory.6
            private final String val$key;
            private final String val$def;

            {
                this.val$key = key;
                this.val$def = def;
            }

            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(this.val$key, this.val$def);
            }
        });
    }

    private static void initDiagnostics() {
        String classLoaderName;
        try {
            String dest = getSystemProperty(DIAGNOSTICS_DEST_PROPERTY, null);
            if (dest == null) {
                return;
            }
            if (dest.equals("STDOUT")) {
                diagnosticsStream = System.out;
            } else if (dest.equals("STDERR")) {
                diagnosticsStream = System.err;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(dest, true);
                    diagnosticsStream = new PrintStream(fos);
                } catch (IOException e) {
                    return;
                }
            }
            try {
                ClassLoader classLoader = thisClassLoader;
                if (thisClassLoader == null) {
                    classLoaderName = "BOOTLOADER";
                } else {
                    classLoaderName = objectId(classLoader);
                }
            } catch (SecurityException e2) {
                classLoaderName = "UNKNOWN";
            }
            diagnosticPrefix = new StringBuffer().append("[LogFactory from ").append(classLoaderName).append("] ").toString();
        } catch (SecurityException e3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isDiagnosticsEnabled() {
        return diagnosticsStream != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void logDiagnostic(String msg) {
        if (diagnosticsStream != null) {
            diagnosticsStream.print(diagnosticPrefix);
            diagnosticsStream.println(msg);
            diagnosticsStream.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final void logRawDiagnostic(String msg) {
        if (diagnosticsStream != null) {
            diagnosticsStream.println(msg);
            diagnosticsStream.flush();
        }
    }

    private static void logClassLoaderEnvironment(Class clazz) {
        if (!isDiagnosticsEnabled()) {
            return;
        }
        try {
            logDiagnostic(new StringBuffer().append("[ENV] Extension directories (java.ext.dir): ").append(System.getProperty("java.ext.dir")).toString());
            logDiagnostic(new StringBuffer().append("[ENV] Application classpath (java.class.path): ").append(System.getProperty("java.class.path")).toString());
        } catch (SecurityException e) {
            logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
        }
        String className = clazz.getName();
        try {
            ClassLoader classLoader = getClassLoader(clazz);
            logDiagnostic(new StringBuffer().append("[ENV] Class ").append(className).append(" was loaded via classloader ").append(objectId(classLoader)).toString());
            logHierarchy(new StringBuffer().append("[ENV] Ancestry of classloader which loaded ").append(className).append(" is ").toString(), classLoader);
        } catch (SecurityException e2) {
            logDiagnostic(new StringBuffer().append("[ENV] Security forbids determining the classloader for ").append(className).toString());
        }
    }

    private static void logHierarchy(String prefix, ClassLoader classLoader) {
        if (!isDiagnosticsEnabled()) {
            return;
        }
        if (classLoader != null) {
            String classLoaderString = classLoader.toString();
            logDiagnostic(new StringBuffer().append(prefix).append(objectId(classLoader)).append(" == '").append(classLoaderString).append("'").toString());
        }
        try {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            if (classLoader != null) {
                StringBuffer buf = new StringBuffer(new StringBuffer().append(prefix).append("ClassLoader tree:").toString());
                do {
                    buf.append(objectId(classLoader));
                    if (classLoader == systemClassLoader) {
                        buf.append(" (SYSTEM) ");
                    }
                    try {
                        classLoader = classLoader.getParent();
                        buf.append(" --> ");
                    } catch (SecurityException e) {
                        buf.append(" --> SECRET");
                    }
                } while (classLoader != null);
                buf.append("BOOT");
                logDiagnostic(buf.toString());
            }
        } catch (SecurityException e2) {
            logDiagnostic(new StringBuffer().append(prefix).append("Security forbids determining the system classloader.").toString());
        }
    }

    public static String objectId(Object o) {
        if (o == null) {
            return Jimple.NULL;
        }
        return new StringBuffer().append(o.getClass().getName()).append("@").append(System.identityHashCode(o)).toString();
    }
}
