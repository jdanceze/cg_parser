package javax.xml.rpc;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/Call.class */
public interface Call {
    public static final String USERNAME_PROPERTY = "javax.xml.rpc.security.auth.username";
    public static final String PASSWORD_PROPERTY = "javax.xml.rpc.security.auth.password";
    public static final String OPERATION_STYLE_PROPERTY = "javax.xml.rpc.soap.operation.style";
    public static final String SOAPACTION_USE_PROPERTY = "javax.xml.rpc.soap.http.soapaction.use";
    public static final String SOAPACTION_URI_PROPERTY = "javax.xml.rpc.soap.http.soapaction.uri";
    public static final String ENCODINGSTYLE_URI_PROPERTY = "javax.xml.rpc.encodingstyle.namespace.uri";
    public static final String SESSION_MAINTAIN_PROPERTY = "javax.xml.rpc.session.maintain";

    boolean isParameterAndReturnSpecRequired(QName qName);

    void addParameter(String str, QName qName, ParameterMode parameterMode);

    void addParameter(String str, QName qName, Class cls, ParameterMode parameterMode);

    QName getParameterTypeByName(String str);

    void setReturnType(QName qName);

    void setReturnType(QName qName, Class cls);

    QName getReturnType();

    void removeAllParameters();

    QName getOperationName();

    void setOperationName(QName qName);

    QName getPortTypeName();

    void setPortTypeName(QName qName);

    void setTargetEndpointAddress(String str);

    String getTargetEndpointAddress();

    void setProperty(String str, Object obj);

    Object getProperty(String str);

    void removeProperty(String str);

    Iterator getPropertyNames();

    Object invoke(Object[] objArr) throws RemoteException;

    Object invoke(QName qName, Object[] objArr) throws RemoteException;

    void invokeOneWay(Object[] objArr);

    Map getOutputParams();

    List getOutputValues();
}
