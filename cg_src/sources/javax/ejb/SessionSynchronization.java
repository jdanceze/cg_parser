package javax.ejb;

import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/SessionSynchronization.class */
public interface SessionSynchronization {
    void afterBegin() throws EJBException, RemoteException;

    void beforeCompletion() throws EJBException, RemoteException;

    void afterCompletion(boolean z) throws EJBException, RemoteException;
}
