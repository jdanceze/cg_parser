package javax.ejb;

import java.rmi.RemoteException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EntityBean.class */
public interface EntityBean extends EnterpriseBean {
    void setEntityContext(EntityContext entityContext) throws EJBException, RemoteException;

    void unsetEntityContext() throws EJBException, RemoteException;

    void ejbRemove() throws RemoveException, EJBException, RemoteException;

    void ejbActivate() throws EJBException, RemoteException;

    void ejbPassivate() throws EJBException, RemoteException;

    void ejbLoad() throws EJBException, RemoteException;

    void ejbStore() throws EJBException, RemoteException;
}
