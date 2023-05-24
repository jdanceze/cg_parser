package javax.xml.registry;

import java.util.Set;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/Connection.class */
public interface Connection {
    RegistryService getRegistryService() throws JAXRException;

    void close() throws JAXRException;

    boolean isClosed() throws JAXRException;

    boolean isSynchronous() throws JAXRException;

    void setSynchronous(boolean z) throws JAXRException;

    void setCredentials(Set set) throws JAXRException;

    Set getCredentials() throws JAXRException;
}
