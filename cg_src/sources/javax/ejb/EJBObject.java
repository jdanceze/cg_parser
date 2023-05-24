package javax.ejb;

import java.rmi.Remote;
import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EJBObject.class */
public interface EJBObject extends Remote {
    EJBHome getEJBHome() throws RemoteException;

    Object getPrimaryKey() throws RemoteException;

    void remove() throws RemoteException, RemoveException;

    Handle getHandle() throws RemoteException;

    boolean isIdentical(EJBObject eJBObject) throws RemoteException;
}
