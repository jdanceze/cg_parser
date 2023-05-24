package gnu.trove.queue;

import gnu.trove.TIntCollection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/queue/TIntQueue.class */
public interface TIntQueue extends TIntCollection {
    int element();

    boolean offer(int i);

    int peek();

    int poll();
}
