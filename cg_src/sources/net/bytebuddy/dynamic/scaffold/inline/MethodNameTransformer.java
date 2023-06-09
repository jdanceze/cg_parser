package net.bytebuddy.dynamic.scaffold.inline;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.utility.RandomString;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodNameTransformer.class */
public interface MethodNameTransformer {
    String transform(MethodDescription methodDescription);

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodNameTransformer$Suffixing.class */
    public static class Suffixing implements MethodNameTransformer {
        private static final String DEFAULT_SUFFIX = "original$";
        private final String suffix;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.suffix.equals(((Suffixing) obj).suffix);
        }

        public int hashCode() {
            return (17 * 31) + this.suffix.hashCode();
        }

        public static MethodNameTransformer withRandomSuffix() {
            return new Suffixing(DEFAULT_SUFFIX + RandomString.make());
        }

        public Suffixing(String suffix) {
            this.suffix = suffix;
        }

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer
        public String transform(MethodDescription methodDescription) {
            return methodDescription.getInternalName() + "$" + this.suffix;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/dynamic/scaffold/inline/MethodNameTransformer$Prefixing.class */
    public static class Prefixing implements MethodNameTransformer {
        private static final String DEFAULT_PREFIX = "original";
        private final String prefix;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.prefix.equals(((Prefixing) obj).prefix);
        }

        public int hashCode() {
            return (17 * 31) + this.prefix.hashCode();
        }

        public Prefixing() {
            this(DEFAULT_PREFIX);
        }

        public Prefixing(String prefix) {
            this.prefix = prefix;
        }

        @Override // net.bytebuddy.dynamic.scaffold.inline.MethodNameTransformer
        public String transform(MethodDescription methodDescription) {
            return this.prefix + methodDescription.getInternalName();
        }
    }
}
