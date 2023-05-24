package javax.ejb;

import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/SessionBean.class */
public interface SessionBean extends EnterpriseBean {
    void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException;

    void ejbRemove() throws EJBException, RemoteException;

    void ejbActivate() throws EJBException, RemoteException;

    void ejbPassivate() throws EJBException, RemoteException;
}
