package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
@CanIgnoreReturnValue
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/PrimitiveSink.class */
public interface PrimitiveSink {
    PrimitiveSink putByte(byte b);

    PrimitiveSink putBytes(byte[] bArr);

    PrimitiveSink putBytes(byte[] bArr, int i, int i2);

    PrimitiveSink putBytes(ByteBuffer byteBuffer);

    PrimitiveSink putShort(short s);

    PrimitiveSink putInt(int i);

    PrimitiveSink putLong(long j);

    PrimitiveSink putFloat(float f);

    PrimitiveSink putDouble(double d);

    PrimitiveSink putBoolean(boolean z);

    PrimitiveSink putChar(char c);

    PrimitiveSink putUnencodedChars(CharSequence charSequence);

    PrimitiveSink putString(CharSequence charSequence, Charset charset);
}
