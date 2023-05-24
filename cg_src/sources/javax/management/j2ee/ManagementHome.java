package javax.management.j2ee;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/management/j2ee/ManagementHome.class */
public interface ManagementHome extends EJBHome {
    Management create() throws CreateException, RemoteException;
}
