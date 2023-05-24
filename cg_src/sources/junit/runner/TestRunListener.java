package junit.runner;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/runner/TestRunListener.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/runner/TestRunListener.class */
public interface TestRunListener {
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_FAILURE = 2;

    void testRunStarted(String str, int i);

    void testRunEnded(long j);

    void testRunStopped(long j);

    void testStarted(String str);

    void testEnded(String str);

    void testFailed(int i, String str, String str2);
}
