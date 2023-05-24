package com.google.common.hash;

import android.widget.ExpandableListView;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/hash/FarmHashFingerprint64.class */
final class FarmHashFingerprint64 extends AbstractNonStreamingHashFunction {
    static final HashFunction FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
    private static final long K0 = -4348849565147123417L;
    private static final long K1 = -5435081209227447693L;
    private static final long K2 = -7286425919675154353L;

    FarmHashFingerprint64() {
    }

    @Override // com.google.common.hash.AbstractNonStreamingHashFunction, com.google.common.hash.AbstractHashFunction, com.google.common.hash.HashFunction
    public HashCode hashBytes(byte[] input, int off, int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        return HashCode.fromLong(fingerprint(input, off, len));
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 64;
    }

    public String toString() {
        return "Hashing.farmHashFingerprint64()";
    }

    @VisibleForTesting
    static long fingerprint(byte[] bytes, int offset, int length) {
        if (length <= 32) {
            if (length <= 16) {
                return hashLength0to16(bytes, offset, length);
            }
            return hashLength17to32(bytes, offset, length);
        } else if (length <= 64) {
            return hashLength33To64(bytes, offset, length);
        } else {
            return hashLength65Plus(bytes, offset, length);
        }
    }

    private static long shiftMix(long val) {
        return val ^ (val >>> 47);
    }

    private static long hashLength16(long u, long v, long mul) {
        long a = (u ^ v) * mul;
        long b = (v ^ (a ^ (a >>> 47))) * mul;
        return (b ^ (b >>> 47)) * mul;
    }

    private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
        long part1 = LittleEndianByteArray.load64(bytes, offset);
        long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
        long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
        long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
        long seedA2 = seedA + part1;
        long seedB2 = Long.rotateRight(seedB + seedA2 + part4, 21);
        long seedA3 = seedA2 + part2 + part3;
        output[0] = seedA3 + part4;
        output[1] = seedB2 + Long.rotateRight(seedA3, 44) + seedA2;
    }

    private static long hashLength0to16(byte[] bytes, int offset, int length) {
        if (length >= 8) {
            long mul = K2 + (length * 2);
            long a = LittleEndianByteArray.load64(bytes, offset) + K2;
            long b = LittleEndianByteArray.load64(bytes, (offset + length) - 8);
            long c = (Long.rotateRight(b, 37) * mul) + a;
            long d = (Long.rotateRight(a, 25) + b) * mul;
            return hashLength16(c, d, mul);
        } else if (length >= 4) {
            long mul2 = K2 + (length * 2);
            long a2 = LittleEndianByteArray.load32(bytes, offset) & ExpandableListView.PACKED_POSITION_VALUE_NULL;
            return hashLength16(length + (a2 << 3), LittleEndianByteArray.load32(bytes, (offset + length) - 4) & ExpandableListView.PACKED_POSITION_VALUE_NULL, mul2);
        } else if (length > 0) {
            byte a3 = bytes[offset];
            byte b2 = bytes[offset + (length >> 1)];
            byte c2 = bytes[offset + (length - 1)];
            int y = (a3 & 255) + ((b2 & 255) << 8);
            int z = length + ((c2 & 255) << 2);
            return shiftMix((y * K2) ^ (z * K0)) * K2;
        } else {
            return K2;
        }
    }

    private static long hashLength17to32(byte[] bytes, int offset, int length) {
        long mul = K2 + (length * 2);
        long a = LittleEndianByteArray.load64(bytes, offset) * K1;
        long b = LittleEndianByteArray.load64(bytes, offset + 8);
        long c = LittleEndianByteArray.load64(bytes, (offset + length) - 8) * mul;
        long d = LittleEndianByteArray.load64(bytes, (offset + length) - 16) * K2;
        return hashLength16(Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d, a + Long.rotateRight(b + K2, 18) + c, mul);
    }

    private static long hashLength33To64(byte[] bytes, int offset, int length) {
        long mul = K2 + (length * 2);
        long a = LittleEndianByteArray.load64(bytes, offset) * K2;
        long b = LittleEndianByteArray.load64(bytes, offset + 8);
        long c = LittleEndianByteArray.load64(bytes, (offset + length) - 8) * mul;
        long d = LittleEndianByteArray.load64(bytes, (offset + length) - 16) * K2;
        long y = Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d;
        long z = hashLength16(y, a + Long.rotateRight(b + K2, 18) + c, mul);
        long e = LittleEndianByteArray.load64(bytes, offset + 16) * mul;
        long f = LittleEndianByteArray.load64(bytes, offset + 24);
        long g = (y + LittleEndianByteArray.load64(bytes, (offset + length) - 32)) * mul;
        long h = (z + LittleEndianByteArray.load64(bytes, (offset + length) - 24)) * mul;
        return hashLength16(Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30) + h, e + Long.rotateRight(f + a, 18) + g, mul);
    }

    private static long hashLength65Plus(byte[] bytes, int offset, int length) {
        long y = 2480279821605975764L;
        long z = shiftMix((2480279821605975764L * K2) + 113) * K2;
        long[] v = new long[2];
        long[] w = new long[2];
        long x = (81 * K2) + LittleEndianByteArray.load64(bytes, offset);
        int end = offset + (((length - 1) / 64) * 64);
        int last64offset = (end + ((length - 1) & 63)) - 63;
        do {
            long x2 = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * K1;
            long y2 = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * K1;
            long x3 = x2 ^ w[1];
            y = y2 + v[0] + LittleEndianByteArray.load64(bytes, offset + 40);
            long z2 = Long.rotateRight(z + w[0], 33) * K1;
            weakHashLength32WithSeeds(bytes, offset, v[1] * K1, x3 + w[0], v);
            weakHashLength32WithSeeds(bytes, offset + 32, z2 + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
            x = z2;
            z = x3;
            offset += 64;
        } while (offset != end);
        long mul = K1 + ((z & 255) << 1);
        w[0] = w[0] + ((length - 1) & 63);
        v[0] = v[0] + w[0];
        w[0] = w[0] + v[0];
        long x4 = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, last64offset + 8), 37) * mul;
        long y3 = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, last64offset + 48), 42) * mul;
        long x5 = x4 ^ (w[1] * 9);
        long y4 = y3 + (v[0] * 9) + LittleEndianByteArray.load64(bytes, last64offset + 40);
        long z3 = Long.rotateRight(z + w[0], 33) * mul;
        weakHashLength32WithSeeds(bytes, last64offset, v[1] * mul, x5 + w[0], v);
        weakHashLength32WithSeeds(bytes, last64offset + 32, z3 + w[1], y4 + LittleEndianByteArray.load64(bytes, last64offset + 16), w);
        return hashLength16(hashLength16(v[0], w[0], mul) + (shiftMix(y4) * K0) + x5, hashLength16(v[1], w[1], mul) + z3, mul);
    }
}
