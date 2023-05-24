package gnu.trove.queue;

import gnu.trove.TShortCollection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/queue/TShortQueue.class */
public interface TShortQueue extends TShortCollection {
    short element();

    boolean offer(short s);

    short peek();

    short poll();
}
