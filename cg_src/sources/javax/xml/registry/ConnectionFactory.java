package javax.xml.registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Properties;
import org.apache.tools.ant.launch.Launcher;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/ConnectionFactory.class */
public abstract class ConnectionFactory {
    private static final String CONNECTION_FACTORY_CLASS = "javax.xml.registry.ConnectionFactoryClass";
    private static final String DEFAULT_CONNECTION_FACTORY = "com.sun.xml.registry.common.ConnectionFactoryImpl";

    public abstract void setProperties(Properties properties) throws JAXRException;

    public abstract Properties getProperties() throws JAXRException;

    public abstract Connection createConnection() throws JAXRException;

    public abstract FederatedConnection createFederatedConnection(Collection collection) throws JAXRException;

    public static ConnectionFactory newInstance() throws JAXRException {
        try {
            return (ConnectionFactory) find(CONNECTION_FACTORY_CLASS, DEFAULT_CONNECTION_FACTORY);
        } catch (Exception e) {
            throw new JAXRException(new StringBuffer().append("Unable to create ConnectionFactory: ").append(e.getMessage()).toString());
        }
    }

    private static Object newInstance(String str, ClassLoader classLoader) throws JAXRException {
        try {
            return (classLoader == null ? Class.forName(str) : classLoader.loadClass(str)).newInstance();
        } catch (ClassNotFoundException e) {
            throw new JAXRException(new StringBuffer().append("Provider ").append(str).append(" not found").toString(), e);
        } catch (Exception e2) {
            throw new JAXRException(new StringBuffer().append("Provider ").append(str).append(" could not be instantiated: ").append(e2).toString(), e2);
        }
    }

    static Object find(String str, String str2) throws JAXRException {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                String property = System.getProperty(str);
                if (property != null) {
                    return newInstance(property, contextClassLoader);
                }
            } catch (SecurityException e) {
            }
            try {
                File file = new File(new StringBuffer().append(System.getProperty("java.home")).append(File.separator).append(Launcher.ANT_PRIVATELIB).append(File.separator).append("jaxr.properties").toString());
                if (file.exists()) {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(file));
                    return newInstance(properties.getProperty(str), contextClassLoader);
                }
            } catch (Exception e2) {
            }
            String stringBuffer = new StringBuffer().append("META-INF/services/").append(str).toString();
            try {
                InputStream systemResourceAsStream = contextClassLoader == null ? ClassLoader.getSystemResourceAsStream(stringBuffer) : contextClassLoader.getResourceAsStream(stringBuffer);
                if (systemResourceAsStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(systemResourceAsStream, "UTF-8"));
                    String readLine = bufferedReader.readLine();
                    bufferedReader.close();
                    if (readLine != null && !"".equals(readLine)) {
                        return newInstance(readLine, contextClassLoader);
                    }
                }
            } catch (Exception e3) {
            }
            if (str2 == null) {
                throw new JAXRException(new StringBuffer().append("Provider for ").append(str).append(" cannot be found").toString(), null);
            }
            return newInstance(str2, contextClassLoader);
        } catch (Exception e4) {
            throw new JAXRException(e4.toString(), e4);
        }
    }
}
