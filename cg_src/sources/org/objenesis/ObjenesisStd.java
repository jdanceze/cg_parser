package org.objenesis;

import org.objenesis.strategy.StdInstantiatorStrategy;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/ObjenesisStd.class */
public class ObjenesisStd extends ObjenesisBase {
    public ObjenesisStd() {
        super(new StdInstantiatorStrategy());
    }

    public ObjenesisStd(boolean useCache) {
        super(new StdInstantiatorStrategy(), useCache);
    }
}
