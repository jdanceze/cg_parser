package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.lang.Comparable;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import javax.resource.spi.work.WorkManager;
@GwtCompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/DiscreteDomain.class */
public abstract class DiscreteDomain<C extends Comparable> {
    final boolean supportsFastOffset;

    public abstract C next(C c);

    public abstract C previous(C c);

    public abstract long distance(C c, C c2);

    public static DiscreteDomain<Integer> integers() {
        return IntegerDomain.INSTANCE;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/DiscreteDomain$IntegerDomain.class */
    private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
        private static final IntegerDomain INSTANCE = new IntegerDomain();
        private static final long serialVersionUID = 0;

        IntegerDomain() {
            super(true);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer next(Integer value) {
            int i = value.intValue();
            if (i == Integer.MAX_VALUE) {
                return null;
            }
            return Integer.valueOf(i + 1);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Integer previous(Integer value) {
            int i = value.intValue();
            if (i == Integer.MIN_VALUE) {
                return null;
            }
            return Integer.valueOf(i - 1);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.DiscreteDomain
        public Integer offset(Integer origin, long distance) {
            CollectPreconditions.checkNonnegative(distance, "distance");
            return Integer.valueOf(Ints.checkedCast(origin.longValue() + distance));
        }

        @Override // com.google.common.collect.DiscreteDomain
        public long distance(Integer start, Integer end) {
            return end.intValue() - start.intValue();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.collect.DiscreteDomain
        public Integer minValue() {
            return Integer.MIN_VALUE;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.collect.DiscreteDomain
        public Integer maxValue() {
            return Integer.MAX_VALUE;
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.integers()";
        }
    }

    public static DiscreteDomain<Long> longs() {
        return LongDomain.INSTANCE;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/DiscreteDomain$LongDomain.class */
    private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
        private static final LongDomain INSTANCE = new LongDomain();
        private static final long serialVersionUID = 0;

        LongDomain() {
            super(true);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long next(Long value) {
            long l = value.longValue();
            if (l == WorkManager.INDEFINITE) {
                return null;
            }
            return Long.valueOf(l + 1);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public Long previous(Long value) {
            long l = value.longValue();
            if (l == Long.MIN_VALUE) {
                return null;
            }
            return Long.valueOf(l - 1);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.DiscreteDomain
        public Long offset(Long origin, long distance) {
            CollectPreconditions.checkNonnegative(distance, "distance");
            long result = origin.longValue() + distance;
            if (result < 0) {
                Preconditions.checkArgument(origin.longValue() < 0, "overflow");
            }
            return Long.valueOf(result);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public long distance(Long start, Long end) {
            long result = end.longValue() - start.longValue();
            if (end.longValue() > start.longValue() && result < 0) {
                return WorkManager.INDEFINITE;
            }
            if (end.longValue() < start.longValue() && result > 0) {
                return Long.MIN_VALUE;
            }
            return result;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.collect.DiscreteDomain
        public Long minValue() {
            return Long.MIN_VALUE;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.common.collect.DiscreteDomain
        public Long maxValue() {
            return Long.valueOf((long) WorkManager.INDEFINITE);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.longs()";
        }
    }

    public static DiscreteDomain<BigInteger> bigIntegers() {
        return BigIntegerDomain.INSTANCE;
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/collect/DiscreteDomain$BigIntegerDomain.class */
    private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
        private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
        private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
        private static final BigInteger MAX_LONG = BigInteger.valueOf(WorkManager.INDEFINITE);
        private static final long serialVersionUID = 0;

        BigIntegerDomain() {
            super(true);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public BigInteger next(BigInteger value) {
            return value.add(BigInteger.ONE);
        }

        @Override // com.google.common.collect.DiscreteDomain
        public BigInteger previous(BigInteger value) {
            return value.subtract(BigInteger.ONE);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.DiscreteDomain
        public BigInteger offset(BigInteger origin, long distance) {
            CollectPreconditions.checkNonnegative(distance, "distance");
            return origin.add(BigInteger.valueOf(distance));
        }

        @Override // com.google.common.collect.DiscreteDomain
        public long distance(BigInteger start, BigInteger end) {
            return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public String toString() {
            return "DiscreteDomain.bigIntegers()";
        }
    }

    protected DiscreteDomain() {
        this(false);
    }

    private DiscreteDomain(boolean supportsFastOffset) {
        this.supportsFastOffset = supportsFastOffset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public C offset(C origin, long distance) {
        CollectPreconditions.checkNonnegative(distance, "distance");
        long j = 0;
        while (true) {
            long i = j;
            if (i < distance) {
                origin = next(origin);
                j = i + 1;
            } else {
                return origin;
            }
        }
    }

    @CanIgnoreReturnValue
    public C minValue() {
        throw new NoSuchElementException();
    }

    @CanIgnoreReturnValue
    public C maxValue() {
        throw new NoSuchElementException();
    }
}
