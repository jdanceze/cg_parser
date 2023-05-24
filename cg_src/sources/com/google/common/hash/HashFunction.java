package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.Immutable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
@Immutable
@Beta
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/HashFunction.class */
public interface HashFunction {
    Hasher newHasher();

    Hasher newHasher(int i);

    HashCode hashInt(int i);

    HashCode hashLong(long j);

    HashCode hashBytes(byte[] bArr);

    HashCode hashBytes(byte[] bArr, int i, int i2);

    HashCode hashBytes(ByteBuffer byteBuffer);

    HashCode hashUnencodedChars(CharSequence charSequence);

    HashCode hashString(CharSequence charSequence, Charset charset);

    <T> HashCode hashObject(T t, Funnel<? super T> funnel);

    int bits();
}
