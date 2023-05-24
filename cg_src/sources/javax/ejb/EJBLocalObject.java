package javax.ejb;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EJBLocalObject.class */
public interface EJBLocalObject {
    EJBLocalHome getEJBLocalHome() throws EJBException;

    Object getPrimaryKey() throws EJBException;

    void remove() throws RemoveException, EJBException;

    boolean isIdentical(EJBLocalObject eJBLocalObject) throws EJBException;
}
