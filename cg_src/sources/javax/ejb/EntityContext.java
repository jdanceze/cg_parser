package javax.ejb;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EntityContext.class */
public interface EntityContext extends EJBContext {
    EJBLocalObject getEJBLocalObject() throws IllegalStateException;

    EJBObject getEJBObject() throws IllegalStateException;

    Object getPrimaryKey() throws IllegalStateException;
}
