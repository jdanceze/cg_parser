package gnu.trove.queue;

import gnu.trove.TDoubleCollection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/queue/TDoubleQueue.class */
public interface TDoubleQueue extends TDoubleCollection {
    double element();

    boolean offer(double d);

    double peek();

    double poll();
}
