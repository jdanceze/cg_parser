package polyglot.util;

import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/SilentErrorQueue.class */
public class SilentErrorQueue extends AbstractErrorQueue {
    private List errors;

    public SilentErrorQueue(int limit, String name) {
        super(limit, name);
        this.errors = new ArrayList(limit);
    }

    @Override // polyglot.util.AbstractErrorQueue
    public void displayError(ErrorInfo e) {
        this.errors.add(e);
    }

    public List getErrors() {
        return this.errors;
    }
}
