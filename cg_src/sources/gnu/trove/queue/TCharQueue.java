package gnu.trove.queue;

import gnu.trove.TCharCollection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/queue/TCharQueue.class */
public interface TCharQueue extends TCharCollection {
    char element();

    boolean offer(char c);

    char peek();

    char poll();
}
