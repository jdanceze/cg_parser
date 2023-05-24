package javax.xml.rpc;

import java.net.URL;
import java.rmi.Remote;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.TypeMappingRegistry;
import javax.xml.rpc.handler.HandlerRegistry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/Service.class */
public interface Service {
    Remote getPort(QName qName, Class cls) throws ServiceException;

    Remote getPort(Class cls) throws ServiceException;

    Call[] getCalls(QName qName) throws ServiceException;

    Call createCall(QName qName) throws ServiceException;

    Call createCall(QName qName, QName qName2) throws ServiceException;

    Call createCall(QName qName, String str) throws ServiceException;

    Call createCall() throws ServiceException;

    QName getServiceName();

    Iterator getPorts() throws ServiceException;

    URL getWSDLDocumentLocation();

    TypeMappingRegistry getTypeMappingRegistry();

    HandlerRegistry getHandlerRegistry();
}
