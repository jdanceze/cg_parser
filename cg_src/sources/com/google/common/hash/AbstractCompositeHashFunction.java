package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
@Immutable
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/AbstractCompositeHashFunction.class */
abstract class AbstractCompositeHashFunction extends AbstractHashFunction {
    final HashFunction[] functions;
    private static final long serialVersionUID = 0;

    abstract HashCode makeHash(Hasher[] hasherArr);

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractCompositeHashFunction(HashFunction... functions) {
        for (HashFunction function : functions) {
            Preconditions.checkNotNull(function);
        }
        this.functions = functions;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        Hasher[] hashers = new Hasher[this.functions.length];
        for (int i = 0; i < hashers.length; i++) {
            hashers[i] = this.functions[i].newHasher();
        }
        return fromHashers(hashers);
    }

    @Override // com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public Hasher newHasher(int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        Hasher[] hashers = new Hasher[this.functions.length];
        for (int i = 0; i < hashers.length; i++) {
            hashers[i] = this.functions[i].newHasher(expectedInputSize);
        }
        return fromHashers(hashers);
    }

    private Hasher fromHashers(final Hasher[] hashers) {
        return new Hasher() { // from class: com.google.common.hash.AbstractCompositeHashFunction.1
            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putByte(byte b) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putByte(b);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putBytes(byte[] bytes) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putBytes(bytes);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putBytes(byte[] bytes, int off, int len) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putBytes(bytes, off, len);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putBytes(ByteBuffer bytes) {
                Hasher[] hasherArr;
                int pos = bytes.position();
                for (Hasher hasher : hashers) {
                    bytes.position(pos);
                    hasher.putBytes(bytes);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putShort(short s) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putShort(s);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putInt(int i) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putInt(i);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putLong(long l) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putLong(l);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putFloat(float f) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putFloat(f);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putDouble(double d) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putDouble(d);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putBoolean(boolean b) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putBoolean(b);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putChar(char c) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putChar(c);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putUnencodedChars(CharSequence chars) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putUnencodedChars(chars);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putString(CharSequence chars, Charset charset) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putString(chars, charset);
                }
                return this;
            }

            @Override // com.google.common.hash.Hasher
            public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
                Hasher[] hasherArr;
                for (Hasher hasher : hashers) {
                    hasher.putObject(instance, funnel);
                }
                return this;
            }

            @Override // com.google.common.hash.Hasher
            public HashCode hash() {
                return AbstractCompositeHashFunction.this.makeHash(hashers);
            }
        };
    }
}
