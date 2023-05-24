package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.cache.LocalCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.compatqual.MonotonicNonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec.class */
public final class CacheBuilderSpec {
    private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
    private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", new MaximumSizeParser()).put("maximumWeight", new MaximumWeightParser()).put("concurrencyLevel", new ConcurrencyLevelParser()).put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK)).put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT)).put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK)).put("recordStats", new RecordStatsParser()).put("expireAfterAccess", new AccessDurationParser()).put("expireAfterWrite", new WriteDurationParser()).put("refreshAfterWrite", new RefreshDurationParser()).put("refreshInterval", new RefreshDurationParser()).build();
    @VisibleForTesting
    @MonotonicNonNullDecl
    Integer initialCapacity;
    @VisibleForTesting
    @MonotonicNonNullDecl
    Long maximumSize;
    @VisibleForTesting
    @MonotonicNonNullDecl
    Long maximumWeight;
    @VisibleForTesting
    @MonotonicNonNullDecl
    Integer concurrencyLevel;
    @VisibleForTesting
    @MonotonicNonNullDecl
    LocalCache.Strength keyStrength;
    @VisibleForTesting
    @MonotonicNonNullDecl
    LocalCache.Strength valueStrength;
    @VisibleForTesting
    @MonotonicNonNullDecl
    Boolean recordStats;
    @VisibleForTesting
    long writeExpirationDuration;
    @VisibleForTesting
    @MonotonicNonNullDecl
    TimeUnit writeExpirationTimeUnit;
    @VisibleForTesting
    long accessExpirationDuration;
    @VisibleForTesting
    @MonotonicNonNullDecl
    TimeUnit accessExpirationTimeUnit;
    @VisibleForTesting
    long refreshDuration;
    @VisibleForTesting
    @MonotonicNonNullDecl
    TimeUnit refreshTimeUnit;
    private final String specification;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$ValueParser.class */
    public interface ValueParser {
        void parse(CacheBuilderSpec cacheBuilderSpec, String str, @NullableDecl String str2);
    }

    private CacheBuilderSpec(String specification) {
        this.specification = specification;
    }

