package net.bytebuddy;

import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.utility.RandomString;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy.class */
public interface NamingStrategy {
    String subclass(TypeDescription.Generic generic);

    String redefine(TypeDescription typeDescription);

    String rebase(TypeDescription typeDescription);

    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy$AbstractBase.class */
    public static abstract class AbstractBase implements NamingStrategy {
        protected abstract String name(TypeDescription typeDescription);

        @Override // net.bytebuddy.NamingStrategy
        public String subclass(TypeDescription.Generic superClass) {
            return name(superClass.asErasure());
        }

        @Override // net.bytebuddy.NamingStrategy
        public String redefine(TypeDescription typeDescription) {
            return typeDescription.getName();
        }

        @Override // net.bytebuddy.NamingStrategy
        public String rebase(TypeDescription typeDescription) {
            return typeDescription.getName();
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy$SuffixingRandom.class */
    public static class SuffixingRandom extends AbstractBase {
        public static final String BYTE_BUDDY_RENAME_PACKAGE = "net.bytebuddy.renamed";
        public static final String NO_PREFIX = "";
        private static final String JAVA_PACKAGE = "java.";
        private final String suffix;
        private final String javaLangPackagePrefix;
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
        private final RandomString randomString;
        private final BaseNameResolver baseNameResolver;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.suffix.equals(((SuffixingRandom) obj).suffix) && this.javaLangPackagePrefix.equals(((SuffixingRandom) obj).javaLangPackagePrefix) && this.baseNameResolver.equals(((SuffixingRandom) obj).baseNameResolver);
        }

        public int hashCode() {
            return (((((17 * 31) + this.suffix.hashCode()) * 31) + this.javaLangPackagePrefix.hashCode()) * 31) + this.baseNameResolver.hashCode();
        }

        public SuffixingRandom(String suffix) {
            this(suffix, BaseNameResolver.ForUnnamedType.INSTANCE);
        }

        public SuffixingRandom(String suffix, String javaLangPackagePrefix) {
            this(suffix, BaseNameResolver.ForUnnamedType.INSTANCE, javaLangPackagePrefix);
        }

        public SuffixingRandom(String suffix, BaseNameResolver baseNameResolver) {
            this(suffix, baseNameResolver, BYTE_BUDDY_RENAME_PACKAGE);
        }

        public SuffixingRandom(String suffix, BaseNameResolver baseNameResolver, String javaLangPackagePrefix) {
            this.suffix = suffix;
            this.baseNameResolver = baseNameResolver;
            this.javaLangPackagePrefix = javaLangPackagePrefix;
            this.randomString = new RandomString();
        }

        @Override // net.bytebuddy.NamingStrategy.AbstractBase
        protected String name(TypeDescription superClass) {
            String baseName = this.baseNameResolver.resolve(superClass);
            if (baseName.startsWith(JAVA_PACKAGE) && !this.javaLangPackagePrefix.equals("")) {
                baseName = this.javaLangPackagePrefix + "." + baseName;
            }
            return baseName + "$" + this.suffix + "$" + this.randomString.nextString();
        }

        /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy$SuffixingRandom$BaseNameResolver.class */
        public interface BaseNameResolver {
            String resolve(TypeDescription typeDescription);

            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy$SuffixingRandom$BaseNameResolver$ForUnnamedType.class */
            public enum ForUnnamedType implements BaseNameResolver {
                INSTANCE;

                @Override // net.bytebuddy.NamingStrategy.SuffixingRandom.BaseNameResolver
                public String resolve(TypeDescription typeDescription) {
                    return typeDescription.getName();
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy$SuffixingRandom$BaseNameResolver$ForGivenType.class */
            public static class ForGivenType implements BaseNameResolver {
                private final TypeDescription typeDescription;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.typeDescription.equals(((ForGivenType) obj).typeDescription);
                }

                public int hashCode() {
                    return (17 * 31) + this.typeDescription.hashCode();
                }

                public ForGivenType(TypeDescription typeDescription) {
                    this.typeDescription = typeDescription;
                }

                @Override // net.bytebuddy.NamingStrategy.SuffixingRandom.BaseNameResolver
                public String resolve(TypeDescription typeDescription) {
                    return this.typeDescription.getName();
                }
            }

            @HashCodeAndEqualsPlugin.Enhance
            /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy$SuffixingRandom$BaseNameResolver$ForFixedValue.class */
            public static class ForFixedValue implements BaseNameResolver {
                private final String name;

                public boolean equals(Object obj) {
                    if (this == obj) {
                        return true;
                    }
                    return obj != null && getClass() == obj.getClass() && this.name.equals(((ForFixedValue) obj).name);
                }

                public int hashCode() {
                    return (17 * 31) + this.name.hashCode();
                }

                public ForFixedValue(String name) {
                    this.name = name;
                }

                @Override // net.bytebuddy.NamingStrategy.SuffixingRandom.BaseNameResolver
                public String resolve(TypeDescription typeDescription) {
                    return this.name;
                }
            }
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/NamingStrategy$PrefixingRandom.class */
    public static class PrefixingRandom extends AbstractBase {
        private final String prefix;
        @HashCodeAndEqualsPlugin.ValueHandling(HashCodeAndEqualsPlugin.ValueHandling.Sort.IGNORE)
        private final RandomString randomString = new RandomString();

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.prefix.equals(((PrefixingRandom) obj).prefix);
        }

        public int hashCode() {
            return (17 * 31) + this.prefix.hashCode();
        }

        public PrefixingRandom(String prefix) {
            this.prefix = prefix;
        }

        @Override // net.bytebuddy.NamingStrategy.AbstractBase
        protected String name(TypeDescription superClass) {
            return this.prefix + "." + superClass.getName() + "$" + this.randomString.nextString();
        }
    }
}
