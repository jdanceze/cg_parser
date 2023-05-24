package polyglot.util;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/AbstractErrorQueue.class */
public abstract class AbstractErrorQueue implements ErrorQueue {
    protected final int limit;
    protected final String name;
    protected int errorCount = 0;
    protected boolean flushed = true;

    protected abstract void displayError(ErrorInfo errorInfo);

    public AbstractErrorQueue(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }

    @Override // polyglot.util.ErrorQueue
    public final void enqueue(int type, String message) {
        enqueue(type, message, null);
    }

    @Override // polyglot.util.ErrorQueue
    public final void enqueue(int type, String message, Position position) {
        enqueue(new ErrorInfo(type, message, position));
    }

    @Override // polyglot.util.ErrorQueue
    public final void enqueue(ErrorInfo e) {
        if (e.getErrorKind() != 0) {
            this.errorCount++;
        }
        this.flushed = false;
        displayError(e);
        if (this.errorCount >= this.limit) {
            tooManyErrors(e);
            flush();
            throw new ErrorLimitError();
        }
    }

    protected void tooManyErrors(ErrorInfo lastError) {
    }

    @Override // polyglot.util.ErrorQueue
    public void flush() {
        this.flushed = true;
    }

    @Override // polyglot.util.ErrorQueue
    public final boolean hasErrors() {
        return this.errorCount > 0;
    }

    @Override // polyglot.util.ErrorQueue
    public final int errorCount() {
        return this.errorCount;
    }
}
