package javax.ejb;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EJBMetaData.class */
public interface EJBMetaData {
    EJBHome getEJBHome();

    Class getHomeInterfaceClass();

    Class getRemoteInterfaceClass();

    Class getPrimaryKeyClass();

    boolean isSession();

    boolean isStatelessSession();
}
