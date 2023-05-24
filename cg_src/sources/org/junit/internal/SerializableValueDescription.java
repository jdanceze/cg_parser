package org.junit.internal;

import java.io.Serializable;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:org/junit/internal/SerializableValueDescription.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:org/junit/internal/SerializableValueDescription.class */
class SerializableValueDescription implements Serializable {
    private final String value;

    private SerializableValueDescription(Object value) {
        this.value = String.valueOf(value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object asSerializableValue(Object value) {
        if (value == null || (value instanceof Serializable)) {
            return value;
        }
        return new SerializableValueDescription(value);
    }

    public String toString() {
        return this.value;
    }
}
