package javax.ejb;

import java.security.Identity;
import java.security.Principal;
import java.util.Properties;
import javax.transaction.UserTransaction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/EJBContext.class */
public interface EJBContext {
    EJBHome getEJBHome();

    EJBLocalHome getEJBLocalHome();

    Properties getEnvironment();

    Identity getCallerIdentity();

    Principal getCallerPrincipal();

    boolean isCallerInRole(Identity identity);

    boolean isCallerInRole(String str);

    UserTransaction getUserTransaction() throws IllegalStateException;

    void setRollbackOnly() throws IllegalStateException;

    boolean getRollbackOnly() throws IllegalStateException;

    TimerService getTimerService() throws IllegalStateException;
}
