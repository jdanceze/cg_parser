package gnu.trove.queue;

import gnu.trove.TFloatCollection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/queue/TFloatQueue.class */
public interface TFloatQueue extends TFloatCollection {
    float element();

    boolean offer(float f);

    float peek();

    float poll();
}
