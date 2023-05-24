package javax.xml.rpc;

import java.net.URL;
import java.util.Properties;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/ServiceFactory.class */
public abstract class ServiceFactory {
    public static final String SERVICEFACTORY_PROPERTY = "javax.xml.rpc.ServiceFactory";
    private static final String DEFAULT_SERVICEFACTORY = "com.sun.xml.rpc.client.ServiceFactoryImpl";

    public abstract Service createService(URL url, QName qName) throws ServiceException;

    public abstract Service createService(QName qName) throws ServiceException;

    public abstract Service loadService(Class cls) throws ServiceException;

    public abstract Service loadService(URL url, Class cls, Properties properties) throws ServiceException;

    public abstract Service loadService(URL url, QName qName, Properties properties) throws ServiceException;

    protected ServiceFactory() {
    }

    public static ServiceFactory newInstance() throws ServiceException {
        try {
            return (ServiceFactory) FactoryFinder.find(SERVICEFACTORY_PROPERTY, DEFAULT_SERVICEFACTORY);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new ServiceException(new StringBuffer().append("Unable to create Service Factory: ").append(ex2.getMessage()).toString());
        }
    }
}
