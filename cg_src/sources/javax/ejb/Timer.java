package javax.ejb;

import java.io.Serializable;
import java.util.Date;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/Timer.class */
public interface Timer {
    void cancel() throws IllegalStateException, NoSuchObjectLocalException, EJBException;

    long getTimeRemaining() throws IllegalStateException, NoSuchObjectLocalException, EJBException;

    Date getNextTimeout() throws IllegalStateException, NoSuchObjectLocalException, EJBException;

    Serializable getInfo() throws IllegalStateException, NoSuchObjectLocalException, EJBException;

    TimerHandle getHandle() throws IllegalStateException, NoSuchObjectLocalException, EJBException;
}
