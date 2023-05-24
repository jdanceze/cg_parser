package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.zip.Checksum;
/* JADX INFO: Access modifiers changed from: package-private */
@Immutable
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/ChecksumHashFunction.class */
public final class ChecksumHashFunction extends AbstractHashFunction implements Serializable {
    private final ImmutableSupplier<? extends Checksum> checksumSupplier;
    private final int bits;
    private final String toString;
    private static final long serialVersionUID = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChecksumHashFunction(ImmutableSupplier<? extends Checksum> checksumSupplier, int bits, String toString) {
        this.checksumSupplier = (ImmutableSupplier) Preconditions.checkNotNull(checksumSupplier);
        Preconditions.checkArgument(bits == 32 || bits == 64, "bits (%s) must be either 32 or 64", bits);
        this.bits = bits;
        this.toString = (String) Preconditions.checkNotNull(toString);
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return this.bits;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new ChecksumHasher(this.checksumSupplier.get());
    }

    public String toString() {
        return this.toString;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/ChecksumHashFunction$ChecksumHasher.class */
    private final class ChecksumHasher extends AbstractByteHasher {
        private final Checksum checksum;

        private ChecksumHasher(Checksum checksum) {
            this.checksum = (Checksum) Preconditions.checkNotNull(checksum);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte b) {
            this.checksum.update(b);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte[] bytes, int off, int len) {
            this.checksum.update(bytes, off, len);
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            long value = this.checksum.getValue();
            if (ChecksumHashFunction.this.bits == 32) {
                return HashCode.fromInt((int) value);
            }
            return HashCode.fromLong(value);
        }
    }
}
