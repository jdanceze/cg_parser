package javax.ejb;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/TimerService.class */
public interface TimerService {
    Timer createTimer(long j, Serializable serializable) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createTimer(long j, long j2, Serializable serializable) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createTimer(Date date, Serializable serializable) throws IllegalArgumentException, IllegalStateException, EJBException;

    Timer createTimer(Date date, long j, Serializable serializable) throws IllegalArgumentException, IllegalStateException, EJBException;

    Collection getTimers() throws IllegalStateException, EJBException;
}
