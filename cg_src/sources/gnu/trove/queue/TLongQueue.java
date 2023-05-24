package gnu.trove.queue;

import gnu.trove.TLongCollection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/queue/TLongQueue.class */
public interface TLongQueue extends TLongCollection {
    long element();

    boolean offer(long j);

    long peek();

    long poll();
}
