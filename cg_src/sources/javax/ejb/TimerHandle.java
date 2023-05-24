package javax.ejb;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/ejb/TimerHandle.class */
public interface TimerHandle extends Serializable {
    Timer getTimer() throws IllegalStateException, NoSuchObjectLocalException, EJBException;
}
