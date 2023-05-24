package javax.ejb;

import java.rmi.Remote;
import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EJBHome.class */
public interface EJBHome extends Remote {
    void remove(Handle handle) throws RemoteException, RemoveException;

    void remove(Object obj) throws RemoteException, RemoveException;

    EJBMetaData getEJBMetaData() throws RemoteException;

    HomeHandle getHomeHandle() throws RemoteException;
}
