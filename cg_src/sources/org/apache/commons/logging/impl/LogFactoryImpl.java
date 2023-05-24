package org.apache.commons.logging.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;
/* loaded from: gencallgraphv3.jar:commons-logging-1.1.1.jar:org/apache/commons/logging/impl/LogFactoryImpl.class */
public class LogFactoryImpl extends LogFactory {
    public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
    protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
    public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.apache.commons.logging.Log.allowFlawedContext";
    public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedDiscovery";
    public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedHierarchy";
    private String diagnosticPrefix;
    private String logClassName;
    protected Class[] logConstructorSignature;
    protected Method logMethod;
    protected Class[] logMethodSignature;
    private boolean allowFlawedContext;
    private boolean allowFlawedDiscovery;
    private boolean allowFlawedHierarchy;
    static Class class$java$lang$String;
    static Class class$org$apache$commons$logging$LogFactory;
    static Class class$org$apache$commons$logging$impl$LogFactoryImpl;
    static Class class$org$apache$commons$logging$Log;
    private static final String PKG_IMPL = "org.apache.commons.logging.impl.";
    private static final int PKG_LEN = PKG_IMPL.length();
    private static final String LOGGING_IMPL_LOG4J_LOGGER = "org.apache.commons.logging.impl.Log4JLogger";
    private static final String LOGGING_IMPL_JDK14_LOGGER = "org.apache.commons.logging.impl.Jdk14Logger";
    private static final String LOGGING_IMPL_LUMBERJACK_LOGGER = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
    private static final String LOGGING_IMPL_SIMPLE_LOGGER = "org.apache.commons.logging.impl.SimpleLog";
    private static final String[] classesToDiscover = {LOGGING_IMPL_LOG4J_LOGGER, LOGGING_IMPL_JDK14_LOGGER, LOGGING_IMPL_LUMBERJACK_LOGGER, LOGGING_IMPL_SIMPLE_LOGGER};
    private boolean useTCCL = true;
    protected Hashtable attributes = new Hashtable();
    protected Hashtable instances = new Hashtable();
    protected Constructor logConstructor = null;

