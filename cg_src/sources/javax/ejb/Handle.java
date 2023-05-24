package javax.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/Handle.class */
public interface Handle extends Serializable {
    EJBObject getEJBObject() throws RemoteException;
}
