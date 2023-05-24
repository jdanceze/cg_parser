package javax.ejb;

import java.io.Serializable;
import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/HomeHandle.class */
public interface HomeHandle extends Serializable {
    EJBHome getEJBHome() throws RemoteException;
}
