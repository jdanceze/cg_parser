package javax.transaction;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/transaction/Synchronization.class */
public interface Synchronization {
    void beforeCompletion();

    void afterCompletion(int i);
}
