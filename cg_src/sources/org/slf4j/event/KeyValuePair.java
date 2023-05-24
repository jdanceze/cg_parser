package org.slf4j.event;
/* loaded from: gencallgraphv3.jar:slf4j-api-2.0.3.jar:org/slf4j/event/KeyValuePair.class */
public class KeyValuePair {
    public final String key;
    public final Object value;

    public KeyValuePair(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return String.valueOf(this.key) + "=\"" + String.valueOf(this.value) + "\"";
    }
}
