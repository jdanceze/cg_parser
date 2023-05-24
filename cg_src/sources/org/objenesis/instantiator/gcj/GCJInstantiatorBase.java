package org.objenesis.instantiator.gcj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/instantiator/gcj/GCJInstantiatorBase.class */
public abstract class GCJInstantiatorBase<T> implements ObjectInstantiator<T> {
    static Method newObjectMethod = null;
    static ObjectInputStream dummyStream;
    protected final Class<T> type;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/instantiator/gcj/GCJInstantiatorBase$DummyStream.class */
    public static class DummyStream extends ObjectInputStream {
    }

    @Override // org.objenesis.instantiator.ObjectInstantiator
    public abstract T newInstance();

    private static void initialize() {
        if (newObjectMethod == null) {
            try {
                newObjectMethod = ObjectInputStream.class.getDeclaredMethod("newObject", Class.class, Class.class);
                newObjectMethod.setAccessible(true);
                dummyStream = new DummyStream();
            } catch (IOException | NoSuchMethodException | RuntimeException e) {
                throw new ObjenesisException(e);
            }
        }
    }

    public GCJInstantiatorBase(Class<T> type) {
        this.type = type;
        initialize();
    }
}
