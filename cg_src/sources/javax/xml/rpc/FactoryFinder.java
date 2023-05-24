package javax.xml.rpc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.tools.ant.launch.Launcher;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/FactoryFinder.class */
class FactoryFinder {
    FactoryFinder() {
    }

    private static Object newInstance(String className, ClassLoader classLoader) throws ServiceException {
        Class spiClass;
        try {
            if (classLoader == null) {
                spiClass = Class.forName(className);
            } else {
                spiClass = classLoader.loadClass(className);
            }
            return spiClass.newInstance();
        } catch (ClassNotFoundException x) {
            throw new ServiceException(new StringBuffer().append("Provider ").append(className).append(" not found").toString(), x);
        } catch (Exception x2) {
            throw new ServiceException(new StringBuffer().append("Provider ").append(className).append(" could not be instantiated: ").append(x2).toString(), x2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object find(String factoryId, String fallbackClassName) throws ServiceException {
        InputStream is;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try {
                String systemProp = System.getProperty(factoryId);
                if (systemProp != null) {
                    return newInstance(systemProp, classLoader);
                }
            } catch (SecurityException e) {
            }
            try {
                String javah = System.getProperty("java.home");
                String configFile = new StringBuffer().append(javah).append(File.separator).append(Launcher.ANT_PRIVATELIB).append(File.separator).append("jaxrpc.properties").toString();
                File f = new File(configFile);
                if (f.exists()) {
                    Properties props = new Properties();
                    props.load(new FileInputStream(f));
                    return newInstance(props.getProperty(factoryId), classLoader);
                }
            } catch (Exception e2) {
            }
            String serviceId = new StringBuffer().append("META-INF/services/").append(factoryId).toString();
            try {
                if (classLoader == null) {
                    is = ClassLoader.getSystemResourceAsStream(serviceId);
                } else {
                    is = classLoader.getResourceAsStream(serviceId);
                }
                if (is != null) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String factoryClassName = rd.readLine();
                    rd.close();
                    if (factoryClassName != null && !"".equals(factoryClassName)) {
                        return newInstance(factoryClassName, classLoader);
                    }
                }
            } catch (Exception e3) {
            }
            if (fallbackClassName == null) {
                throw new ServiceException(new StringBuffer().append("Provider for ").append(factoryId).append(" cannot be found").toString(), null);
            }
            return newInstance(fallbackClassName, classLoader);
        } catch (Exception x) {
            throw new ServiceException(x.toString(), x);
        }
    }
}
