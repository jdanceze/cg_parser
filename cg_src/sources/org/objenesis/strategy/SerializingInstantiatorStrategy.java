package org.objenesis.strategy;

import java.io.NotSerializableException;
import java.io.Serializable;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.objenesis.ObjenesisException;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.instantiator.android.AndroidSerializationInstantiator;
import org.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
import org.objenesis.instantiator.basic.ObjectStreamClassInstantiator;
import org.objenesis.instantiator.gcj.GCJSerializationInstantiator;
import org.objenesis.instantiator.perc.PercSerializationInstantiator;
import org.objenesis.instantiator.sun.SunReflectionFactorySerializationInstantiator;
/* loaded from: gencallgraphv3.jar:objenesis-3.1.jar:org/objenesis/strategy/SerializingInstantiatorStrategy.class */
public class SerializingInstantiatorStrategy extends BaseInstantiatorStrategy {
    @Override // org.objenesis.strategy.InstantiatorStrategy
    public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
        if (!Serializable.class.isAssignableFrom(type)) {
            throw new ObjenesisException(new NotSerializableException(type + " not serializable"));
        }
        if (PlatformDescription.JVM_NAME.startsWith("Java HotSpot") || PlatformDescription.isThisJVM(PlatformDescription.OPENJDK)) {
            if (PlatformDescription.isGoogleAppEngine() && PlatformDescription.SPECIFICATION_VERSION.equals(JavaEnvUtils.JAVA_1_7)) {
                return new ObjectInputStreamInstantiator(type);
            }
            return new SunReflectionFactorySerializationInstantiator(type);
        } else if (PlatformDescription.JVM_NAME.startsWith(PlatformDescription.DALVIK)) {
            if (PlatformDescription.isAndroidOpenJDK()) {
                return new ObjectStreamClassInstantiator(type);
            }
            return new AndroidSerializationInstantiator(type);
        } else if (PlatformDescription.JVM_NAME.startsWith(PlatformDescription.GNU)) {
            return new GCJSerializationInstantiator(type);
        } else {
            if (PlatformDescription.JVM_NAME.startsWith(PlatformDescription.PERC)) {
                return new PercSerializationInstantiator(type);
            }
            return new SunReflectionFactorySerializationInstantiator(type);
        }
    }
}
