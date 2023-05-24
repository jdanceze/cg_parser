package gnu.trove.queue;

import gnu.trove.TByteCollection;
/* loaded from: gencallgraphv3.jar:trove-3.0.3.jar:gnu/trove/queue/TByteQueue.class */
public interface TByteQueue extends TByteCollection {
    byte element();

    boolean offer(byte b);

    byte peek();

    byte poll();
}