    public static CacheBuilderSpec parse(String cacheBuilderSpecification) {
        CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        if (!cacheBuilderSpecification.isEmpty()) {
            for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
                List<String> keyAndValue = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
                Preconditions.checkArgument(!keyAndValue.isEmpty(), "blank key-value pair");
                Preconditions.checkArgument(keyAndValue.size() <= 2, "key-value pair %s with more than one equals sign", keyValuePair);
                String key = keyAndValue.get(0);
                ValueParser valueParser = VALUE_PARSERS.get(key);
                Preconditions.checkArgument(valueParser != null, "unknown key %s", key);
                String value = keyAndValue.size() == 1 ? null : keyAndValue.get(1);
                valueParser.parse(spec, key, value);
            }
        }
        return spec;
    }

    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CacheBuilder<Object, Object> toCacheBuilder() {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        if (this.initialCapacity != null) {
            builder.initialCapacity(this.initialCapacity.intValue());
        }
        if (this.maximumSize != null) {
            builder.maximumSize(this.maximumSize.longValue());
        }
        if (this.maximumWeight != null) {
            builder.maximumWeight(this.maximumWeight.longValue());
        }
        if (this.concurrencyLevel != null) {
            builder.concurrencyLevel(this.concurrencyLevel.intValue());
        }
        if (this.keyStrength != null) {
            switch (this.keyStrength) {
                case WEAK:
                    builder.weakKeys();
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (this.valueStrength != null) {
            switch (this.valueStrength) {
                case WEAK:
                    builder.weakValues();
                    break;
                case SOFT:
                    builder.softValues();
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (this.recordStats != null && this.recordStats.booleanValue()) {
            builder.recordStats();
        }
        if (this.writeExpirationTimeUnit != null) {
            builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
        }
        if (this.accessExpirationTimeUnit != null) {
            builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
        }
        if (this.refreshTimeUnit != null) {
            builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
        }
        return builder;
    }

    public String toParsableString() {
        return this.specification;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
    }

    public int hashCode() {
        return Objects.hashCode(this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit));
    }

    public boolean equals(@NullableDecl Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheBuilderSpec)) {
            return false;
        }
        CacheBuilderSpec that = (CacheBuilderSpec) obj;
        return Objects.equal(this.initialCapacity, that.initialCapacity) && Objects.equal(this.maximumSize, that.maximumSize) && Objects.equal(this.maximumWeight, that.maximumWeight) && Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && Objects.equal(this.keyStrength, that.keyStrength) && Objects.equal(this.valueStrength, that.valueStrength) && Objects.equal(this.recordStats, that.recordStats) && Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(that.refreshDuration, that.refreshTimeUnit));
    }

    @NullableDecl
    private static Long durationInNanos(long duration, @NullableDecl TimeUnit unit) {
        if (unit == null) {
            return null;
        }
        return Long.valueOf(unit.toNanos(duration));
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$IntegerParser.class */
    static abstract class IntegerParser implements ValueParser {
        protected abstract void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i);

        IntegerParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, String value) {
            Preconditions.checkArgument((value == null || value.isEmpty()) ? false : true, "value of key %s omitted", key);
            try {
                parseInteger(spec, Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$LongParser.class */
    static abstract class LongParser implements ValueParser {
        protected abstract void parseLong(CacheBuilderSpec cacheBuilderSpec, long j);

        LongParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, String value) {
            Preconditions.checkArgument((value == null || value.isEmpty()) ? false : true, "value of key %s omitted", key);
            try {
                parseLong(spec, Long.parseLong(value));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value), e);
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$InitialCapacityParser.class */
    static class InitialCapacityParser extends IntegerParser {
        InitialCapacityParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.IntegerParser
        protected void parseInteger(CacheBuilderSpec spec, int value) {
            Preconditions.checkArgument(spec.initialCapacity == null, "initial capacity was already set to ", spec.initialCapacity);
            spec.initialCapacity = Integer.valueOf(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$MaximumSizeParser.class */
    static class MaximumSizeParser extends LongParser {
        MaximumSizeParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.LongParser
        protected void parseLong(CacheBuilderSpec spec, long value) {
            Preconditions.checkArgument(spec.maximumSize == null, "maximum size was already set to ", spec.maximumSize);
            Preconditions.checkArgument(spec.maximumWeight == null, "maximum weight was already set to ", spec.maximumWeight);
            spec.maximumSize = Long.valueOf(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$MaximumWeightParser.class */
    static class MaximumWeightParser extends LongParser {
        MaximumWeightParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.LongParser
        protected void parseLong(CacheBuilderSpec spec, long value) {
            Preconditions.checkArgument(spec.maximumWeight == null, "maximum weight was already set to ", spec.maximumWeight);
            Preconditions.checkArgument(spec.maximumSize == null, "maximum size was already set to ", spec.maximumSize);
            spec.maximumWeight = Long.valueOf(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$ConcurrencyLevelParser.class */
    static class ConcurrencyLevelParser extends IntegerParser {
        ConcurrencyLevelParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.IntegerParser
        protected void parseInteger(CacheBuilderSpec spec, int value) {
            Preconditions.checkArgument(spec.concurrencyLevel == null, "concurrency level was already set to ", spec.concurrencyLevel);
            spec.concurrencyLevel = Integer.valueOf(value);
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$KeyStrengthParser.class */
    static class KeyStrengthParser implements ValueParser {
        private final LocalCache.Strength strength;

        public KeyStrengthParser(LocalCache.Strength strength) {
            this.strength = strength;
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, @NullableDecl String value) {
            Preconditions.checkArgument(value == null, "key %s does not take values", key);
            Preconditions.checkArgument(spec.keyStrength == null, "%s was already set to %s", key, spec.keyStrength);
            spec.keyStrength = this.strength;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$ValueStrengthParser.class */
    static class ValueStrengthParser implements ValueParser {
        private final LocalCache.Strength strength;

        public ValueStrengthParser(LocalCache.Strength strength) {
            this.strength = strength;
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, @NullableDecl String value) {
            Preconditions.checkArgument(value == null, "key %s does not take values", key);
            Preconditions.checkArgument(spec.valueStrength == null, "%s was already set to %s", key, spec.valueStrength);
            spec.valueStrength = this.strength;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$RecordStatsParser.class */
    static class RecordStatsParser implements ValueParser {
        RecordStatsParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, @NullableDecl String value) {
            Preconditions.checkArgument(value == null, "recordStats does not take values");
            Preconditions.checkArgument(spec.recordStats == null, "recordStats already set");
            spec.recordStats = true;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$DurationParser.class */
    static abstract class DurationParser implements ValueParser {
        protected abstract void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit);

        DurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.ValueParser
        public void parse(CacheBuilderSpec spec, String key, String value) {
            TimeUnit timeUnit;
            Preconditions.checkArgument((value == null || value.isEmpty()) ? false : true, "value of key %s omitted", key);
            try {
                char lastChar = value.charAt(value.length() - 1);
                switch (lastChar) {
                    case 'd':
                        timeUnit = TimeUnit.DAYS;
                        break;
                    case 'h':
                        timeUnit = TimeUnit.HOURS;
                        break;
                    case 'm':
                        timeUnit = TimeUnit.MINUTES;
                        break;
                    case 's':
                        timeUnit = TimeUnit.SECONDS;
                        break;
                    default:
                        throw new IllegalArgumentException(CacheBuilderSpec.format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", key, value));
                }
                long duration = Long.parseLong(value.substring(0, value.length() - 1));
                parseDuration(spec, duration, timeUnit);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(CacheBuilderSpec.format("key %s value set to %s, must be integer", key, value));
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$AccessDurationParser.class */
    static class AccessDurationParser extends DurationParser {
        AccessDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.accessExpirationTimeUnit == null, "expireAfterAccess already set");
            spec.accessExpirationDuration = duration;
            spec.accessExpirationTimeUnit = unit;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$WriteDurationParser.class */
    static class WriteDurationParser extends DurationParser {
        WriteDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.writeExpirationTimeUnit == null, "expireAfterWrite already set");
            spec.writeExpirationDuration = duration;
            spec.writeExpirationTimeUnit = unit;
        }
    }

    /* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/cache/CacheBuilderSpec$RefreshDurationParser.class */
    static class RefreshDurationParser extends DurationParser {
        RefreshDurationParser() {
        }

        @Override // com.google.common.cache.CacheBuilderSpec.DurationParser
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            Preconditions.checkArgument(spec.refreshTimeUnit == null, "refreshAfterWrite already set");
            spec.refreshDuration = duration;
            spec.refreshTimeUnit = unit;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }
}
