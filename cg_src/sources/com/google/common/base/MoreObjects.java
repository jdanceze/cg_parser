package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/MoreObjects.class */
public final class MoreObjects {
    public static <T> T firstNonNull(@NullableDecl T first, @NullableDecl T second) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }
        throw new NullPointerException("Both parameters are null");
    }

    public static ToStringHelper toStringHelper(Object self) {
        return new ToStringHelper(self.getClass().getSimpleName());
    }

    public static ToStringHelper toStringHelper(Class<?> clazz) {
        return new ToStringHelper(clazz.getSimpleName());
    }

    public static ToStringHelper toStringHelper(String className) {
        return new ToStringHelper(className);
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/MoreObjects$ToStringHelper.class */
    public static final class ToStringHelper {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;

        private ToStringHelper(String className) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.className = (String) Preconditions.checkNotNull(className);
        }

        @CanIgnoreReturnValue
        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, @NullableDecl Object value) {
            return addHolder(name, value);
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, boolean value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, char value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, double value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, float value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, int value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper add(String name, long value) {
            return addHolder(name, String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(@NullableDecl Object value) {
            return addHolder(value);
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(boolean value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(char value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(double value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(float value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(int value) {
            return addHolder(String.valueOf(value));
        }

        @CanIgnoreReturnValue
        public ToStringHelper addValue(long value) {
            return addHolder(String.valueOf(value));
        }

        public String toString() {
            boolean omitNullValuesSnapshot = this.omitNullValues;
            String nextSeparator = "";
            StringBuilder builder = new StringBuilder(32).append(this.className).append('{');
            ValueHolder valueHolder = this.holderHead.next;
            while (true) {
                ValueHolder valueHolder2 = valueHolder;
                if (valueHolder2 != null) {
                    Object value = valueHolder2.value;
                    if (!omitNullValuesSnapshot || value != null) {
                        builder.append(nextSeparator);
                        nextSeparator = ", ";
                        if (valueHolder2.name != null) {
                            builder.append(valueHolder2.name).append('=');
                        }
                        if (value != null && value.getClass().isArray()) {
                            Object[] objectArray = {value};
                            String arrayString = Arrays.deepToString(objectArray);
                            builder.append((CharSequence) arrayString, 1, arrayString.length() - 1);
                        } else {
                            builder.append(value);
                        }
                    }
                    valueHolder = valueHolder2.next;
                } else {
                    return builder.append('}').toString();
                }
            }
        }

        private ValueHolder addHolder() {
            ValueHolder valueHolder = new ValueHolder();
            this.holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            return valueHolder;
        }

        private ToStringHelper addHolder(@NullableDecl Object value) {
            ValueHolder valueHolder = addHolder();
            valueHolder.value = value;
            return this;
        }

        private ToStringHelper addHolder(String name, @NullableDecl Object value) {
            ValueHolder valueHolder = addHolder();
            valueHolder.value = value;
            valueHolder.name = (String) Preconditions.checkNotNull(name);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/base/MoreObjects$ToStringHelper$ValueHolder.class */
        public static final class ValueHolder {
            @NullableDecl
            String name;
            @NullableDecl
            Object value;
            @NullableDecl
            ValueHolder next;

            private ValueHolder() {
            }
        }
    }

    private MoreObjects() {
    }
}
