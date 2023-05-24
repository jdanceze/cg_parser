package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
@CanIgnoreReturnValue
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/Hasher.class */
public interface Hasher extends PrimitiveSink {
    @Override // com.google.common.hash.PrimitiveSink
    Hasher putByte(byte b);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBytes(byte[] bArr);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBytes(byte[] bArr, int i, int i2);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBytes(ByteBuffer byteBuffer);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putShort(short s);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putInt(int i);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putLong(long j);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putFloat(float f);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putDouble(double d);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putBoolean(boolean z);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putChar(char c);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putUnencodedChars(CharSequence charSequence);

    @Override // com.google.common.hash.PrimitiveSink
    Hasher putString(CharSequence charSequence, Charset charset);

    <T> Hasher putObject(T t, Funnel<? super T> funnel);

    HashCode hash();

    @Deprecated
    int hashCode();
}
