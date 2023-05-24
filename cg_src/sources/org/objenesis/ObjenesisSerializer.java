package org.objenesis;

import org.objenesis.strategy.SerializingInstantiatorStrategy;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/ObjenesisSerializer.class */
public class ObjenesisSerializer extends ObjenesisBase {
    public ObjenesisSerializer() {
        super(new SerializingInstantiatorStrategy());
    }

    public ObjenesisSerializer(boolean useCache) {
        super(new SerializingInstantiatorStrategy(), useCache);
    }
}
