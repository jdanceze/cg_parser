package org.jf.dexlib2;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import javax.annotation.Nonnull;
import org.jf.util.ExceptionWithContext;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/MethodHandleType.class */
public class MethodHandleType {
    public static final int STATIC_PUT = 0;
    public static final int STATIC_GET = 1;
    public static final int INSTANCE_PUT = 2;
    public static final int INSTANCE_GET = 3;
    public static final int INVOKE_STATIC = 4;
    public static final int INVOKE_INSTANCE = 5;
    public static final int INVOKE_CONSTRUCTOR = 6;
    public static final int INVOKE_DIRECT = 7;
    public static final int INVOKE_INTERFACE = 8;
    private static final BiMap<Integer, String> methodHandleTypeNames = new ImmutableBiMap.Builder().put((ImmutableBiMap.Builder) 0, (int) "static-put").put((ImmutableBiMap.Builder) 1, (int) "static-get").put((ImmutableBiMap.Builder) 2, (int) "instance-put").put((ImmutableBiMap.Builder) 3, (int) "instance-get").put((ImmutableBiMap.Builder) 4, (int) "invoke-static").put((ImmutableBiMap.Builder) 5, (int) "invoke-instance").put((ImmutableBiMap.Builder) 6, (int) "invoke-constructor").put((ImmutableBiMap.Builder) 7, (int) "invoke-direct").put((ImmutableBiMap.Builder) 8, (int) "invoke-interface").build();

    @Nonnull
    public static String toString(int methodHandleType) {
        String val = methodHandleTypeNames.get(Integer.valueOf(methodHandleType));
        if (val == null) {
            throw new InvalidMethodHandleTypeException(methodHandleType);
        }
        return val;
    }

    public static int getMethodHandleType(String methodHandleType) {
        Integer ret = methodHandleTypeNames.inverse().get(methodHandleType);
        if (ret == null) {
            throw new ExceptionWithContext("Invalid method handle type: %s", methodHandleType);
        }
        return ret.intValue();
    }

    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/MethodHandleType$InvalidMethodHandleTypeException.class */
    public static class InvalidMethodHandleTypeException extends ExceptionWithContext {
        private final int methodHandleType;

        public InvalidMethodHandleTypeException(int methodHandleType) {
            super("Invalid method handle type: %d", Integer.valueOf(methodHandleType));
            this.methodHandleType = methodHandleType;
        }

        public InvalidMethodHandleTypeException(int methodHandleType, String message, Object... formatArgs) {
            super(message, formatArgs);
            this.methodHandleType = methodHandleType;
        }

        public int getMethodHandleType() {
            return this.methodHandleType;
        }
    }
}