    public LogFactoryImpl() {
        Class cls;
        Class cls2;
        Class[] clsArr = new Class[1];
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        clsArr[0] = cls;
        this.logConstructorSignature = clsArr;
        this.logMethod = null;
        Class[] clsArr2 = new Class[1];
        if (class$org$apache$commons$logging$LogFactory == null) {
            cls2 = class$(LogFactory.FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = cls2;
        } else {
            cls2 = class$org$apache$commons$logging$LogFactory;
        }
        clsArr2[0] = cls2;
        this.logMethodSignature = clsArr2;
        initDiagnostics();
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Instance created.");
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    @Override // org.apache.commons.logging.LogFactory
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override // org.apache.commons.logging.LogFactory
    public String[] getAttributeNames() {
        Vector names = new Vector();
        Enumeration keys = this.attributes.keys();
        while (keys.hasMoreElements()) {
            names.addElement((String) keys.nextElement());
        }
        String[] results = new String[names.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = (String) names.elementAt(i);
        }
        return results;
    }

    @Override // org.apache.commons.logging.LogFactory
    public Log getInstance(Class clazz) throws LogConfigurationException {
        return getInstance(clazz.getName());
    }

    @Override // org.apache.commons.logging.LogFactory
    public Log getInstance(String name) throws LogConfigurationException {
        Log instance = (Log) this.instances.get(name);
        if (instance == null) {
            instance = newInstance(name);
            this.instances.put(name, instance);
        }
        return instance;
    }

    @Override // org.apache.commons.logging.LogFactory
    public void release() {
        logDiagnostic("Releasing all known loggers");
        this.instances.clear();
    }

    @Override // org.apache.commons.logging.LogFactory
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override // org.apache.commons.logging.LogFactory
    public void setAttribute(String name, Object value) {
        if (this.logConstructor != null) {
            logDiagnostic("setAttribute: call too late; configuration already performed.");
        }
        if (value == null) {
            this.attributes.remove(name);
        } else {
            this.attributes.put(name, value);
        }
        if (name.equals(LogFactory.TCCL_KEY)) {
            this.useTCCL = Boolean.valueOf(value.toString()).booleanValue();
        }
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        return LogFactory.getContextClassLoader();
    }

    protected static boolean isDiagnosticsEnabled() {
        return LogFactory.isDiagnosticsEnabled();
    }

    protected static ClassLoader getClassLoader(Class clazz) {
        return LogFactory.getClassLoader(clazz);
    }

    private void initDiagnostics() {
        String classLoaderName;
        Class clazz = getClass();
        ClassLoader classLoader = getClassLoader(clazz);
        if (classLoader == null) {
            classLoaderName = "BOOTLOADER";
        } else {
            try {
                classLoaderName = LogFactory.objectId(classLoader);
            } catch (SecurityException e) {
                classLoaderName = "UNKNOWN";
            }
        }
        this.diagnosticPrefix = new StringBuffer().append("[LogFactoryImpl@").append(System.identityHashCode(this)).append(" from ").append(classLoaderName).append("] ").toString();
    }

    protected void logDiagnostic(String msg) {
        if (isDiagnosticsEnabled()) {
            LogFactory.logRawDiagnostic(new StringBuffer().append(this.diagnosticPrefix).append(msg).toString());
        }
    }

    protected String getLogClassName() {
        if (this.logClassName == null) {
            discoverLogImplementation(getClass().getName());
        }
        return this.logClassName;
    }

    protected Constructor getLogConstructor() throws LogConfigurationException {
        if (this.logConstructor == null) {
            discoverLogImplementation(getClass().getName());
        }
        return this.logConstructor;
    }

    protected boolean isJdk13LumberjackAvailable() {
        return isLogLibraryAvailable("Jdk13Lumberjack", LOGGING_IMPL_LUMBERJACK_LOGGER);
    }

    protected boolean isJdk14Available() {
        return isLogLibraryAvailable("Jdk14", LOGGING_IMPL_JDK14_LOGGER);
    }

    protected boolean isLog4JAvailable() {
        return isLogLibraryAvailable("Log4J", LOGGING_IMPL_LOG4J_LOGGER);
    }

    protected Log newInstance(String name) throws LogConfigurationException {
        Log instance;
        try {
            if (this.logConstructor == null) {
                instance = discoverLogImplementation(name);
            } else {
                Object[] params = {name};
                instance = (Log) this.logConstructor.newInstance(params);
            }
            if (this.logMethod != null) {
                Object[] params2 = {this};
                this.logMethod.invoke(instance, params2);
            }
            return instance;
        } catch (InvocationTargetException e) {
            Throwable c = e.getTargetException();
            if (c != null) {
                throw new LogConfigurationException(c);
            }
            throw new LogConfigurationException(e);
        } catch (LogConfigurationException lce) {
            throw lce;
        } catch (Throwable t) {
            throw new LogConfigurationException(t);
        }
    }

    private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.commons.logging.impl.LogFactoryImpl.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return LogFactory.directGetContextClassLoader();
            }
        });
    }

    private static String getSystemProperty(String key, String def) throws SecurityException {
        return (String) AccessController.doPrivileged(new PrivilegedAction(key, def) { // from class: org.apache.commons.logging.impl.LogFactoryImpl.2
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

    private ClassLoader getParentClassLoader(ClassLoader cl) {
        try {
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction(this, cl) { // from class: org.apache.commons.logging.impl.LogFactoryImpl.3
                private final ClassLoader val$cl;
                private final LogFactoryImpl this$0;

                {
                    this.this$0 = this;
                    this.val$cl = cl;
                }

                @Override // java.security.PrivilegedAction
                public Object run() {
                    return this.val$cl.getParent();
                }
            });
        } catch (SecurityException e) {
            logDiagnostic("[SECURITY] Unable to obtain parent classloader");
            return null;
        }
    }

    private boolean isLogLibraryAvailable(String name, String classname) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Checking for '").append(name).append("'.").toString());
        }
        try {
            Log log = createLogFromClass(classname, getClass().getName(), false);
            if (log == null) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("Did not find '").append(name).append("'.").toString());
                    return false;
                }
                return false;
            } else if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Found '").append(name).append("'.").toString());
                return true;
            } else {
                return true;
            }
        } catch (LogConfigurationException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Logging system '").append(name).append("' is available but not useable.").toString());
                return false;
            }
            return false;
        }
    }

    private String getConfigurationValue(String property) {
        String value;
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] Trying to get configuration for item ").append(property).toString());
        }
        Object valueObj = getAttribute(property);
        if (valueObj != null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[ENV] Found LogFactory attribute [").append(valueObj).append("] for ").append(property).toString());
            }
            return valueObj.toString();
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] No LogFactory attribute found for ").append(property).toString());
        }
        try {
            value = getSystemProperty(property, null);
        } catch (SecurityException e) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[ENV] Security prevented reading system property ").append(property).toString());
            }
        }
        if (value != null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("[ENV] Found system property [").append(value).append("] for ").append(property).toString());
            }
            return value;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] No system property found for property ").append(property).toString());
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("[ENV] No configuration defined for item ").append(property).toString());
            return null;
        }
        return null;
    }

    private boolean getBooleanConfiguration(String key, boolean dflt) {
        String val = getConfigurationValue(key);
        if (val == null) {
            return dflt;
        }
        return Boolean.valueOf(val).booleanValue();
    }

    private void initConfiguration() {
        this.allowFlawedContext = getBooleanConfiguration(ALLOW_FLAWED_CONTEXT_PROPERTY, true);
        this.allowFlawedDiscovery = getBooleanConfiguration(ALLOW_FLAWED_DISCOVERY_PROPERTY, true);
        this.allowFlawedHierarchy = getBooleanConfiguration(ALLOW_FLAWED_HIERARCHY_PROPERTY, true);
    }

    private Log discoverLogImplementation(String logCategory) throws LogConfigurationException {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Discovering a Log implementation...");
        }
        initConfiguration();
        Log result = null;
        String specifiedLogClassName = findUserSpecifiedLogClassName();
        if (specifiedLogClassName != null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic(new StringBuffer().append("Attempting to load user-specified log class '").append(specifiedLogClassName).append("'...").toString());
            }
            Log result2 = createLogFromClass(specifiedLogClassName, logCategory, true);
            if (result2 == null) {
                StringBuffer messageBuffer = new StringBuffer("User-specified log class '");
                messageBuffer.append(specifiedLogClassName);
                messageBuffer.append("' cannot be found or is not useable.");
                if (specifiedLogClassName != null) {
                    informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_LOG4J_LOGGER);
                    informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_JDK14_LOGGER);
                    informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_LUMBERJACK_LOGGER);
                    informUponSimilarName(messageBuffer, specifiedLogClassName, LOGGING_IMPL_SIMPLE_LOGGER);
                }
                throw new LogConfigurationException(messageBuffer.toString());
            }
            return result2;
        }
        if (isDiagnosticsEnabled()) {
            logDiagnostic("No user-specified Log implementation; performing discovery using the standard supported logging implementations...");
        }
        for (int i = 0; i < classesToDiscover.length && result == null; i++) {
            result = createLogFromClass(classesToDiscover[i], logCategory, true);
        }
        if (result == null) {
            throw new LogConfigurationException("No suitable Log implementation");
        }
        return result;
    }

    private void informUponSimilarName(StringBuffer messageBuffer, String name, String candidate) {
        if (!name.equals(candidate) && name.regionMatches(true, 0, candidate, 0, PKG_LEN + 5)) {
            messageBuffer.append(" Did you mean '");
            messageBuffer.append(candidate);
            messageBuffer.append("'?");
        }
    }

    private String findUserSpecifiedLogClassName() {
        if (isDiagnosticsEnabled()) {
            logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.Log'");
        }
        String specifiedClass = (String) getAttribute(LOG_PROPERTY);
        if (specifiedClass == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.log'");
            }
            specifiedClass = (String) getAttribute(LOG_PROPERTY_OLD);
        }
        if (specifiedClass == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.Log'");
            }
            try {
                specifiedClass = getSystemProperty(LOG_PROPERTY, null);
            } catch (SecurityException e) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("No access allowed to system property 'org.apache.commons.logging.Log' - ").append(e.getMessage()).toString());
                }
            }
        }
        if (specifiedClass == null) {
            if (isDiagnosticsEnabled()) {
                logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.log'");
            }
            try {
                specifiedClass = getSystemProperty(LOG_PROPERTY_OLD, null);
            } catch (SecurityException e2) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(new StringBuffer().append("No access allowed to system property 'org.apache.commons.logging.log' - ").append(e2.getMessage()).toString());
                }
            }
        }
        if (specifiedClass != null) {
            specifiedClass = specifiedClass.trim();
        }
        return specifiedClass;
    }

    private Log createLogFromClass(String logAdapterClassName, String logCategory, boolean affectState) throws LogConfigurationException {
        ClassLoader currentCL;
        Class c;
        Object o;
        URL url;
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Attempting to instantiate '").append(logAdapterClassName).append("'").toString());
        }
        Object[] params = {logCategory};
        Log logAdapter = null;
        Constructor constructor = null;
        Class logAdapterClass = null;
        ClassLoader baseClassLoader = getBaseClassLoader();
        while (true) {
            currentCL = baseClassLoader;
            logDiagnostic(new StringBuffer().append("Trying to load '").append(logAdapterClassName).append("' from classloader ").append(LogFactory.objectId(currentCL)).toString());
            try {
                if (isDiagnosticsEnabled()) {
                    String resourceName = new StringBuffer().append(logAdapterClassName.replace('.', '/')).append(".class").toString();
                    if (currentCL != null) {
                        url = currentCL.getResource(resourceName);
                    } else {
                        url = ClassLoader.getSystemResource(new StringBuffer().append(resourceName).append(".class").toString());
                    }
                    if (url == null) {
                        logDiagnostic(new StringBuffer().append("Class '").append(logAdapterClassName).append("' [").append(resourceName).append("] cannot be found.").toString());
                    } else {
                        logDiagnostic(new StringBuffer().append("Class '").append(logAdapterClassName).append("' was found at '").append(url).append("'").toString());
                    }
                }
                try {
                    c = Class.forName(logAdapterClassName, true, currentCL);
                } catch (ClassNotFoundException originalClassNotFoundException) {
                    String msg = new StringBuffer().append("").append(originalClassNotFoundException.getMessage()).toString();
                    logDiagnostic(new StringBuffer().append("The log adapter '").append(logAdapterClassName).append("' is not available via classloader ").append(LogFactory.objectId(currentCL)).append(": ").append(msg.trim()).toString());
                    try {
                        c = Class.forName(logAdapterClassName);
                    } catch (ClassNotFoundException secondaryClassNotFoundException) {
                        String msg2 = new StringBuffer().append("").append(secondaryClassNotFoundException.getMessage()).toString();
                        logDiagnostic(new StringBuffer().append("The log adapter '").append(logAdapterClassName).append("' is not available via the LogFactoryImpl class classloader: ").append(msg2.trim()).toString());
                    }
                }
                constructor = c.getConstructor(this.logConstructorSignature);
                o = constructor.newInstance(params);
            } catch (ExceptionInInitializerError e) {
                String msg3 = new StringBuffer().append("").append(e.getMessage()).toString();
                logDiagnostic(new StringBuffer().append("The log adapter '").append(logAdapterClassName).append("' is unable to initialize itself when loaded via classloader ").append(LogFactory.objectId(currentCL)).append(": ").append(msg3.trim()).toString());
            } catch (NoClassDefFoundError e2) {
                String msg4 = new StringBuffer().append("").append(e2.getMessage()).toString();
                logDiagnostic(new StringBuffer().append("The log adapter '").append(logAdapterClassName).append("' is missing dependencies when loaded via classloader ").append(LogFactory.objectId(currentCL)).append(": ").append(msg4.trim()).toString());
            } catch (LogConfigurationException e3) {
                throw e3;
            } catch (Throwable t) {
                handleFlawedDiscovery(logAdapterClassName, currentCL, t);
            }
            if (o instanceof Log) {
                logAdapterClass = c;
                logAdapter = (Log) o;
                break;
            }
            handleFlawedHierarchy(currentCL, c);
            if (currentCL == null) {
                break;
            }
            baseClassLoader = getParentClassLoader(currentCL);
        }
        if (logAdapter != null && affectState) {
            this.logClassName = logAdapterClassName;
            this.logConstructor = constructor;
            try {
                this.logMethod = logAdapterClass.getMethod("setLogFactory", this.logMethodSignature);
                logDiagnostic(new StringBuffer().append("Found method setLogFactory(LogFactory) in '").append(logAdapterClassName).append("'").toString());
            } catch (Throwable th) {
                this.logMethod = null;
                logDiagnostic(new StringBuffer().append("[INFO] '").append(logAdapterClassName).append("' from classloader ").append(LogFactory.objectId(currentCL)).append(" does not declare optional method ").append("setLogFactory(LogFactory)").toString());
            }
            logDiagnostic(new StringBuffer().append("Log adapter '").append(logAdapterClassName).append("' from classloader ").append(LogFactory.objectId(logAdapterClass.getClassLoader())).append(" has been selected for use.").toString());
        }
        return logAdapter;
    }

    private ClassLoader getBaseClassLoader() throws LogConfigurationException {
        Class cls;
        if (class$org$apache$commons$logging$impl$LogFactoryImpl == null) {
            cls = class$(LogFactory.FACTORY_DEFAULT);
            class$org$apache$commons$logging$impl$LogFactoryImpl = cls;
        } else {
            cls = class$org$apache$commons$logging$impl$LogFactoryImpl;
        }
        ClassLoader thisClassLoader = getClassLoader(cls);
        if (!this.useTCCL) {
            return thisClassLoader;
        }
        ClassLoader contextClassLoader = getContextClassLoaderInternal();
        ClassLoader baseClassLoader = getLowestClassLoader(contextClassLoader, thisClassLoader);
        if (baseClassLoader == null) {
            if (this.allowFlawedContext) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("[WARNING] the context classloader is not part of a parent-child relationship with the classloader that loaded LogFactoryImpl.");
                }
                return contextClassLoader;
            }
            throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
        }
        if (baseClassLoader != contextClassLoader) {
            if (this.allowFlawedContext) {
                if (isDiagnosticsEnabled()) {
                    logDiagnostic("Warning: the context classloader is an ancestor of the classloader that loaded LogFactoryImpl; it should be the same or a descendant. The application using commons-logging should ensure the context classloader is used correctly.");
                }
            } else {
                throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
            }
        }
        return baseClassLoader;
    }

    private ClassLoader getLowestClassLoader(ClassLoader c1, ClassLoader c2) {
        if (c1 == null) {
            return c2;
        }
        if (c2 == null) {
            return c1;
        }
        ClassLoader classLoader = c1;
        while (true) {
            ClassLoader current = classLoader;
            if (current != null) {
                if (current == c2) {
                    return c1;
                }
                classLoader = current.getParent();
            } else {
                ClassLoader classLoader2 = c2;
                while (true) {
                    ClassLoader current2 = classLoader2;
                    if (current2 != null) {
                        if (current2 == c1) {
                            return c2;
                        }
                        classLoader2 = current2.getParent();
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private void handleFlawedDiscovery(String logAdapterClassName, ClassLoader classLoader, Throwable discoveryFlaw) {
        if (isDiagnosticsEnabled()) {
            logDiagnostic(new StringBuffer().append("Could not instantiate Log '").append(logAdapterClassName).append("' -- ").append(discoveryFlaw.getClass().getName()).append(": ").append(discoveryFlaw.getLocalizedMessage()).toString());
            if (discoveryFlaw instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) discoveryFlaw;
                Throwable cause = ite.getTargetException();
                if (cause != null) {
                    logDiagnostic(new StringBuffer().append("... InvocationTargetException: ").append(cause.getClass().getName()).append(": ").append(cause.getLocalizedMessage()).toString());
                    if (cause instanceof ExceptionInInitializerError) {
                        ExceptionInInitializerError eiie = (ExceptionInInitializerError) cause;
                        Throwable cause2 = eiie.getException();
                        if (cause2 != null) {
                            logDiagnostic(new StringBuffer().append("... ExceptionInInitializerError: ").append(cause2.getClass().getName()).append(": ").append(cause2.getLocalizedMessage()).toString());
                        }
                    }
                }
            }
        }
        if (!this.allowFlawedDiscovery) {
            throw new LogConfigurationException(discoveryFlaw);
        }
    }

    private void handleFlawedHierarchy(ClassLoader badClassLoader, Class badClass) throws LogConfigurationException {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean implementsLog = false;
        if (class$org$apache$commons$logging$Log == null) {
            cls = class$(LOG_PROPERTY);
            class$org$apache$commons$logging$Log = cls;
        } else {
            cls = class$org$apache$commons$logging$Log;
        }
        String logInterfaceName = cls.getName();
        Class[] interfaces = badClass.getInterfaces();
        int i = 0;
        while (true) {
            if (i >= interfaces.length) {
                break;
            } else if (!logInterfaceName.equals(interfaces[i].getName())) {
                i++;
            } else {
                implementsLog = true;
                break;
            }
        }
        if (implementsLog) {
            if (isDiagnosticsEnabled()) {
                try {
                    if (class$org$apache$commons$logging$Log == null) {
                        cls2 = class$(LOG_PROPERTY);
                        class$org$apache$commons$logging$Log = cls2;
                    } else {
                        cls2 = class$org$apache$commons$logging$Log;
                    }
                    ClassLoader logInterfaceClassLoader = getClassLoader(cls2);
                    logDiagnostic(new StringBuffer().append("Class '").append(badClass.getName()).append("' was found in classloader ").append(LogFactory.objectId(badClassLoader)).append(". It is bound to a Log interface which is not").append(" the one loaded from classloader ").append(LogFactory.objectId(logInterfaceClassLoader)).toString());
                } catch (Throwable th) {
                    logDiagnostic(new StringBuffer().append("Error while trying to output diagnostics about bad class '").append(badClass).append("'").toString());
                }
            }
            if (!this.allowFlawedHierarchy) {
                StringBuffer msg = new StringBuffer();
                msg.append("Terminating logging for this context ");
                msg.append("due to bad log hierarchy. ");
                msg.append("You have more than one version of '");
                if (class$org$apache$commons$logging$Log == null) {
                    cls4 = class$(LOG_PROPERTY);
                    class$org$apache$commons$logging$Log = cls4;
                } else {
                    cls4 = class$org$apache$commons$logging$Log;
                }
                msg.append(cls4.getName());
                msg.append("' visible.");
                if (isDiagnosticsEnabled()) {
                    logDiagnostic(msg.toString());
                }
                throw new LogConfigurationException(msg.toString());
            } else if (isDiagnosticsEnabled()) {
                StringBuffer msg2 = new StringBuffer();
                msg2.append("Warning: bad log hierarchy. ");
                msg2.append("You have more than one version of '");
                if (class$org$apache$commons$logging$Log == null) {
                    cls3 = class$(LOG_PROPERTY);
                    class$org$apache$commons$logging$Log = cls3;
                } else {
                    cls3 = class$org$apache$commons$logging$Log;
                }
                msg2.append(cls3.getName());
                msg2.append("' visible.");
                logDiagnostic(msg2.toString());
            }
        } else if (!this.allowFlawedDiscovery) {
            StringBuffer msg3 = new StringBuffer();
            msg3.append("Terminating logging for this context. ");
            msg3.append("Log class '");
            msg3.append(badClass.getName());
            msg3.append("' does not implement the Log interface.");
            if (isDiagnosticsEnabled()) {
                logDiagnostic(msg3.toString());
            }
            throw new LogConfigurationException(msg3.toString());
        } else if (isDiagnosticsEnabled()) {
            StringBuffer msg4 = new StringBuffer();
            msg4.append("[WARNING] Log class '");
            msg4.append(badClass.getName());
            msg4.append("' does not implement the Log interface.");
            logDiagnostic(msg4.toString());
        }
    }
}
