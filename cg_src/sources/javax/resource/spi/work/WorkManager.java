package javax.resource.spi.work;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/resource/spi/work/WorkManager.class */
public interface WorkManager {
    public static final long IMMEDIATE = 0;
    public static final long INDEFINITE = Long.MAX_VALUE;
    public static final long UNKNOWN = -1;

    void doWork(Work work) throws WorkException;

    void doWork(Work work, long j, ExecutionContext executionContext, WorkListener workListener) throws WorkException;

    long startWork(Work work) throws WorkException;

    long startWork(Work work, long j, ExecutionContext executionContext, WorkListener workListener) throws WorkException;

    void scheduleWork(Work work) throws WorkException;

    void scheduleWork(Work work, long j, ExecutionContext executionContext, WorkListener workListener) throws WorkException;
}
