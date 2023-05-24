package android.util;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/util/Property.class */
public abstract class Property<T, V> {
    public abstract V get(T t);

    public Property(Class<V> type, String name) {
        throw new RuntimeException("Stub!");
    }

    public static <T, V> Property<T, V> of(Class<T> hostType, Class<V> valueType, String name) {
        throw new RuntimeException("Stub!");
    }

    public boolean isReadOnly() {
        throw new RuntimeException("Stub!");
    }

    public void set(T object, V value) {
        throw new RuntimeException("Stub!");
    }

    public String getName() {
        throw new RuntimeException("Stub!");
    }

    public Class<V> getType() {
        throw new RuntimeException("Stub!");
    }
}
