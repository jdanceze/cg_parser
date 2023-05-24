package polyglot.util;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/ErrorQueue.class */
public interface ErrorQueue {
    void enqueue(int i, String str);

    void enqueue(int i, String str, Position position);

    void enqueue(ErrorInfo errorInfo);

    void flush();

    boolean hasErrors();

    int errorCount();
}
