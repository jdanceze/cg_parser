package org.jf.dexlib2;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/HiddenApiRestriction.class */
public enum HiddenApiRestriction {
    WHITELIST(0, "whitelist", false),
    GREYLIST(1, "greylist", false),
    BLACKLIST(2, "blacklist", false),
    GREYLIST_MAX_O(3, "greylist-max-o", false),
    GREYLIST_MAX_P(4, "greylist-max-p", false),
    GREYLIST_MAX_Q(5, "greylist-max-q", false),
    CORE_PLATFORM_API(8, "core-platform-api", true),
    TEST_API(16, "test-api", true);
    
    private static final int HIDDENAPI_FLAG_MASK = 7;
    private final int value;
    private final String name;
    private final boolean isDomainSpecificApiFlag;
    private static final HiddenApiRestriction[] hiddenApiFlags = {WHITELIST, GREYLIST, BLACKLIST, GREYLIST_MAX_O, GREYLIST_MAX_P, GREYLIST_MAX_Q};
    private static final HiddenApiRestriction[] domainSpecificApiFlags = {CORE_PLATFORM_API, TEST_API};
    private static final Map<String, HiddenApiRestriction> hiddenApiRestrictionsByName = new HashMap();

    static {
        HiddenApiRestriction[] values;
        for (HiddenApiRestriction hiddenApiRestriction : values()) {
            hiddenApiRestrictionsByName.put(hiddenApiRestriction.toString(), hiddenApiRestriction);
        }
    }

    HiddenApiRestriction(int value, String name, boolean isDomainSpecificApiFlag) {
        this.value = value;
        this.name = name;
        this.isDomainSpecificApiFlag = isDomainSpecificApiFlag;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isSet(int value) {
        return this.isDomainSpecificApiFlag ? (value & this.value) != 0 : (value & 7) == this.value;
    }

    public boolean isDomainSpecificApiFlag() {
        return this.isDomainSpecificApiFlag;
    }

    public static Set<HiddenApiRestriction> getAllFlags(int value) {
        HiddenApiRestriction[] hiddenApiRestrictionArr;
        HiddenApiRestriction normalRestriction = hiddenApiFlags[value & 7];
        int domainSpecificPart = value & (-8);
        if (domainSpecificPart == 0) {
            return ImmutableSet.of(normalRestriction);
        }
        ImmutableSet.Builder<HiddenApiRestriction> builder = ImmutableSet.builder();
        builder.add((ImmutableSet.Builder<HiddenApiRestriction>) normalRestriction);
        for (HiddenApiRestriction domainSpecificApiFlag : domainSpecificApiFlags) {
            if (domainSpecificApiFlag.isSet(value)) {
                builder.add((ImmutableSet.Builder<HiddenApiRestriction>) domainSpecificApiFlag);
            }
        }
        return builder.build();
    }

    public static String formatHiddenRestrictions(int value) {
        StringJoiner joiner = new StringJoiner("|");
        for (HiddenApiRestriction hiddenApiRestriction : getAllFlags(value)) {
            joiner.add(hiddenApiRestriction.toString());
        }
        return joiner.toString();
    }

    public static int combineFlags(Iterable<HiddenApiRestriction> flags) {
        boolean gotHiddenApiFlag = false;
        int value = 0;
        for (HiddenApiRestriction flag : flags) {
            if (flag.isDomainSpecificApiFlag) {
                value += flag.value;
            } else if (gotHiddenApiFlag) {
                throw new IllegalArgumentException("Cannot combine multiple flags for hidden api restrictions");
            } else {
                gotHiddenApiFlag = true;
                value += flag.value;
            }
        }
        return value;
    }

    public static HiddenApiRestriction forName(String name) {
        return hiddenApiRestrictionsByName.get(name);
    }
}
